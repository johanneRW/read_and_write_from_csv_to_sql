package company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class SQLWriter {

    private final File DML_FILE = new File("data/dml.sql");
    private final File DDL_FILE = new File("data/ddl.sql");

    private Scanner scanner;
    private String[] columnNames;

    public SQLWriter(String csvFileName) {
        try {
            File csvFile = new File(csvFileName);
            scanner = new Scanner(csvFile);
            columnNames = getColumnNames();
        } catch (FileNotFoundException e) {
            System.out.println("Can't read from file");
        }
    }

    private String[] getColumnNames() {
        String header = scanner.nextLine();
        String[] lineAsArray = header.split(";");
        return lineAsArray;
    }

    public void createTable(String tableName) {
        try {
            PrintStream ps = new PrintStream(new FileOutputStream(DDL_FILE, false));
            StringBuilder sb = new StringBuilder();

            sb.append("CREATE TABLE " + tableName + " (\n");
            for (int i = 0; i < columnNames.length; i++) {
                String columnName = columnNames[i];
                if (i == columnNames.length - 1) {
                    sb.append(columnName + " varchar(255) \n );");
                } else {
                    sb.append(columnName + " varchar(255), \n");
                }
            }
            ps.print(sb);
        } catch (FileNotFoundException e) {
            System.out.println("Can't write to file");
        }
    }

    public void writeToTable(String tableName) {
        writeInsertStatement(tableName);
        writeTableContent();
    }

    private void writeInsertStatement(String tableName) {
        try {
            StringBuilder sb = new StringBuilder();
            PrintStream ps = new PrintStream(new FileOutputStream(DML_FILE, false));

            sb.append("INSERT INTO " + tableName + " (");

            for (int i = 0; i < columnNames.length; i++) {
                String columnName = columnNames[i];

                if (i == columnNames.length - 1) {
                    sb.append(columnName + ")\n");
                } else {
                    sb.append(columnName + ",");
                }
            }

            ps.print(sb);
        } catch (FileNotFoundException e) {
            System.out.println("Can't write to file");
        }
    }


    private void writeTableContent() {
        try {
            // Continue writing to DML file, where "writeInsertStatement" left off
            PrintStream ps = new PrintStream(new FileOutputStream(DML_FILE, true));
            StringBuilder sb = new StringBuilder();

            // Skip header line in CSV file
            scanner.nextLine();

            sb.append("VALUES \n");
            while (scanner.hasNextLine()) {
                String values = scanner.nextLine();
                String[] valuesAsArray = values.split(";");
                sb.append("(");
                for (int i = 0; i < valuesAsArray.length; i++) {
                    String tableInfo = valuesAsArray[i];
                    tableInfo = tableInfo.replace("'", "\\'");
                    if (i == valuesAsArray.length - 1) {
                        sb.append("'" + tableInfo + "'),\n");
                    } else {
                        sb.append("'" + tableInfo + "',");
                    }
                }
            }
            int indexOfLastComma = sb.lastIndexOf(",");
            sb.replace(indexOfLastComma, indexOfLastComma + 1, ";");
            ps.print(sb);
        } catch (FileNotFoundException e) {
            System.out.println("Can't write to file");
        }

    }
}