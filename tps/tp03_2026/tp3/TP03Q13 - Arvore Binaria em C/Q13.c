#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

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

/* ── Parse / Formatacao ─────────────────────────────────────────────────── */

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
    strncpy(r->nome, fields[1], MAX_STR-1); r->nome[MAX_STR-1] = '\0';
    strncpy(r->cidade, fields[2], MAX_STR-1); r->cidade[MAX_STR-1] = '\0';
    r->capacidade = atoi(fields[3]);
    r->avaliacao = atof(fields[4]);

    char tb[200]; strncpy(tb, fields[5], 199); tb[199] = '\0';
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
    char faixa[6]; int i;
    for (i = 0; i < r->faixa_preco; i++) faixa[i] = '$'; faixa[i] = '\0';
    char ts[200] = "";
    for (i = 0; i < r->num_tipos; i++) {
        if (i > 0) strcat(ts, ",");
        strcat(ts, r->tipos_cozinha[i]);
    }
    sprintf(buf, "[%d ## %s ## %s ## %d ## %.1f ## [%s] ## %s ## %02d:%02d-%02d:%02d ## %02d/%02d/%04d ## %s]",
            r->id, r->nome, r->cidade, r->capacidade, r->avaliacao,
            ts, faixa,
            r->horario_abertura.hora, r->horario_abertura.minuto,
            r->horario_fechamento.hora, r->horario_fechamento.minuto,
            r->data_abertura.dia, r->data_abertura.mes, r->data_abertura.ano,
            r->aberto ? "true" : "false");
}

/* ── CSV Loading ────────────────────────────────────────────────────────── */

typedef struct { int tamanho; Restaurante* restaurantes[MAX_REST]; } Colecao;

void ler_csv(Colecao* col) {
    FILE* f = fopen("/tmp/restaurantes.csv", "r");
    if (!f) return;
    char line[1000];
    fgets(line, sizeof(line), f); /* cabecalho */
    col->tamanho = 0;
    while (fgets(line, sizeof(line), f))
        if (strlen(line) > 1)
            col->restaurantes[col->tamanho++] = parse_restaurante(line);
    fclose(f);
}

/* ── BST ────────────────────────────────────────────────────────────────── */

typedef struct No {
    Restaurante* dado;
    struct No* esq;
    struct No* dir;
} No;

No* raiz = NULL;

void inserir(Restaurante* r) {
    No* novo = (No*)malloc(sizeof(No));
    novo->dado = r; novo->esq = NULL; novo->dir = NULL;
    if (!raiz) { raiz = novo; return; }
    No* atual = raiz;
    while (1) {
        int cmp = strcmp(r->nome, atual->dado->nome);
        if (cmp < 0) {
            if (!atual->esq) { atual->esq = novo; return; }
            atual = atual->esq;
        } else {
            if (!atual->dir) { atual->dir = novo; return; }
            atual = atual->dir;
        }
    }
}

void buscar(const char* nome) {
    printf("raiz");
    No* atual = raiz;
    while (atual) {
        int cmp = strcmp(nome, atual->dado->nome);
        if (cmp == 0) { printf(" SIM\n"); return; }
        if (cmp < 0) { printf(" esq"); atual = atual->esq; }
        else         { printf(" dir"); atual = atual->dir; }
    }
    printf(" NAO\n");
}

void inorder(No* no) {
    if (!no) return;
    inorder(no->esq);
    char buf[600];
    formatar_restaurante(no->dado, buf);
    printf("%s\n", buf);
    inorder(no->dir);
}

/* ── Main ───────────────────────────────────────────────────────────────── */

int main() {
    Colecao col; col.tamanho = 0;
    ler_csv(&col);

    int id;
    while (scanf("%d", &id) == 1 && id != -1) {
        for (int i = 0; i < col.tamanho; i++) {
            if (col.restaurantes[i]->id == id) {
                inserir(col.restaurantes[i]);
                break;
            }
        }
    }
    /* consume newline after -1 */
    { char tmp[5]; fgets(tmp, sizeof(tmp), stdin); }

    char query[MAX_STR];
    while (fgets(query, sizeof(query), stdin)) {
        int len = strlen(query);
        while (len > 0 && (query[len-1]=='\n' || query[len-1]=='\r')) query[--len] = '\0';
        if (strcmp(query, "FIM") == 0) break;
        buscar(query);
    }

    inorder(raiz);

    FILE* log = fopen("797798_busca.txt", "w");
    fprintf(log, "%s\t%d\t%d\t%.2f\n", MATRICULA, 0, 0, 0.0);
    fclose(log);

    return 0;
}
