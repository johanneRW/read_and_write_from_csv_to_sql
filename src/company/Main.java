package company;

public class Main {

    public static void main(String[] args) {

        String filePath = "data/imdb-data.csv";
        SQLWriter sqlWriter = new SQLWriter(filePath);
        sqlWriter.createTable("imdb");
        sqlWriter.writeToTable("imdb");

    }
}

