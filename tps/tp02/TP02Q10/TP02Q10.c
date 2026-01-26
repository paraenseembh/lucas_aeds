/**
 * TP02Q10 - Sorting with Quicksort in C
 * Sorting the Disney+ show catalog by the date_added attribute
 * In case of a tie, sort by the title attribute
 * 
 * Records execution time, comparisons, and movements in a log file
 */

 #include <stdio.h>
 #include <stdlib.h>
 #include <string.h>
 #include <time.h>
 #define MAX_SHOWS 1368
 #define LINE_SIZE 1000
 
 typedef struct Show {
     char *show_id;
     char *type;
     char *title;
     char *director;
     char *cast;
     char *country;
     char *date_added;
     char *release_year;
     char *rating;
     char *duration;
     char *listed_in;
     char *description;
     char **cast_array;
     char **categories;
     int cast_size;
     int categories_size;
     int day, month, year;
     int has_date;
 } Show;
 
 int getLength(char* line){
     int length = 0;
     while(line[length] != '\0'){
         length++;
     }
     return length;
 }
 
 void freeShow(Show *s) {
     if (s == NULL) return;
 
     free(s->show_id);
     free(s->type);
     free(s->title);
     free(s->director);
     free(s->cast);
     free(s->country);
     free(s->date_added);
     free(s->release_year);
     free(s->rating);
     free(s->duration);
     free(s->listed_in);
     free(s->description);
 
     if (s->cast_array != NULL) {
         for (int i = 0; i < s->cast_size; i++) {
             free(s->cast_array[i]);
         }
         free(s->cast_array);
     }
 
     if (s->categories != NULL) {
         for (int i = 0; i < s->categories_size; i++) {
             free(s->categories[i]);
         }
         free(s->categories);
     }
 }
 
 void trim(char *str) {
     if (str == NULL) return;
     
     int start = 0;
     while (str[start] == ' ') start++;
 
     int end = strlen(str) - 1;
     while (end > start && str[end] == ' ') end--;
 
     int i, j = 0;
     for (i = start; i <= end; i++) {
         str[j++] = str[i];
     }
     str[j] = '\0';
 }
 
 char *extractField(char *next, int *extracted){
     char *field = malloc(1000);
     int i=0, j=0, continue_extract = 1, in_quotes = 0, characters = 0;
 
     while(continue_extract && next[i] != '\0') {
         if(!in_quotes) {
             if(next[i] == ',') {
                 continue_extract = !continue_extract;
             } else {
                 if(next[i] == '"') {
                     in_quotes = !in_quotes;
                 } else {
                     field[j++] = next[i];
                     characters++;
                 }
             }
         } else {
             if(next[i] == '"') {
                 in_quotes = !in_quotes;
             } else {
                 field[j++] = next[i];
                 characters++;
             }
         }
         i++;
     }
     if(characters == 0) {
         field[j++] = 'N';
         field[j++] = 'a';
         field[j++] = 'N';
     }
     field[j] = '\0';
     *extracted = i;
 
     return field;
 }
 
 char **splitString(char *line, int *size) {
     if (line == NULL || strcmp(line, "NaN") == 0) {
         *size = 0;
         return NULL;
     }
 
     int i = 0, j = 0, k = 0;
     *size = 1;
 
     for (i = 0; line[i] != '\0'; i++) {
         if (line[i] == ',') {
             (*size)++;
         }
     }
 
     char **result = (char **)malloc(sizeof(char*) * (*size));
     for (i = 0; i < *size; i++) {
         result[i] = NULL;
     }
 
     i = j = k = 0;
     result[k] = (char *)malloc(sizeof(char) * (strlen(line) + 1));
 
     while (line[i] != '\0') {
         if (line[i] == ',') {
             result[k][j] = '\0';
             trim(result[k]);
             k++;
             j = 0;
             result[k] = (char *)malloc(sizeof(char) * (strlen(line) + 1));
         } else if (line[i] != '"') {
             result[k][j++] = line[i];
         }
         i++;
     }
 
     result[k][j] = '\0';
     trim(result[k]);
 
     // Sort array of strings
     for (int m = 0; m < *size - 1; m++) {
         int smallest = m;
         for (int n = m + 1; n < *size; n++) {
             if (strcmp(result[n], result[smallest]) < 0) {
                 smallest = n;
             }
         }
         if (smallest != m) {
             char *temp = result[m];
             result[m] = result[smallest];
             result[smallest] = temp;
         }
     }
 
     return result;
 }
 
 Show *readShow(char *line){
     Show *show = (Show *)malloc(sizeof(Show));
     int i = 0, offset = 0;
     char *fields[12];
 
     for (int c = 0; c < 12; c++) {
         fields[c] = extractField(line + i, &offset);
         i += offset;
     }
 
     show->show_id = fields[0];
     show->type = fields[1];
     show->title = fields[2];
     show->director = fields[3];
     show->cast = fields[4];
     show->country = fields[5];
     show->date_added = fields[6];
     show->release_year = fields[7];
     show->rating = fields[8];
     show->duration = fields[9];
     show->listed_in = fields[10];
     show->description = fields[11];
 
     show->cast_array = splitString(show->cast, &show->cast_size);
     show->categories = splitString(show->listed_in, &show->categories_size);
     show->has_date = 0;
 
     return show;
 }
 
 void printArray(char **array, int size){
     printf("[");
     for(int i=0; i<size; i++){
         printf("%s", array[i]);
         if(i < size-1){
             printf(", ");
         }
     }
     printf("]");
 }
 
 void printShow(Show *show){
     printf("=> %s ## %s ## %s ## %s ## ", show->show_id, show->title, show->type, show->director);
     printArray(show->cast_array, show->cast_size);
     printf(" ## %s ## %s ## %s ## %s ## %s ## ", show->country, show->date_added, 
         show->release_year, show->rating, show->duration);
     printArray(show->categories, show->categories_size);
     printf(" ##\n");
 }
 
 int monthToInt(char *month) {
     if (month == NULL) return 0;
     
     if (strstr(month, "January")) return 1;
     if (strstr(month, "February")) return 2;
     if (strstr(month, "March")) return 3;
     if (strstr(month, "April")) return 4;
     if (strstr(month, "May")) return 5;
     if (strstr(month, "June")) return 6;
     if (strstr(month, "July")) return 7;
     if (strstr(month, "August")) return 8;
     if (strstr(month, "September")) return 9;
     if (strstr(month, "October")) return 10;
     if (strstr(month, "November")) return 11;
     if (strstr(month, "December")) return 12;
     return 0;
 }
 
 void processDate(Show *s) {
     if (s == NULL || strcmp(s->date_added, "NaN") == 0) {
         s->has_date = 0;
         return;
     }
 
     char month[20] = {0};
     int day = 0, year = 0;
 
     if (sscanf(s->date_added, "%s %d, %d", month, &day, &year) == 3) {
         s->month = monthToInt(month);
         s->day = day;
         s->year = year;
         s->has_date = (s->month > 0) ? 1 : 0;
     } else {
         s->has_date = 0;
     }
 }
 
 int compareShows(const Show *a, const Show *b, int *comparisons) {
     (*comparisons)++;
     
     // Case 1: Both have no date
     if (!a->has_date && !b->has_date) {
         (*comparisons)++;
         return strcmp(a->title, b->title);
     }
     
     // Case 2: a has no date
     if (!a->has_date) {
         return 1; // a is greater (goes to the end)
     }
     
     // Case 3: b has no date
     if (!b->has_date) {
         return -1; // a is less (goes to the beginning)
     }
     
     // Case 4: Both have dates
     if (a->year != b->year) return a->year - b->year;
     if (a->month != b->month) return a->month - b->month;
     if (a->day != b->day) return a->day - b->day;
     
     // Dates are equal, sort by title
     (*comparisons)++;
     return strcmp(a->title, b->title);
 }
 
 void swapShows(Show **a, Show **b, int *movements) {
     Show *temp = *a;
     *a = *b;
     *b = temp;
     (*movements) += 3;
 }
 
 void quicksort(Show *arr[], int left, int right, int *comparisons, int *movements) {
     if (left < right) {
         int i = left, j = right;
         Show *pivot = arr[(left + right) / 2];
 
         while (i <= j) {
             while (compareShows(arr[i], pivot, comparisons) < 0) i++;
             while (compareShows(arr[j], pivot, comparisons) > 0) j--;
 
             if (i <= j) {
                 swapShows(&arr[i], &arr[j], movements);
                 i++; j--;
             }
         }
         
         if (left < j) quicksort(arr, left, j, comparisons, movements);
         if (i < right) quicksort(arr, i, right, comparisons, movements);
     }
 }
 
 int main() {
     FILE *file = fopen("/tmp/disneyplus.csv", "r");
     if (file == NULL) {
         printf("Error opening file.\n");
         return 1;
     }
 
     char line[LINE_SIZE];
     int i = 0;
 
     Show *shows[MAX_SHOWS];
 
     // Skip header line
     fgets(line, LINE_SIZE, file);
 
     while (fgets(line, LINE_SIZE, file) != NULL && i < MAX_SHOWS) {
         line[strcspn(line, "\r\n")] = '\0';
         shows[i] = readShow(line);
         i++;
     }
     fclose(file);
 
     Show *my_shows[MAX_SHOWS];
     int count = 0;
     char id[50];
 
     // Read IDs until finding "FIM"
     while (1) {
         if (scanf("%s", id) == EOF || strcmp(id, "FIM") == 0) {
             break;
         }
 
         for (int j = 0; j < i; j++) {
             if (strcmp(id, shows[j]->show_id) == 0) {
                 my_shows[count++] = shows[j];
                 break;
             }
         }
     }
 
     // Process dates of selected shows
     for (int j = 0; j < count; j++) {
         processDate(my_shows[j]);
     }
 
     // Sort using Quicksort
     int comparisons = 0, movements = 0;
     clock_t start = clock();
     quicksort(my_shows, 0, count - 1, &comparisons, &movements);
     clock_t end = clock();
     double time_taken = (double)(end - start) / CLOCKS_PER_SEC;
 
     // Print sorted shows
     for (int j = 0; j < count; j++) {
         printShow(my_shows[j]);
     }
 
     // Generate log file - replace 1404192 with your matricula
     FILE *log = fopen("1404192_quicksort.txt", "w");
     if (log != NULL) {
         fprintf(log, "1404192\t%d\t%d\t%.6f", comparisons, movements, time_taken);
         fclose(log);
     }
 
     // Free memory
     for (int j = 0; j < i; j++) {
         freeShow(shows[j]);
     }
 
     return 0;
 }