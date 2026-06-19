#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MATRICULA "868511"
#define MAX_STR 200
#define MAX_TIPOS 5
#define MAX_TIPO_LEN 50
#define MAX_REST 600

#define VERMELHO 0
#define PRETO    1

/* ── Restaurante ─────────────────────────────────────────────────── */

typedef struct {
    int id;
    char nome[MAX_STR];
    char cidade[MAX_STR];
    int capacidade;
    double avaliacao;
    char tipos[MAX_TIPOS][MAX_TIPO_LEN];
    int num_tipos;
    int faixa_preco;
    int ab_h, ab_m, fe_h, fe_m;
    int data_dia, data_mes, data_ano;
    int aberto;
} Restaurante;

void formatar(Restaurante *r) {
    printf("[%d ## %s ## %s ## %d ## %.1f ## [", r->id, r->nome, r->cidade,
           r->capacidade, r->avaliacao);
    for (int i = 0; i < r->num_tipos; i++) {
        if (i > 0) printf(",");
        printf("%s", r->tipos[i]);
    }
    printf("] ## ");
    for (int i = 0; i < r->faixa_preco; i++) printf("$");
    printf(" ## %02d:%02d-%02d:%02d ## %02d/%02d/%04d ## %s]\n",
           r->ab_h, r->ab_m, r->fe_h, r->fe_m,
           r->data_dia, r->data_mes, r->data_ano,
           r->aberto ? "true" : "false");
}

Restaurante *parse_restaurante(char *line) {
    Restaurante *r = malloc(sizeof(Restaurante));
    char buf[1024];
    strncpy(buf, line, 1023); buf[1023] = '\0';
    int len = strlen(buf);
    while (len > 0 && (buf[len-1]=='\n'||buf[len-1]=='\r')) buf[--len]='\0';

    char *f[12]; int nf = 0;
    char *tok = strtok(buf, ",");
    while (tok && nf < 12) { f[nf++] = tok; tok = strtok(NULL, ","); }

    r->id = atoi(f[0]);
    strncpy(r->nome, f[1], MAX_STR-1); r->nome[MAX_STR-1]='\0';
    strncpy(r->cidade, f[2], MAX_STR-1); r->cidade[MAX_STR-1]='\0';
    r->capacidade = atoi(f[3]);
    r->avaliacao = atof(f[4]);

    char tipos_buf[300]; strncpy(tipos_buf, f[5], 299);
    r->num_tipos = 0;
    char *tipo = strtok(tipos_buf, ";");
    while (tipo && r->num_tipos < MAX_TIPOS) {
        strncpy(r->tipos[r->num_tipos++], tipo, MAX_TIPO_LEN-1);
        tipo = strtok(NULL, ";");
    }

    r->faixa_preco = strlen(f[6]);
    sscanf(f[7], "%d:%d-%d:%d", &r->ab_h, &r->ab_m, &r->fe_h, &r->fe_m);
    sscanf(f[8], "%d-%d-%d", &r->data_ano, &r->data_mes, &r->data_dia);
    r->aberto = (strcmp(f[9], "true") == 0 || strncmp(f[9], "true", 4) == 0);
    return r;
}

/* ── Red-Black Tree ──────────────────────────────────────────────── */

typedef struct No {
    Restaurante *dado;
    int cor;
    struct No *esq, *dir, *pai;
} No;

static No *NIL;   /* sentinel */
static No *raiz;

static No *novo_no(Restaurante *r) {
    No *n = malloc(sizeof(No));
    n->dado = r; n->cor = VERMELHO;
    n->esq = n->dir = n->pai = NIL;
    return n;
}

static void rot_esq(No *x) {
    No *y = x->dir;
    x->dir = y->esq;
    if (y->esq != NIL) y->esq->pai = x;
    y->pai = x->pai;
    if (x->pai == NIL)       raiz = y;
    else if (x == x->pai->esq) x->pai->esq = y;
    else                       x->pai->dir = y;
    y->esq = x; x->pai = y;
}

static void rot_dir(No *x) {
    No *y = x->esq;
    x->esq = y->dir;
    if (y->dir != NIL) y->dir->pai = x;
    y->pai = x->pai;
    if (x->pai == NIL)       raiz = y;
    else if (x == x->pai->dir) x->pai->dir = y;
    else                       x->pai->esq = y;
    y->dir = x; x->pai = y;
}

