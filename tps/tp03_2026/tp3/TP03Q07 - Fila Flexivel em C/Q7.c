#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <locale.h>

#define MATRICULA "797798"
#define MAX_TIPOS 5
#define MAX_TIPO_LEN 30
#define MAX_STR 100
#define MAX_REST 600

typedef struct { int ano, mes, dia; } Data;
typedef struct { int hora, minuto; } Hora;

typedef struct {
    int id;
    char nome[MAX_STR];
    char cidade[MAX_STR];
    int capacidade;
    double avaliacao;
    char tipos_cozinha[MAX_TIPOS][MAX_TIPO_LEN];
    int num_tipos;
    int faixa_preco;
    Hora horario_abertura;
    Hora horario_fechamento;
    Data data_abertura;
    int aberto;
} Restaurante;

void formatar_data(Data* d, char* buf) {
    sprintf(buf, "%02d/%02d/%04d", d->dia, d->mes, d->ano);
}
void formatar_hora(Hora* h, char* buf) {
    sprintf(buf, "%02d:%02d", h->hora, h->minuto);
}

Restaurante* parse_restaurante(char* s) {
    Restaurante* r = (Restaurante*)malloc(sizeof(Restaurante));
    char buf[1000];
    strncpy(buf, s, 999); buf[999] = '\0';
    int len = strlen(buf);
    while (len > 0 && (buf[len-1]=='\n' || buf[len-1]=='\r')) buf[--len] = '\0';
    char* fields[11]; int nf = 0;
    char* tok = strtok(buf, ",");
    while (tok && nf < 11) { fields[nf++] = tok; tok = strtok(NULL, ","); }
    r->id = atoi(fields[0]);
    strncpy(r->nome, fields[1], MAX_STR-1);
    strncpy(r->cidade, fields[2], MAX_STR-1);
    r->capacidade = atoi(fields[3]);
    r->avaliacao = atof(fields[4]);
    char tb[200]; strncpy(tb, fields[5], 199);
    r->num_tipos = 0;
    char* tipo = strtok(tb, ";");
    while (tipo) {
        strncpy(r->tipos_cozinha[r->num_tipos++], tipo, MAX_TIPO_LEN-1);
        tipo = strtok(NULL, ";");
    }
    r->faixa_preco = strlen(fields[6]);
    sscanf(fields[7], "%d:%d-%d:%d",
           &r->horario_abertura.hora, &r->horario_abertura.minuto,
           &r->horario_fechamento.hora, &r->horario_fechamento.minuto);
    sscanf(fields[8], "%d-%d-%d",
           &r->data_abertura.ano, &r->data_abertura.mes, &r->data_abertura.dia);
    char ab[10]; strncpy(ab, fields[9], 9); ab[9] = '\0';
    len = strlen(ab);
    while (len > 0 && (ab[len-1]=='\n' || ab[len-1]=='\r')) ab[--len] = '\0';
    r->aberto = (strcmp(ab, "true") == 0) ? 1 : 0;
    return r;
}

void formatar_restaurante(Restaurante* r, char* buf) {
    char ds[15], ha[10], hf[10]; int i;
    formatar_data(&r->data_abertura, ds);
    formatar_hora(&r->horario_abertura, ha);
    formatar_hora(&r->horario_fechamento, hf);
    char faixa[6];
    for (i = 0; i < r->faixa_preco; i++) faixa[i] = '$'; faixa[i] = '\0';
    char ts[200] = "";
    for (i = 0; i < r->num_tipos; i++) {
        if (i > 0) strcat(ts, ",");
        strcat(ts, r->tipos_cozinha[i]);
    }
    sprintf(buf, "[%d ## %s ## %s ## %d ## %.1f ## [%s] ## %s ## %s-%s ## %s ## %s]",
            r->id, r->nome, r->cidade, r->capacidade, r->avaliacao,
            ts, faixa, ha, hf, ds, r->aberto ? "true" : "false");
}

typedef struct { int tamanho; Restaurante* restaurantes[MAX_REST]; } Colecao;

void ler_csv(Colecao* col) {
    FILE* f = fopen("/tmp/restaurantes.csv", "r");
    if (!f) return;
    char line[1000];
    fgets(line, sizeof(line), f);
    col->tamanho = 0;
    while (fgets(line, sizeof(line), f))
        if (strlen(line) > 1)
            col->restaurantes[col->tamanho++] = parse_restaurante(line);
    fclose(f);
}

/* ── Fila Flexivel (FIFO) - singly linked list ────────────────────────── */

typedef struct No {
    Restaurante* dado;
    struct No* proximo;
} No;

typedef struct {
    No* frente;
    No* tras;
    int tamanho;
} Fila;

Fila* criar_fila() {
    Fila* f = (Fila*)malloc(sizeof(Fila));
    f->frente = f->tras = NULL;
    f->tamanho = 0;
    return f;
}

/* Enqueue at end */
void enfileirar(Fila* f, Restaurante* r) {
    No* novo = (No*)malloc(sizeof(No));
    novo->dado = r;
    novo->proximo = NULL;
    if (!f->tras) {
        f->frente = f->tras = novo;
    } else {
        f->tras->proximo = novo;
        f->tras = novo;
    }
    f->tamanho++;
}

/* Dequeue from front */
Restaurante* desenfileirar(Fila* f) {
    if (!f->frente) return NULL;
    No* tmp = f->frente;
    Restaurante* r = tmp->dado;
    f->frente = tmp->proximo;
    if (!f->frente) f->tras = NULL;
    free(tmp);
    f->tamanho--;
    return r;
}

int main() {
    setlocale(LC_NUMERIC, "C");

    Colecao col; col.tamanho = 0;
    ler_csv(&col);

    Fila* fila = criar_fila();
    int id;
    while (scanf("%d", &id) == 1 && id != -1) {
        for (int i = 0; i < col.tamanho; i++) {
            if (col.restaurantes[i]->id == id) {
                enfileirar(fila, col.restaurantes[i]);
                break;
            }
        }
    }

    int n_ops;
    scanf("%d", &n_ops);
    char op[5];
    long long comparacoes = 0, movimentacoes = 0;
    clock_t ini = clock();

    for (int op_i = 0; op_i < n_ops; op_i++) {
        scanf("%s", op);
        if (strcmp(op, "I") == 0) {
            scanf("%d", &id);
            for (int i = 0; i < col.tamanho; i++) {
                if (col.restaurantes[i]->id == id) {
                    enfileirar(fila, col.restaurantes[i]);
                    movimentacoes++;
                    break;
                }
            }
        } else if (strcmp(op, "R") == 0) {
            Restaurante* r = desenfileirar(fila);
            if (r) { printf("(R)%s\n", r->nome); movimentacoes++; }
        }
    }

    clock_t fim = clock();
    double tempo = (double)(fim - ini) / CLOCKS_PER_SEC * 1000.0;

    char buf[500];
    for (No* n = fila->frente; n != NULL; n = n->proximo) {
        formatar_restaurante(n->dado, buf);
        printf("%s\n", buf);
    }

    FILE* log = fopen("797798_fila.txt", "w");
    fprintf(log, "%s\t%lld\t%lld\t%.2f\n", MATRICULA, comparacoes, movimentacoes, tempo);
    fclose(log);

    return 0;
}
