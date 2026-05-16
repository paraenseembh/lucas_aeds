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

long long comparacoes = 0, movimentacoes = 0;

/* Compare dates: returns <0 if a older, >0 if a newer */
int compara_data(Restaurante* a, Restaurante* b) {
    comparacoes++;
    if (a->data_abertura.ano != b->data_abertura.ano)
        return a->data_abertura.ano - b->data_abertura.ano;
    if (a->data_abertura.mes != b->data_abertura.mes)
        return a->data_abertura.mes - b->data_abertura.mes;
    return a->data_abertura.dia - b->data_abertura.dia;
}

/* Sift up in max-heap (newest = root) */
void sift_up(Restaurante** heap, int i) {
    while (i > 0) {
        int p = (i - 1) / 2;
        if (compara_data(heap[p], heap[i]) < 0) {
            Restaurante* tmp = heap[p]; heap[p] = heap[i]; heap[i] = tmp;
            movimentacoes += 3;
            i = p;
        } else {
            break;
        }
    }
}

/* Sift down in max-heap (newest = root) */
void sift_down(Restaurante** heap, int i, int n) {
    while (1) {
        int lg = i, l = 2*i+1, r = 2*i+2;
        if (l < n) { if (compara_data(heap[l], heap[lg]) > 0) lg = l; }
        if (r < n) { if (compara_data(heap[r], heap[lg]) > 0) lg = r; }
        if (lg == i) break;
        Restaurante* tmp = heap[i]; heap[i] = heap[lg]; heap[lg] = tmp;
        movimentacoes += 3;
        i = lg;
    }
}

/*
 * Partial heapsort ascending by data_abertura.
 * Maintains a max-heap of size k = n/5 keeping the k oldest elements.
 * Elements not in the k-oldest set go into remaining[] in encounter order.
 * The k-oldest heap is sorted ascending (oldest first) and placed in arr[0..k-1].
 */
void heapsort_parcial(Restaurante** arr, int n, Restaurante** remaining, int* rem_size) {
    int k = n / 5;
    Restaurante* heap[MAX_REST];
    int heap_size = 0;
    *rem_size = 0;

    for (int i = 0; i < n; i++) {
        if (heap_size < k) {
            heap[heap_size] = arr[i];
            movimentacoes++;
            sift_up(heap, heap_size);
            heap_size++;
        } else {
            if (compara_data(arr[i], heap[0]) < 0) {
                /* arr[i] is older: replace heap root with arr[i], push old root to remaining */
                remaining[(*rem_size)++] = heap[0];
                heap[0] = arr[i];
                movimentacoes += 2;
                sift_down(heap, 0, k);
            } else {
                /* arr[i] is newer or equal: goes to remaining */
                remaining[(*rem_size)++] = arr[i];
                movimentacoes++;
            }
        }
    }

    /* Sort the k-oldest heap ascending using heapsort */
    int size = heap_size;
    for (int i = size - 1; i > 0; i--) {
        Restaurante* tmp = heap[0]; heap[0] = heap[i]; heap[i] = tmp;
        movimentacoes += 3;
        sift_down(heap, 0, i);
    }

    /* Copy sorted heap to arr[0..k-1] */
    for (int i = 0; i < heap_size; i++) {
        arr[i] = heap[i];
        movimentacoes++;
    }
}

int main() {
    setlocale(LC_NUMERIC, "C");

    Colecao col; col.tamanho = 0;
    ler_csv(&col);

    Restaurante* arr[MAX_REST];
    int n = 0;
    int id;
    while (scanf("%d", &id) == 1 && id != -1) {
        for (int i = 0; i < col.tamanho; i++) {
            if (col.restaurantes[i]->id == id) {
                arr[n++] = col.restaurantes[i];
                break;
            }
        }
    }

    Restaurante* remaining[MAX_REST];
    int rem_size = 0;

    clock_t ini = clock();
    heapsort_parcial(arr, n, remaining, &rem_size);
    clock_t fim = clock();
    double tempo = (double)(fim - ini) / CLOCKS_PER_SEC * 1000.0;

    int k = n / 5;
    char buf[500];
    for (int i = 0; i < k; i++) {
        formatar_restaurante(arr[i], buf);
        printf("%s\n", buf);
    }
    for (int i = 0; i < rem_size; i++) {
        formatar_restaurante(remaining[i], buf);
        printf("%s\n", buf);
    }

    FILE* log = fopen("797798_heapsort.txt", "w");
    fprintf(log, "%s\t%lld\t%lld\t%.2f\n", MATRICULA, comparacoes, movimentacoes, tempo);
    fclose(log);

    return 0;
}
