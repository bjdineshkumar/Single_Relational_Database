import SQLDump.SqlDump;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Group 26
 *
 * @author: Dinesh Kumar Baalajee Jothi
 * @description: Class to select from the table
 *
 * Sample Query : Select * from student;
 *                Select * from student where ID='1234';
 *                Select Name from student;
 *                Select Name from student where ID='1234';
 */

public class selectQuery {
    private String query;
    FileHandler fh;

    Logger logger = Logger.getLogger(selectQuery.class.getName());
    long startTime = System.currentTimeMillis();

    // constructor
    public selectQuery(FileHandler fh) {
        this.fh = fh;
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
    }

    void selectUserQuery(String selectQueryInput) {
        try {
            logger.info("The query is "+selectQueryInput);

            String UserInputSelectQuery = selectQueryInput;
            String whereClauseColumnName = "";
            String whereClauseColumnValue = "";


            List<String> printTheQueryColumnName = new ArrayList<>();
            List<String> printTheQueryColumnValue = new ArrayList<>();

            // Splitting the user select query to find the table
            String[] splitSelectQueryArray = UserInputSelectQuery.split(" ");

            String tableName = splitSelectQueryArray[3].replace(";","");
            String[] columnValuesToGet = splitSelectQueryArray[1].split(",");
            List<String> testArray = Arrays.asList(splitSelectQueryArray[1].split(","));


            // Reading the required table (tableFile)
            File tableFile = new File("//Users/lib-user/Desktop/DWDM" +
                "/database" +
                "-project/tables/"+tableName+".txt");
          //  File tableFile = new File("C:\\Users\\anami\\IdeaProjects" +
            //    "\\database-project\\tables\\tableone.txt");

            BufferedReader bufferRead = new BufferedReader(new FileReader(tableFile));

            String splitQuery = "";

            String tempString;

            int test = 0;
            while ((tempString = bufferRead.readLine()) != null) {

                test ++;

                splitQuery = tempString;

                String[] splitQueryArray = splitQuery.split(";");
                int tableColumnsNumber = splitQueryArray.length;

                boolean checkIfWhereConditionIsSatisfied = false;

                for (int i = 0; i < tableColumnsNumber; i++) {

                    List<String> whereClauseCheck = new ArrayList<>();

                    // check if the select query has select or not
                    if (splitSelectQueryArray.length == 6) {
                        whereClauseCheck = Arrays.asList(splitSelectQueryArray[5].replace(";","").split("="));
                        String whereClauseColumnNameCheck = whereClauseCheck.get(0);
                        String whereClauseColumnValueCheck = whereClauseCheck.get(1).replace("'", "");

                        // Value got from the tableFile
                        // Splitting the column name and the value
                        String[] seperateNameValue = splitQueryArray[i].split(":");
                        String columnName = seperateNameValue[0];
                        String columnValue = seperateNameValue[1];

                        // Checking the where condition and the value of the where condition
                        if (whereClauseColumnNameCheck.equals(columnName) &&
                                whereClauseColumnValueCheck.equals(columnValue)) {
                            checkIfWhereConditionIsSatisfied = true;
                        }
                    } else {
                        checkIfWhereConditionIsSatisfied = true;
                    }
                }

                if(checkIfWhereConditionIsSatisfied){

                //Iterating through the array to find the column and values
                for (int i = 0; i < splitQueryArray.length ; i++) {


                    // Splitting the column name and the value
                    String[] seperateNameValue = splitQueryArray[i].split(":");
                    String columnName = seperateNameValue[0];
                    String columnValue = seperateNameValue[1];


                    // Condition for the where clause
                    if (splitSelectQueryArray.length == 6) {

                        for(int iterator = 0 ; iterator < testArray.size() ; iterator++){
                            if (columnValuesToGet[iterator].matches(columnName)) {
                                printTheQueryColumnName.add(columnName);
                                printTheQueryColumnValue.add(columnValue);
                            } else if(testArray.get(iterator).equals("*")){
                                printTheQueryColumnName.add(columnName);
                                printTheQueryColumnValue.add(columnValue);
                            }
                        }

                        int columnsToGet = testArray.size();
                    } else {
                        for(int iter = 0 ; iter < testArray.size() ; iter++) {
                            if (testArray.get(iter).matches(columnName)) {
                                printTheQueryColumnName.add(columnName);
                                printTheQueryColumnValue.add(columnValue);
                            } else if(testArray.get(iter).equals("*")){
                                printTheQueryColumnName.add(columnName);
                                printTheQueryColumnValue.add(columnValue);
                            }
                        }
                    }
                }
            }
            }
            // LinkedHashMap to save the column name and value to print
            Map<String,String> Columns = new LinkedHashMap<>();

            //Condition to check if it is * or specific column name
            if(columnValuesToGet[0].equals("*")) {
                for (int i = 0; i < printTheQueryColumnName.size(); i++) {
                    Columns.put(printTheQueryColumnName.get(i), "|");
                }
            } else {
                for (int i = 0; i < columnValuesToGet.length; i++) {
                    Columns.put(columnValuesToGet[i], "|");
                }
            }

            //Printing the column name and values to the console

            if(!printTheQueryColumnName.isEmpty()) {
                System.out.println("+------------------------+--------------------+---------------------------+");
                for (Map.Entry<String,String> rowColumnValue : Columns.entrySet()) {
                    String columnName = (String)rowColumnValue.getKey();
                    String columnValue = (String)rowColumnValue.getValue();

                    System.out.print(columnValue + columnName+"                      ");

                }
                System.out.println("\n+------------------------+--------------------+---------------------------+");
                int count = 1;
                for (int i = 0; i < printTheQueryColumnName.size(); i++) {

                    if(count % Columns.size() == 0) {
                        System.out.print("|" + printTheQueryColumnValue.get(i)+"\t\t                 \n");
                        count++;
                    } else if(count % Columns.size() != 0){
                        System.out.print("|" + printTheQueryColumnValue.get(i)+"\t                 ");
                        count++;
                    }
                }
            } else {
                System.out.println("Sorry no data matches the database");
                logger.info("Sorry no data matches the database");
            }
            long endTime = System.currentTimeMillis();
            long execution = endTime-startTime;
            logger.info(("The total execution time for select query is :" + execution +
                " milliseconds"));

        } catch(Exception e){
            System.out.print("Please enter a valid query");
            logger.info("There is an error in select query");

        }
    }
}