static void fix_insert(No *z) {
    while (z->pai->cor == VERMELHO) {
        if (z->pai == z->pai->pai->esq) {
            No *y = z->pai->pai->dir;
            if (y->cor == VERMELHO) {           /* caso 1 */
                z->pai->cor = PRETO;
                y->cor = PRETO;
                z->pai->pai->cor = VERMELHO;
                z = z->pai->pai;
            } else {
                if (z == z->pai->dir) {         /* caso 2 → 3 */
                    z = z->pai; rot_esq(z);
                }
                z->pai->cor = PRETO;            /* caso 3 */
                z->pai->pai->cor = VERMELHO;
                rot_dir(z->pai->pai);
            }
        } else {
            No *y = z->pai->pai->esq;
            if (y->cor == VERMELHO) {
                z->pai->cor = PRETO;
                y->cor = PRETO;
                z->pai->pai->cor = VERMELHO;
                z = z->pai->pai;
            } else {
                if (z == z->pai->esq) {
                    z = z->pai; rot_dir(z);
                }
                z->pai->cor = PRETO;
                z->pai->pai->cor = VERMELHO;
                rot_esq(z->pai->pai);
            }
        }
    }
    raiz->cor = PRETO;
}

static void inserir(Restaurante *r) {
    No *z = novo_no(r);
    No *y = NIL, *x = raiz;
    while (x != NIL) {
        y = x;
        int cmp = strcmp(r->nome, x->dado->nome);
        if (cmp < 0)      x = x->esq;
        else if (cmp > 0) x = x->dir;
        else { free(z); return; }
    }
    z->pai = y;
    if (y == NIL)             raiz = z;
    else if (strcmp(r->nome, y->dado->nome) < 0) y->esq = z;
    else                                          y->dir = z;
    fix_insert(z);
}

/* busca com rastreamento do caminho */
static void buscar(const char *nome) {
    printf("raiz");
    No *x = raiz;
    while (x != NIL) {
        int cmp = strcmp(nome, x->dado->nome);
        if (cmp == 0) { printf(" SIM\n"); return; }
        if (cmp < 0)  { printf(" esq"); x = x->esq; }
        else          { printf(" dir"); x = x->dir; }
    }
    printf(" NAO\n");
}

static void inorder(No *n) {
    if (n == NIL) return;
    inorder(n->esq);
    formatar(n->dado);
    inorder(n->dir);
}

/* ── CSV ──────────────────────────────────────────────────────────── */

static Restaurante *csv[MAX_REST];
static int csv_tam = 0;

static void carregar_csv(void) {
    FILE *f = fopen("/tmp/restaurantes.csv", "r");
    if (!f) { fprintf(stderr, "Erro ao abrir CSV\n"); exit(1); }
    char line[1024];
    fgets(line, sizeof line, f); /* header */
    while (fgets(line, sizeof line, f)) {
        if (line[0] == '\n' || line[0] == '\r') continue;
        csv[csv_tam++] = parse_restaurante(line);
    }
    fclose(f);
}

static Restaurante *buscar_csv(int id) {
    for (int i = 0; i < csv_tam; i++)
        if (csv[i]->id == id) return csv[i];
    return NULL;
}

/* ── Main ─────────────────────────────────────────────────────────── */

int main(void) {
    NIL = malloc(sizeof(No));
    NIL->cor = PRETO; NIL->esq = NIL->dir = NIL->pai = NIL; NIL->dado = NULL;
    raiz = NIL;

    carregar_csv();

    int id;
    while (scanf("%d", &id) == 1) {
        if (id == -1) break;
        Restaurante *r = buscar_csv(id);
        if (r) inserir(r);
    }
    /* consume newline */
    { char c; while ((c = getchar()) != '\n' && c != EOF); }

    char query[MAX_STR];
    while (fgets(query, sizeof query, stdin)) {
        int len = strlen(query);
        while (len > 0 && (query[len-1]=='\n'||query[len-1]=='\r')) query[--len]='\0';
        if (strcmp(query, "FIM") == 0) break;
        buscar(query);
    }

    inorder(raiz);

    /* log */
    FILE *log = fopen(MATRICULA "_rbt.txt", "w");
    if (log) { fprintf(log, "%s\t%d\t%d\t%.2fms\n", MATRICULA, 0, 0, 0.0); fclose(log); }

    return 0;
}
