#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MATRICULA "797798"
#define MAX_STR 200
#define MAX_TIPOS 5
#define MAX_TIPO_LEN 50
#define MAX_REST 600
#define TAM_HASH 31
#define TAM_RESERVA 19
#define TAM_TOTAL 50   /* 31 + 19 */

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
    printf("[%d ## %s ## %s ## %d ## %.1f ## [",
           r->id, r->nome, r->cidade, r->capacidade, r->avaliacao);
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
    r->aberto = (strncmp(f[9], "true", 4) == 0);
    return r;
}

static Restaurante *csv[MAX_REST];
static int csv_tam = 0;

static void carregar_csv(void) {
    FILE *f = fopen("/tmp/restaurantes.csv", "r");
    if (!f) { fprintf(stderr, "Erro ao abrir CSV\n"); exit(1); }
    char line[1024];
    fgets(line, sizeof line, f);
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

/* Tabela: posicoes 0..30 = area hash, 31..49 = area de reserva */
static Restaurante *tabela[TAM_TOTAL];
static int reserva_prox = TAM_HASH; /* proximo slot livre na reserva */

static int hash_func(const char *nome) {
    int soma = 0;
    for (int i = 0; nome[i]; i++) soma += (unsigned char)nome[i];
    return soma % TAM_HASH;
}

static void inserir(Restaurante *r) {
    int pos = hash_func(r->nome);
    if (tabela[pos] == NULL) {
        tabela[pos] = r;
    } else {
        /* colisao: vai para reserva */
        if (reserva_prox < TAM_TOTAL) {
            tabela[reserva_prox++] = r;
        } else {
            printf("%s\n", r->nome);
        }
    }
}

static void buscar(const char *nome) {
    int pos = hash_func(nome);
    /* verifica posicao hash */
    if (tabela[pos] != NULL && strcmp(tabela[pos]->nome, nome) == 0) {
        printf("%d ", pos);
        formatar(tabela[pos]);
        return;
    }
    /* varredura na area de reserva */
    for (int i = TAM_HASH; i < reserva_prox; i++) {
        if (tabela[i] != NULL && strcmp(tabela[i]->nome, nome) == 0) {
            printf("%d ", i);
            formatar(tabela[i]);
            return;
        }
    }
    printf("-1\n");
}

int main(void) {
    memset(tabela, 0, sizeof tabela);
    carregar_csv();

    int id;
    while (scanf("%d", &id) == 1) {
        if (id == -1) break;
        Restaurante *r = buscar_csv(id);
        if (r) inserir(r);
    }
    { char c; while ((c = getchar()) != '\n' && c != EOF); }

    char query[MAX_STR];
    while (fgets(query, sizeof query, stdin)) {
        int len = strlen(query);
        while (len > 0 && (query[len-1]=='\n'||query[len-1]=='\r')) query[--len]='\0';
        if (strcmp(query, "FIM") == 0) break;
        buscar(query);
    }

    FILE *log = fopen(MATRICULA "_hash_reserva.txt", "w");
    if (log) { fprintf(log, "%s\t0\t0.00\n", MATRICULA); fclose(log); }

    return 0;
}
