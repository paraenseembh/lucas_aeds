#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MATRICULA "868511"
#define MAX_STR 200
#define MAX_TIPOS 5
#define MAX_TIPO_LEN 50
#define MAX_REST 600

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

void formatar(Restaurante *r, char *buf, int bufsz) {
    char tipos_str[300] = "";
    for (int i = 0; i < r->num_tipos; i++) {
        if (i > 0) strcat(tipos_str, ",");
        strcat(tipos_str, r->tipos[i]);
    }
    char preco[10] = "";
    for (int i = 0; i < r->faixa_preco; i++) strcat(preco, "$");
    snprintf(buf, bufsz, "[%d ## %s ## %s ## %d ## %.1f ## [%s] ## %s ## %02d:%02d-%02d:%02d ## %02d/%02d/%04d ## %s]",
             r->id, r->nome, r->cidade, r->capacidade, r->avaliacao,
             tipos_str, preco,
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

/* ── Trie com Lista Flexível ─────────────────────────────────────── */

typedef struct No {
    char c;
    Restaurante *dado;   /* non-NULL if end of an inserted name */
    struct No *filhos;   /* head of child list */
    struct No *prox;     /* next sibling */
} No;

static No *raiz_trie = NULL;

static No *novo_no(char c) {
    No *n = malloc(sizeof(No));
    n->c = c;
    n->dado = NULL;
    n->filhos = NULL;
    n->prox = NULL;
    return n;
}

/* find child with character c in sibling list */
static No *achar_filho(No *head, char c) {
    No *cur = head;
    while (cur) {
        if (cur->c == c) return cur;
        cur = cur->prox;
    }
    return NULL;
}

/* add child to sibling list (append) */
static No *adicionar_filho(No *head, No *filho) {
    if (!head) return filho;
    No *cur = head;
    while (cur->prox) cur = cur->prox;
    cur->prox = filho;
    return head;
}

static void trie_inserir(Restaurante *r) {
    const char *nome = r->nome;
    int len = strlen(nome);

    if (len == 0) return;

    /* root is a dummy node; children of root are first chars */
    if (!raiz_trie) {
        raiz_trie = novo_no('\0'); /* dummy root */
    }

    No *cur = raiz_trie;
    for (int i = 0; i < len; i++) {
        No *filho = achar_filho(cur->filhos, nome[i]);
        if (!filho) {
            filho = novo_no(nome[i]);
            cur->filhos = adicionar_filho(cur->filhos, filho);
        }
        cur = filho;
    }
    cur->dado = r;
}

static void trie_buscar(const char *nome) {
    int len = strlen(nome);
    No *cur = raiz_trie;

    if (!cur) {
        printf("NAO\n");
        return;
    }

    for (int i = 0; i < len; i++) {
        No *filho = achar_filho(cur->filhos, nome[i]);
        if (!filho) {
            /* print the character that was NOT found? No — print NAO */
            printf("NAO\n");
            return;
        }
        printf("%c ", nome[i]);
        cur = filho;
    }

    if (cur->dado) {
        char buf[1024];
        formatar(cur->dado, buf, sizeof buf);
        printf("SIM %s\n", buf);
    } else {
        printf("NAO\n");
    }
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
    carregar_csv();

    int id;
    while (scanf("%d", &id) == 1) {
        if (id == -1) break;
        Restaurante *r = buscar_csv(id);
        if (r) trie_inserir(r);
    }
    { char c; while ((c = getchar()) != '\n' && c != EOF); }

    char query[MAX_STR];
    while (fgets(query, sizeof query, stdin)) {
        int len = strlen(query);
        while (len > 0 && (query[len-1]=='\n'||query[len-1]=='\r')) query[--len]='\0';
        if (strcmp(query, "FIM") == 0) break;
        trie_buscar(query);
    }

    /* log */
    FILE *log = fopen(MATRICULA "_arvore_trie_lista.txt", "w");
    if (log) { fprintf(log, "%s\t0\t0.00\n", MATRICULA); fclose(log); }

    return 0;
}
