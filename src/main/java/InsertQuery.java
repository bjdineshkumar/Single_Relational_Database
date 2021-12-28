import java.io.*;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Group 26
 *
 * @author: Dinesh Kumar Baalajee Jothi
 * @description: Class to insert the data into the table
 *
 * Sample Query : insert into student (ID,Name,Age) values (21344,Dinesh,20);
 */

public class InsertQuery {

    private String tableName = ""; // Store the table name
    private Map<String,String> columnNameValuePair= new LinkedHashMap<>(); //Store the column name value
    Logger logger = Logger.getLogger(InsertQuery.class.getName());
    long startTime = System.currentTimeMillis();


    InsertQuery(FileHandler fh){
        // This block configure the logger with handler and formatter
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
    }
    /**
     * Method to get the user query and get the table name, column name and it values
     * insert into table_name (column1, column2, column3, ...) VALUES (value1, value2, value3, ...);
     */
    void insertUserQuery(String Query){
        try {
            logger.info("The SQL Query is " + Query);

            Query = Query.replace(";", "");
            String userQuery[] = Query.split(" ");
            tableName = userQuery[2];
            String tempColumnNames = userQuery[3].replace("(", "");
            tempColumnNames = tempColumnNames.replace(")", "");
            String[] columnNames = tempColumnNames.split(",");

            String tempColumnValues = userQuery[5].replace("(", "");
            tempColumnValues = tempColumnValues.replace(")", "");
            String[] columnValues = tempColumnValues.split(",");

            for (int i = 0; i < columnValues.length; i++) {

                columnNameValuePair.put(columnNames[i], columnValues[i]); //The column name and value to be inserted is added to the map
            }
            writeToFile();
            long endTime = System.currentTimeMillis();
            long execution = endTime - startTime;
            logger.info(("The total execution time for insertion query is :" + execution +
                " milliseconds"));
        }
        catch(Exception e){
            logger.info("Please write the query properly, returning you to " +
                "main menu");
        }
    }

    /**
     * Method to write to the text file the column name and the column values
     */
    public void writeToFile(){

        try {

          //  File tableFile = new File("//Users/lib-user/Desktop/DWDM" +
          //      "/database-project/tables/"+tableName+".txt");
            File tableFile = new File("C:\\Users\\anami\\IdeaProjects\\database-project\\tables\\tableone.txt");
            if(!tableFile.exists()){
                System.out.print("Table does not exist");
                logger.info("The table does not exist");
            } else {

                //Creating a reader for the file
                BufferedReader bufferReaderTable = new BufferedReader(new FileReader(tableFile));

                if (!checkIfDataExist(bufferReaderTable)) {

                    //Creating a writer for the file to append the data to the table
                    BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(tableFile, true));

                    Iterator<Map.Entry<String, String>> iterator = columnNameValuePair.entrySet().iterator();

                    while (iterator.hasNext()) {
                        Map.Entry<String, String> columnNameValuePair = iterator.next();

                        bufferWriter.write(columnNameValuePair.getKey() + ":" + columnNameValuePair.getValue() + ";");

                        // Condition to check if it is the end of the statement to append new line
                        if (!iterator.hasNext()) {
                            bufferWriter.append("\n");
                        }
                    }

                    bufferWriter.close();
                } else {
                    System.out.println("The table values already exists ");
                    logger.info("The table values already exists");
                }
            }
        }  catch(FileNotFoundException fileException) {
            System.out.print("Table does not exist");
            logger.info("Table does not exist");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkIfDataExist(BufferedReader readRows){
        try {
            String line = "";
            String rowExist = "";
            Iterator<Map.Entry<String, String>> iterator = columnNameValuePair.entrySet().iterator();

            // Iterating through the each line in file and appending it to rowExist
            while (iterator.hasNext()) {

                Map.Entry<String, String> columnNameValuePair = iterator.next();

                rowExist += columnNameValuePair.getKey() + ":" + columnNameValuePair.getValue() + ";";

            }

            // While loop to check if the row matches wih the row to be inserted
            while ((line = readRows.readLine()) != null) {
                if(line.equals(rowExist)){
                    return true;
                }
            }

        } catch (Exception e) {
            System.out.print("Error");
            logger.info("There is an error in inserting");
        }
        return false;
    }
}