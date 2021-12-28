package SQLDump;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

public class SqlDump {

  Logger logger = Logger.getLogger(SqlDump.class.getName());
  private String path=null;
  private String path2=null;
  private String table = null;
  long startTime = System.currentTimeMillis();
  HashMap<String, String> hmap = new HashMap<>();

  public SqlDump(String path, String path2, String table, FileHandler fh) {
    this.path = path;
    this.path2=path2;
    this.table = table;
    // This block configure the logger with handler and formatter
    logger.addHandler(fh);
    SimpleFormatter formatter = new SimpleFormatter();
    fh.setFormatter(formatter);
  }

  public void getValuesFromMetadata() {
    try {

     File myObj = new File(path + "\\" + table + ".txt");
      boolean exists = myObj.exists();
      if (exists == false) {
        logger.info("The table was not found in the  specified path " + table);
        System.out.println("This table does not exist in specified path " +
            table);
        System.exit(1);
      }
      Scanner myReader = new Scanner(myObj);
      for (int i = 0; i < 1; i++) {
        System.out.println("Create Table "+ table + " (");
        while(myReader.hasNextLine()){
          String[] x=myReader.nextLine().split("=");
          String first = x[0];
          String second = x[1].replace("|","");
          if(myReader.hasNextLine()==false) {
            System.out.println(first + " " + second);
          }
          else{
            System.out.println(first+" "+second+",");
          }
        }
        System.out.println(");");
        insertionForDump(path2,table);
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.out.println("There is no file with the table name you mention in" +
          " the database");
    }
    long endTime = System.currentTimeMillis();
    long timeOfExecution = endTime - startTime;
    System.out.println("Execution time in milliseconds:" + timeOfExecution);
    logger.info("SQL dump creation for " + table + " in " + path);
    logger.info(("The total execution time for SQL dump creation is :" + timeOfExecution +
        " milliseconds"));
  }

  public void insertionForDump(String path2,String table) throws FileNotFoundException {
    File myObj = new File(path2 + "\\" + table + ".txt");
    boolean exists = myObj.exists();
    if (exists == false) {
      logger.info("The table was not found in the  specified path " + table);
      System.out.println("This table does not exist in specified path " +
          table);
      System.exit(1);
    }
    Scanner myReader = new Scanner(myObj);

    while(myReader.hasNextLine()){
          /*ID:23;name:anamika;
      ID:276;name:anamikaahmed;
      ID:645;name:anamikaahmedana;*/

      // first index of getlines is ID:23; second index of getlines
      // is name:anamika
      String[] getLines = myReader.nextLine().split(";");
      System.out.print("insert into "+table+" values(");

      for(int i=0; i<getLines.length; i++){
        // first index of entity is ID, second index of entity is 23
        String[] entity = getLines[i].split(":");
        //first index
          if(i==getLines.length-1) {
            System.out.print(entity[1]+");");

          }
          else{
            System.out.print(entity[1] + " ,");

          }
      }
      System.out.println();

    }
  }
}