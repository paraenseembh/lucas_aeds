import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Principal {
    
    
    public static void main (String[]args) throws FileNotFoundException{
        Show[] arrayShows = new Show[1368];
    
        //Ler arquivo csv
        //e adicionar o show
        //de cada linha no array

        File file = new File("disneyplus.csv");
        Scanner scanner = new Scanner(file); 
        for(int i = 0 ; i < 1368; i++){
            System.out.println(scanner.nextLine());
            arrayShows[i] = Show.ler(scanner.nextLine()) ;
        }
        
        /*da pra fazer usando
         * while(scanner.hasnextLine){
         * System.out.println(scanner.nextLine());
         * }
         */
        //usar normalmente

        scanner.close();
    }
}

class Show{
    //atributos
    private String showId; 
    private String type;
    private String title; 
    private String director;
    private String cast; //REMONTAR PARA ARRAY
    private String country;
    private String dateAdded; 
    private String releaseYear;
    private String rating; // AQUI EH INT
    private String duration; 
    private String listed_in;
    
    


    public static Show ler(String linha){
        Show novo_show = new Show();

        return novo_show;
    }

    public void imprimir(){
        System.out.println();
    }

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getListed_in() {
        return listed_in;
    }

    public void setListed_in(String listed_in) {
        this.listed_in = listed_in;
    }
   


}




