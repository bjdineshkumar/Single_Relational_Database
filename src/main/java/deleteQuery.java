import java.io.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Group 26
 *
 * @author: Dinesh Kumar Baalajee Jothi
 * @description: Class to delete from table
 *
 * Sample Query : delete from student;
 *                delete from student where ID='1234';
 */

public class deleteQuery {
    Logger logger = Logger.getLogger(deleteQuery.class.getName());
    long startTime = System.currentTimeMillis();

    FileHandler fh;
    private int rowsAffected = 0 ;

    deleteQuery(FileHandler fh) {
        // This block configure the logger with handler and formatter
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
    }

    void deleteUserQuery(String deleteQuery){
         logger.info("The query is "+deleteQuery);
        try{

            //Getting the user input query
            String userDeleteQuery = deleteQuery;

            // Splitting the user select query to find the table
            String[] splitDeleteQuery = userDeleteQuery.split(" ");


            String tableName = splitDeleteQuery[2].replace(";",""); // The table name is got from the delete query from the user

            boolean match = false;


            File fileTable = new File("/Users/lib-user/Desktop/DWDM/database-project/tables/"+tableName+".txt");
            File tempFile = new File("/Users/lib-user/Desktop/DWDM/database-project/tables/temp.txt");


            BufferedReader readTable = new BufferedReader(new FileReader(fileTable));
            BufferedWriter writeTable = new BufferedWriter(new FileWriter(tempFile));

            String[] dataToBeDeleted = new String[0];
            // Condition to check the type of delete
            if(splitDeleteQuery.length > 3){
                dataToBeDeleted = splitDeleteQuery[4].split("=");


                //Getting the column name and the column value
                String columnNameToDelete = dataToBeDeleted[0];
                String columnValueToDelete = dataToBeDeleted[1].replace("'","").replace(";","");

                String row;

                // Reading through each line of the file (Table)
                while((row = readTable.readLine()) != null) {

                    String queryToAdd = "";

                    String[] trimmedLine = row.trim().split(";");

                    // Iterating through the row column and values to find if this is the row to be deleted
                    for (int i = 0; i < trimmedLine.length; i++) {

                        String[] rowLine = trimmedLine[i].split(":");
                        String colName = rowLine[0];
                        String colValue = rowLine[1];

                        // If condition to check whether the row is correct by using column name and value
                        if (colName.equals(columnNameToDelete) && colValue.equals(columnValueToDelete)) {
                            match = true;
                            rowsAffected++;
                            break;
                        } else {
                            match = false;
                            break;
                        }
                    }
                    if(!match){
                        writeTable.write(row+"\n");
                        System.out.print(row+"\n");
                    }

                }

                System.out.println("Test");

                //Deleting the file
                fileTable.delete();

                // Renaming the file
                File newFileName = new File("/Users/lib-user/Desktop/DWDM/database-project/tables/"+tableName+".txt");
                tempFile.renameTo(newFileName);

                writeTable.close();
                readTable.close();

            } else if(splitDeleteQuery.length == 3){

                String rowRead;
                try{
                    while((rowRead = readTable.readLine()) != null) {
                        rowsAffected++;
                    }
                } catch (Exception e){
                    System.out.print("Error");
                }

                // If we want to delete the records
                // We will delete the file and the create another file
                fileTable.delete();

                File newFile = new File("/Users/lib-user/Desktop/DWDM/database-project/tables/"+tableName+".txt");
                tempFile.renameTo(newFile);
            }
            logger.info("The number of rows affected is "+rowsAffected+1);
            long endTime = System.currentTimeMillis();
            long execution = endTime-startTime;
            logger.info(("The total execution time for insertion query is :" + execution +
                " milliseconds"));

            writeTable.close();
            readTable.close();


        } catch(Exception e){
            System.out.println("Please eneter a valid query");
            logger.info("There is an error in deleting");
        }
    }
}
