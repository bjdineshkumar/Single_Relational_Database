import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Group 26
 *
 * @author: Dinesh Kumar Baalajee Jothi
 * @description: Class to drop the table
 *
 * Sample Query : drop table student;
 */

public class dropTable {
    Logger logger = Logger.getLogger(updateQuery.class.getName());
    long startTime = System.currentTimeMillis();


    dropTable(FileHandler fh) {
        // This block configure the logger with handler and formatter
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
    }

    public void userDropTable(String dropQuery){
        logger.info("The query is '"+dropQuery+"'");

        String userDropQuery = dropQuery;
        String[] splitDropQuery = userDropQuery.split(" ");
        String tableName = splitDropQuery[2].replace(";","");

        File readFile = new File("/Users/lib-user/IdeaProjects/dropTable/"+tableName+".txt");
        try {
            if (readFile.delete()) {
                System.out.println("The table " + tableName + " dropped");
                logger.info("The table "+tableName+" has been dropped");
            }
        }
        catch(Exception e){
            logger.info("There is an error in dropping the table");
        }
        long endTime = System.currentTimeMillis();
        long execution = endTime-startTime;
        logger.info(("The total execution time for drop table query is :" + execution +
            " milliseconds"));

    }
}
