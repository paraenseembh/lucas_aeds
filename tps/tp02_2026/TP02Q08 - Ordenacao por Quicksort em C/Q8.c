#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <locale.h>

#define MATRICULA "797798"
#define MAX_TIPOS 5
#define MAX_TIPO_LEN 30
#define MAX_STR 100

typedef struct { int ano, mes, dia; } Data;
typedef struct { int hora, minuto; } Hora;
typedef struct {
    int id; char nome[MAX_STR]; char cidade[MAX_STR];
    int capacidade; double avaliacao;
    char tipos_cozinha[MAX_TIPOS][MAX_TIPO_LEN]; int num_tipos;
    int faixa_preco;
    Hora horario_abertura; Hora horario_fechamento;
    Data data_abertura; int aberto;
} Restaurante;

#define MAX_REST 600
typedef struct { int tamanho; Restaurante* restaurantes[MAX_REST]; } Colecao_Restaurantes;

void formatar_data(Data* d,char* b){sprintf(b,"%02d/%02d/%04d",d->dia,d->mes,d->ano);}
void formatar_hora(Hora* h,char* b){sprintf(b,"%02d:%02d",h->hora,h->minuto);}

Restaurante* parse_restaurante(char* s) {
    Restaurante* r=(Restaurante*)malloc(sizeof(Restaurante));
    char buf[1000];strncpy(buf,s,999);buf[999]='\0';
    int len=strlen(buf);while(len>0&&(buf[len-1]=='\n'||buf[len-1]=='\r'))buf[--len]='\0';
    char* fields[11];int nf=0;char* tok=strtok(buf,",");
    while(tok&&nf<11){fields[nf++]=tok;tok=strtok(NULL,",");}
    r->id=atoi(fields[0]);strncpy(r->nome,fields[1],MAX_STR-1);strncpy(r->cidade,fields[2],MAX_STR-1);
    r->capacidade=atoi(fields[3]);r->avaliacao=atof(fields[4]);
    char tb[200];strncpy(tb,fields[5],199);r->num_tipos=0;char* tipo=strtok(tb,";");
    while(tipo){strncpy(r->tipos_cozinha[r->num_tipos++],tipo,MAX_TIPO_LEN-1);tipo=strtok(NULL,";");}
    r->faixa_preco=strlen(fields[6]);
    sscanf(fields[7],"%d:%d-%d:%d",&r->horario_abertura.hora,&r->horario_abertura.minuto,
           &r->horario_fechamento.hora,&r->horario_fechamento.minuto);
    sscanf(fields[8],"%d-%d-%d",&r->data_abertura.ano,&r->data_abertura.mes,&r->data_abertura.dia);
    char ab[10];strncpy(ab,fields[9],9);ab[9]='\0';
    len=strlen(ab);while(len>0&&(ab[len-1]=='\n'||ab[len-1]=='\r'))ab[--len]='\0';
    r->aberto=(strcmp(ab,"true")==0)?1:0;return r;
}

void formatar_restaurante(Restaurante* r,char* buf){
    char ds[15],ha[10],hf[10];int i;
    formatar_data(&r->data_abertura,ds);formatar_hora(&r->horario_abertura,ha);formatar_hora(&r->horario_fechamento,hf);
    char faixa[6];for(i=0;i<r->faixa_preco;i++)faixa[i]='$';faixa[i]='\0';
    char ts[200]="";for(i=0;i<r->num_tipos;i++){if(i>0)strcat(ts,",");strcat(ts,r->tipos_cozinha[i]);}
    sprintf(buf,"[%d ## %s ## %s ## %d ## %.1f ## [%s] ## %s ## %s-%s ## %s ## %s]",
            r->id,r->nome,r->cidade,r->capacidade,r->avaliacao,ts,faixa,ha,hf,ds,r->aberto?"true":"false");
}

void ler_csv_colecao(Colecao_Restaurantes* col,char* path){
    FILE* f=fopen(path,"r");if(!f)return;
    char line[1000];fgets(line,sizeof(line),f);col->tamanho=0;
    while(fgets(line,sizeof(line),f))if(strlen(line)>1)col->restaurantes[col->tamanho++]=parse_restaurante(line);
    fclose(f);
}

long long comparacoes=0, movimentacoes=0;

int compara(Restaurante* a, Restaurante* b){
    if(a->avaliacao < b->avaliacao) return -1;
    if(a->avaliacao > b->avaliacao) return 1;
    return strcmp(a->nome, b->nome);
}

void trocar(Restaurante** arr,int i,int j){
    Restaurante* tmp=arr[i];arr[i]=arr[j];arr[j]=tmp;movimentacoes+=3;
}

void quicksort(Restaurante** arr, int lo, int hi){
    if(lo>=hi) return;
    Restaurante* pivo=arr[hi];
    int i=lo-1;
    for(int j=lo;j<hi;j++){
        comparacoes++;
        if(compara(arr[j],pivo)<=0){i++;trocar(arr,i,j);}
    }
    trocar(arr,i+1,hi);
    int p=i+1;
    quicksort(arr,lo,p-1);
    quicksort(arr,p+1,hi);
}

int main(){
    setlocale(LC_NUMERIC,"C");
    Colecao_Restaurantes col;col.tamanho=0;
    ler_csv_colecao(&col,"/tmp/restaurantes.csv");
    Restaurante* arr[600];int n=0;int id;
    while(scanf("%d",&id)==1&&id!=-1)
        for(int i=0;i<col.tamanho;i++)if(col.restaurantes[i]->id==id){arr[n++]=col.restaurantes[i];break;}
    clock_t ini=clock();
    quicksort(arr,0,n-1);
    clock_t fim=clock();
    double tempo=(double)(fim-ini)/CLOCKS_PER_SEC*1000.0;
    char buf[500];
    for(int i=0;i<n;i++){formatar_restaurante(arr[i],buf);printf("%s\n",buf);}
    FILE* log=fopen("matricula_quicksort.txt","w");
    fprintf(log,"%s\t%lld\t%lld\t%.2f\n",MATRICULA,comparacoes,movimentacoes,tempo);
    fclose(log);
    return 0;
}
