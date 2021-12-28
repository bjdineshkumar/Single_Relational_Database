import SQLDump.SqlDump;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Group 26
 *
 * @author: Dinesh Kumar Baalajee Jothi
 * @description: Class to update the table
 *
 * Sample Query : Update student set Name='Bobby' where DOB='10/12/1998';
 */

public class updateQuery {
    Logger logger = Logger.getLogger(updateQuery.class.getName());
    long startTime = System.currentTimeMillis();


    private int rowsAffected = 0;

    updateQuery(FileHandler fh) {
        // This block configure the logger with handler and formatter
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
    }

    void updateUserQuery(String updateQuery) {
        logger.info("The query is "+updateQuery);

        //Getting the user input query
        String userUpdateQuery = updateQuery;

        // Splitting the user select query to find the table
        String[] splitUpdateQueryByUser = userUpdateQuery.split(" ");
        String tableName = splitUpdateQueryByUser[1];
        String[] ColumnNameValueToChange = splitUpdateQueryByUser[3].split(",");
        String[] whereConditionNameValue = splitUpdateQueryByUser[5].split("=");

        Map<String, String> whereColumn = new HashMap<>();
        whereColumn.put(whereConditionNameValue[0], whereConditionNameValue[1].replace("'", "").replace(";", ""));

        List<String> columnToChange = new ArrayList<>();

        for (int i = 0; i < ColumnNameValueToChange.length; i++) {
            columnToChange.add(ColumnNameValueToChange[i]);
        }
        updateTheTable(whereColumn, columnToChange, tableName);
    }

    void updateTheTable(Map<String, String> whereClause, List<String> columnToChange, String tableName) {

        try {
            // Reading the file the table is stored and creating a temp file
            File fileTable = new File("//Users/lib-user/Desktop/DWDM" +
                "/database-project/tables/"+tableName+".txt");
            File tempFile = new File
             ("//Users/lib-user/Desktop/DWDM/database-project/tables/temp.txt");


            String whereColumnName = "";
            String whereColumnValue = "";


            // Getting the where condition column name and column value
            for (Map.Entry<String, String> entry : whereClause.entrySet()) {
                whereColumnName = entry.getKey();
                whereColumnValue = entry.getValue();
            }

            BufferedReader readTable = new BufferedReader(new FileReader(fileTable));
            BufferedWriter writeTable = new BufferedWriter(new FileWriter(tempFile, true));

            String row;

            // Reading each line in the file (Table)
            while ((row = readTable.readLine()) != null) {

                String[] rowFromFile = row.split(";");

                String updatedRowToWrite = "";

                // Looping through each attributes in the row
                for (int column = 0; column < rowFromFile.length; column++) {
                    String[] splitColumnNameAndValue = rowFromFile[column].split(":");
                    String ColumnName = splitColumnNameAndValue[0];
                    String ColumnValue = splitColumnNameAndValue[1];

                    //Checking if the where condition is met to do the update
                    if (ColumnName.equals(whereColumnName) && ColumnValue.equals(String.valueOf(whereColumnValue))) {

                        //getting the column to be updates
                        for (int i = 0; i < columnToChange.size(); i++) {

                            String[] currentColumnToChange = columnToChange.get(i).split("=");
                            String columnNameToChange = currentColumnToChange[0];
                            String columnValueToChange = currentColumnToChange[1].replace("'", "");

                            //Looping through the column values of the row
                            for (int columns = 0; columns < rowFromFile.length; columns++) {
                                String[] tempRow = rowFromFile[columns].split(":");
                                String tempColumnName = tempRow[0];
                                String tempColumnValue = tempRow[1];
                                //Checking if the Column needs the update in the value
                                if (tempColumnName.equals(columnNameToChange)) {
                                    rowFromFile[columns] = columnNameToChange + ":" + columnValueToChange;
                                    rowsAffected++;
                                }
                            }
                        }
                    }
                    logger.info("The number of rows affected while updated " +
                        "is " +rowsAffected+1);
                    System.out.println(rowFromFile[column]);
                }
                for (int rowWrite = 0; rowWrite < rowFromFile.length; rowWrite++) {
                    if (rowWrite != rowFromFile.length - 1) {
                        updatedRowToWrite += rowFromFile[rowWrite] + ";";
                    } else if (rowWrite == rowFromFile.length - 1) {
                        updatedRowToWrite += rowFromFile[rowWrite] + ";\n";
                    }
                }
                writeTable.write(updatedRowToWrite);
                long endTime = System.currentTimeMillis();
                long execution = endTime-startTime;
                logger.info(("The total execution time for update query is :" + execution +
                    " milliseconds"));
            }

            writeTable.close();
            readTable.close();

            //Deleting the file
            fileTable.delete();

            // Now we will rename the temp file to the table name
            File newName = new File("//Users/lib-user/Desktop/DWDM/database-project/tables/"+tableName+".txt");
            tempFile.renameTo(newName);

        } catch (Exception e) {
            System.out.print("Please enter a valid query");
        }
    }
}

