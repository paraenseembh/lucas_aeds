#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <locale.h>

typedef struct { int ano, mes, dia; } Data;
typedef struct { int hora, minuto; } Hora;

#define MAX_TIPOS 5
#define MAX_TIPO_LEN 30
#define MAX_STR 100

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

#define MAX_REST 600
typedef struct {
    int tamanho;
    Restaurante* restaurantes[MAX_REST];
} Colecao_Restaurantes;

Data parse_data(char* s) {
    Data d;
    sscanf(s, "%d-%d-%d", &d.ano, &d.mes, &d.dia);
    return d;
}
void formatar_data(Data* d, char* buf) {
    sprintf(buf, "%02d/%02d/%04d", d->dia, d->mes, d->ano);
}
Hora parse_hora(char* s) {
    Hora h;
    sscanf(s, "%d:%d", &h.hora, &h.minuto);
    return h;
}
void formatar_hora(Hora* h, char* buf) {
    sprintf(buf, "%02d:%02d", h->hora, h->minuto);
}

Restaurante* parse_restaurante(char* s) {
    Restaurante* r = (Restaurante*)malloc(sizeof(Restaurante));
    char buf[1000];
    strncpy(buf, s, 999); buf[999] = '\0';
    int len = strlen(buf);
    while (len > 0 && (buf[len-1]=='\n'||buf[len-1]=='\r')) buf[--len]='\0';

    char* fields[11];
    int nf = 0;
    char* tok = strtok(buf, ",");
    while (tok && nf < 11) { fields[nf++] = tok; tok = strtok(NULL, ","); }

    r->id = atoi(fields[0]);
    strncpy(r->nome, fields[1], MAX_STR-1);
    strncpy(r->cidade, fields[2], MAX_STR-1);
    r->capacidade = atoi(fields[3]);
    r->avaliacao = atof(fields[4]);

    char tipos_buf[200];
    strncpy(tipos_buf, fields[5], 199);
    r->num_tipos = 0;
    char* tipo = strtok(tipos_buf, ";");
    while (tipo) {
        strncpy(r->tipos_cozinha[r->num_tipos++], tipo, MAX_TIPO_LEN-1);
        tipo = strtok(NULL, ";");
    }

    r->faixa_preco = strlen(fields[6]);
    sscanf(fields[7], "%d:%d-%d:%d",
           &r->horario_abertura.hora, &r->horario_abertura.minuto,
           &r->horario_fechamento.hora, &r->horario_fechamento.minuto);
    r->data_abertura = parse_data(fields[8]);

    char ab[10]; strncpy(ab, fields[9], 9); ab[9]='\0';
    len = strlen(ab);
    while (len>0&&(ab[len-1]=='\n'||ab[len-1]=='\r')) ab[--len]='\0';
    r->aberto = (strcmp(ab, "true") == 0) ? 1 : 0;
    return r;
}

void formatar_restaurante(Restaurante* r, char* buf) {
    char data_str[15], ha[10], hf[10];
    formatar_data(&r->data_abertura, data_str);
    formatar_hora(&r->horario_abertura, ha);
    formatar_hora(&r->horario_fechamento, hf);
    char faixa[6]; int i;
    for (i=0;i<r->faixa_preco;i++) faixa[i]='$'; faixa[i]='\0';
    char tipos_str[200]="";
    for (i=0;i<r->num_tipos;i++) {
        if (i>0) strcat(tipos_str, ",");
        strcat(tipos_str, r->tipos_cozinha[i]);
    }
    sprintf(buf, "[%d ## %s ## %s ## %d ## %.1f ## [%s] ## %s ## %s-%s ## %s ## %s]",
            r->id, r->nome, r->cidade, r->capacidade, r->avaliacao,
            tipos_str, faixa, ha, hf, data_str, r->aberto?"true":"false");
}

void ler_csv_colecao(Colecao_Restaurantes* col, char* path) {
    FILE* f = fopen(path, "r");
    if (!f) return;
    char line[1000];
    fgets(line, sizeof(line), f);
    col->tamanho = 0;
    while (fgets(line, sizeof(line), f))
        if (strlen(line) > 1)
            col->restaurantes[col->tamanho++] = parse_restaurante(line);
    fclose(f);
}

Colecao_Restaurantes* ler_csv() {
    Colecao_Restaurantes* col = (Colecao_Restaurantes*)malloc(sizeof(Colecao_Restaurantes));
    ler_csv_colecao(col, "/tmp/restaurantes.csv");
    return col;
}

int main() {
    setlocale(LC_NUMERIC, "C");
    Colecao_Restaurantes* col = ler_csv();
    int id;
    while (scanf("%d", &id) == 1 && id != -1) {
        char buf[500];
        for (int i = 0; i < col->tamanho; i++) {
            if (col->restaurantes[i]->id == id) {
                formatar_restaurante(col->restaurantes[i], buf);
                printf("%s\n", buf);
                break;
            }
        }
    }
    return 0;
}
