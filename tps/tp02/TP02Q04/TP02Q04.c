/**
 * Binary Search Implementation for Disney+ Shows
 * @author Your Name
 * @version 1.0 MM/YYYY
 */

 #include <stdio.h>
 #include <stdlib.h>
 #include <string.h>
 #include <stdbool.h>
 #include <time.h>
 
 // Structure for date representation
 typedef struct {
     int day, month, year;
 } Date;
 
 // Show structure as required in TP02
 typedef struct {
     char show_id[20];
     char type[100];
     char title[200];
     char director[200];
     char* cast[50];          // Array of cast members
     int cast_size;
     char country[100];
     Date date_added;
     int release_year;
     char rating[20];
     char duration[50];
     char* listed_in[50];     // Array of categories
     int listed_in_size;
 } Show;
 
 // Function prototypes
 void initShow(Show* show);
 void readShow(Show* show, char* id);
 void printShow(Show show);
 void sortShowsByTitle(Show shows[], int n);
 bool binarySearch(Show shows[], int n, char* title, int* comparisons);
 void freeShow(Show* show);
 char* trim(char* str);
 int compareString(const char* str1, const char* str2);
 
 /**
  * Main function
  */
 int main() {
     char id[20];
     int n = 0;
     
     // Count how many shows we'll need to read
     int maxShows = 100; // Initial capacity
     Show* shows = (Show*)malloc(maxShows * sizeof(Show));
     
     // Read show IDs until FIM
     scanf("%s", id);
     while (strcmp(id, "FIM") != 0) {
         // Increase array size if needed
         if (n >= maxShows) {
             maxShows *= 2;
             shows = (Show*)realloc(shows, maxShows * sizeof(Show));
         }
         
         // Initialize and read show data
         initShow(&shows[n]);
         readShow(&shows[n], id);
         n++;
         
         scanf("%s", id);
     }
     
     // Sort shows by title (required for binary search)
     sortShowsByTitle(shows, n);
     
     // Start binary search section
     char title[200];
     int comparisons = 0;
     clock_t start = clock();
     
     // Read titles to search until FIM
     scanf(" %[^\n]", title);
     while (strcmp(title, "FIM") != 0) {
         bool found = binarySearch(shows, n, title, &comparisons);
         printf("%s\n", found ? "SIM" : "NAO");
         
         scanf(" %[^\n]", title);
     }
     
     // Calculate execution time
     clock_t end = clock();
     double execution_time = ((double)(end - start)) / CLOCKS_PER_SEC;
     
     // Generate log file (replace 123456 with your actual enrollment number)
     FILE* log = fopen("1404192_binaria.txt", "w");
     if (log) {
         fprintf(log, "1404192\t%.6lf\t%d", execution_time, comparisons);
         fclose(log);
     }
     
     // Free allocated memory
     for (int i = 0; i < n; i++) {
         freeShow(&shows[i]);
     }
     free(shows);
     
     return 0;
 }
 
 /**
  * Initialize a Show struct
  */
 void initShow(Show* show) {
     strcpy(show->show_id, "");
     strcpy(show->type, "");
     strcpy(show->title, "");
     strcpy(show->director, "");
     show->cast_size = 0;
     strcpy(show->country, "");
     show->date_added.day = 0;
     show->date_added.month = 0;
     show->date_added.year = 0;
     show->release_year = 0;
     strcpy(show->rating, "");
     strcpy(show->duration, "");
     show->listed_in_size = 0;
 }
 
 /**
  * Read a show from CSV file based on ID
  */
 void readShow(Show* show, char* id) {
     FILE* file = fopen("/tmp/disneyplus.csv", "r");
     if (!file) {
         printf("Failed to open file\n");
         return;
     }
     
     char line[2048]; // Increased buffer size for large lines
     char* result = NULL;
     bool found = false;
     
     // Skip header line
     fgets(line, sizeof(line), file);
     
     // Search for the show with matching ID
     while (!found && (result = fgets(line, sizeof(line), file)) != NULL) {
         char buffer[2048];
         int pos = 0;
         
         // Extract show_id from the beginning of the line
         sscanf(line, "%[^,]%n", buffer, &pos);
         
         if (strcmp(buffer, id) == 0) {
             // Found the show with matching ID
             strcpy(show->show_id, buffer);
             
             // Move past the comma
             pos++;
             
             // Type
             sscanf(line + pos, "%[^,]%n", buffer, &pos);
             strcpy(show->type, buffer);
             pos++;
             
             // Title (may contain commas)
             if (line[pos] == '"') {
                 // Title is in quotes, extract until closing quote
                 pos++; // Skip opening quote
                 int start = pos;
                 while (line[pos] != '"' || line[pos+1] != ',') pos++;
                 int len = pos - start;
                 strncpy(show->title, line + start, len);
                 show->title[len] = '\0';
                 pos += 2; // Skip quote and comma
             } else {
                 // Title without quotes
                 sscanf(line + pos, "%[^,]%n", show->title, &pos);
                 pos++;
             }
             
             // Director
             sscanf(line + pos, "%[^,]%n", buffer, &pos);
             if (strlen(buffer) > 0) {
                 strcpy(show->director, buffer);
             } else {
                 strcpy(show->director, "NaN");
             }
             pos++;
             
             // Cast (handle commas within quotes)
             if (line[pos] == '"') {
                 // Cast is in quotes
                 pos++; // Skip opening quote
                 int start = pos;
                 while (line[pos] != '"' || line[pos+1] != ',') pos++;
                 
                 // Extract the entire cast string
                 char castStr[1024];
                 int len = pos - start;
                 strncpy(castStr, line + start, len);
                 castStr[len] = '\0';
                 
                 // Split by comma
                 char* token = strtok(castStr, ",");
                 while (token && show->cast_size < 50) {
                     char* trimmed = trim(token);
                     if (strlen(trimmed) > 0) {
                         show->cast[show->cast_size] = strdup(trimmed);
                         show->cast_size++;
                     }
                     token = strtok(NULL, ",");
                 }
                 
                 pos += 2; // Skip quote and comma
             } else {
                 // Single cast member or empty
                 sscanf(line + pos, "%[^,]%n", buffer, &pos);
                 if (strlen(buffer) > 0) {
                     show->cast[0] = strdup(trim(buffer));
                     show->cast_size = 1;
                 }
                 pos++;
             }
             
             // Country
             sscanf(line + pos, "%[^,]%n", buffer, &pos);
             if (strlen(buffer) > 0) {
                 strcpy(show->country, buffer);
             } else {
                 strcpy(show->country, "NaN");
             }
             pos++;
             
             // Date Added
             sscanf(line + pos, "%[^,]%n", buffer, &pos);
             if (strlen(buffer) > 0) {
                 char month[20];
                 int day, year;
                 
                 if (sscanf(buffer, "%s %d, %d", month, &day, &year) == 3) {
                     // Convert month name to number
                     const char* months[] = {"January", "February", "March", "April", "May", "June", 
                                             "July", "August", "September", "October", "November", "December"};
                     for (int i = 0; i < 12; i++) {
                         if (strstr(month, months[i]) != NULL) {
                             show->date_added.month = i + 1;
                             break;
                         }
                     }
                     show->date_added.day = day;
                     show->date_added.year = year;
                 }
             }
             pos++;
             
             // Release Year
             int year;
             sscanf(line + pos, "%d%n", &year, &pos);
             show->release_year = year;
             pos++;
             
             // Rating
             sscanf(line + pos, "%[^,]%n", buffer, &pos);
             if (strlen(buffer) > 0) {
                 strcpy(show->rating, buffer);
             } else {
                 strcpy(show->rating, "NaN");
             }
             pos++;
             
             // Duration
             sscanf(line + pos, "%[^,]%n", buffer, &pos);
             if (strlen(buffer) > 0) {
                 strcpy(show->duration, buffer);
             } else {
                 strcpy(show->duration, "NaN");
             }
             pos++;
             
             // Listed In (categories)
             if (line[pos] == '"') {
                 // Categories in quotes
                 pos++; // Skip opening quote
                 int start = pos;
                 
                 // Find the closing quote, but careful with line endings
                 while (line[pos] != '\0' && (line[pos] != '"' || (line[pos+1] != ',' && line[pos+1] != '\n' && line[pos+1] != '\0'))) {
                     pos++;
                 }
                 
                 // Extract the categories string
                 char categories[1024];
                 int len = pos - start;
                 strncpy(categories, line + start, len);
                 categories[len] = '\0';
                 
                 // Split by comma
                 char* token = strtok(categories, ",");
                 while (token && show->listed_in_size < 50) {
                     char* trimmed = trim(token);
                     if (strlen(trimmed) > 0) {
                         show->listed_in[show->listed_in_size] = strdup(trimmed);
                         show->listed_in_size++;
                     }
                     token = strtok(NULL, ",");
                 }
             } else {
                 // Single category or empty
                 sscanf(line + pos, "%[^,\n]", buffer);
                 if (strlen(buffer) > 0) {
                     show->listed_in[0] = strdup(trim(buffer));
                     show->listed_in_size = 1;
                 }
             }
             
             found = true;
         }
     }
     
     fclose(file);
 }
 
 /**
  * Print a show according to the specified format
  */
 void printShow(Show show) {
     printf("=> %s ## %s ## %s ## %s ## [", 
            show.show_id, 
            show.type, 
            show.title, 
            strcmp(show.director, "") == 0 ? "NaN" : show.director);
     
     // Print cast members
     if (show.cast_size > 0) {
         for (int i = 0; i < show.cast_size; i++) {
             printf("%s", show.cast[i]);
             if (i < show.cast_size - 1) printf(", ");
         }
     } else {
         printf("NaN");
     }
     
     printf("] ## %s ## ", strcmp(show.country, "") == 0 ? "NaN" : show.country);
     
     // Print date added
     if (show.date_added.year > 0) {
         const char* months[] = {"", "January", "February", "March", "April", "May", "June", 
                                "July", "August", "September", "October", "November", "December"};
         printf("%s %d, %d", months[show.date_added.month], show.date_added.day, show.date_added.year);
     } else {
         printf("NaN");
     }
     
     printf(" ## %d ## %s ## %s ## [", 
            show.release_year, 
            strcmp(show.rating, "") == 0 ? "NaN" : show.rating,
            strcmp(show.duration, "") == 0 ? "NaN" : show.duration);
     
     // Print categories
     if (show.listed_in_size > 0) {
         for (int i = 0; i < show.listed_in_size; i++) {
             printf("%s", show.listed_in[i]);
             if (i < show.listed_in_size - 1) printf(", ");
         }
     } else {
         printf("NaN");
     }
     
     printf("] ##\n");
 }
 
 /**
  * Sort the shows array by title using insertion sort
  */
 void sortShowsByTitle(Show shows[], int n) {
     for (int i = 1; i < n; i++) {
         Show tmp = shows[i];
         int j = i - 1;
         
         while (j >= 0 && compareString(shows[j].title, tmp.title) > 0) {
             shows[j + 1] = shows[j];
             j--;
         }
         
         shows[j + 1] = tmp;
     }
 }
 
 /**
  * Binary search for a show by title
  */
 bool binarySearch(Show shows[], int n, char* title, int* comparisons) {
     int left = 0;
     int right = n - 1;
     
     while (left <= right) {
         int mid = (left + right) / 2;
         (*comparisons)++;
         
         int cmp = compareString(shows[mid].title, title);
         
         if (cmp == 0) {
             return true;
         } else if (cmp < 0) {
             left = mid + 1;
         } else {
             right = mid - 1;
         }
     }
     
     return false;
 }
 
 /**
  * Compare two strings (case-insensitive)
  */
 int compareString(const char* str1, const char* str2) {
     char s1[200], s2[200];
     
     // Make copies to avoid modifying the originals
     strcpy(s1, str1);
     strcpy(s2, str2);
     
     // Convert to lowercase
     for (int i = 0; s1[i]; i++) {
         if (s1[i] >= 'A' && s1[i] <= 'Z') {
             s1[i] = s1[i] + ('a' - 'A');
         }
     }
     
     for (int i = 0; s2[i]; i++) {
         if (s2[i] >= 'A' && s2[i] <= 'Z') {
             s2[i] = s2[i] + ('a' - 'A');
         }
     }
     
     return strcmp(s1, s2);
 }
 
 /**
  * Free memory allocated for a show
  */
 void freeShow(Show* show) {
     // Free cast members
     for (int i = 0; i < show->cast_size; i++) {
         if (show->cast[i] != NULL) {
             free(show->cast[i]);
             show->cast[i] = NULL;
         }
     }
     
     // Free listed_in categories
     for (int i = 0; i < show->listed_in_size; i++) {
         if (show->listed_in[i] != NULL) {
             free(show->listed_in[i]);
             show->listed_in[i] = NULL;
         }
     }
     
     // Reset counts to avoid double freeing
     show->cast_size = 0;
     show->listed_in_size = 0;
 }
 
 /**
  * Trim leading and trailing whitespace
  */
 char* trim(char* str) {
     if (!str) return NULL;
     
     // Skip leading spaces
     while (*str && (*str == ' ' || *str == '\t' || *str == '\n' || *str == '\r')) {
         str++;
     }
     
     if (*str == '\0') return str;
     
     // Trim trailing spaces
     char* end = str + strlen(str) - 1;
     while (end > str && (*end == ' ' || *end == '\t' || *end == '\n' || *end == '\r')) {
         *end = '\0';
         end--;
     }
     
     return str;
 }