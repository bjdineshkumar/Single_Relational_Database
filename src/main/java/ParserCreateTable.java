import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ParserCreateTable {
    private String table;
    Login login = new Login();
    Logger logger = Logger.getLogger(ParserCreateTable.class.getName());
    FileHandler fh;

    public ParserCreateTable(FileHandler fh) {
        this.fh = fh;
        // This block configure the logger with handler and formatter
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);

    }

    public void createUserQuery(String query) {
        logger.info("The query is "+query);
        LinkedHashMap<String, String> columnAndConditions =
                identifyTheColumns(query);
        if (!columnAndConditions.isEmpty()) {

            // Ignite Execution Engine
            QueryEngineCreateTable
                    createTableQueryEngine = new QueryEngineCreateTable();
            try {
                createTableQueryEngine.runQuery(columnAndConditions, table);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        login.mainMenu();




    }

    private LinkedHashMap<String, String> identifyTheColumns(String query) {

        LinkedHashMap<String, String> keyColumnMap = new LinkedHashMap<>();

        String[] ColumnsList = query.split(",");

        // First table and column
        try {
            String[] tmpArrSplitsOne = ColumnsList[0].split(" ");

            if (tmpArrSplitsOne.length > 1) {
                table = tmpArrSplitsOne[2];

            } else {
                throw new IndexOutOfBoundsException("Incorrect index");
            }

            // remove the ( brace from the table name if exists
            if (table.contains("(")) {
                String[] tNameArrList = (tmpArrSplitsOne[2].split("\\("));
                table = tNameArrList[0].trim();
            }
        } catch (Exception e) {
            System.out.println("Invalid syntax");
        }


        // Get column names and their data types
        // Get the 1st column name and data types
        String[] tmpArrSplitsTwo = ColumnsList[0].split("\\(");
        String column1 = tmpArrSplitsTwo[1].trim();
        String[] tmpArrSplitsThree = column1.split(" ");
        StringBuilder tempThreeListBuildString = new StringBuilder();

        tempThreeListBuildString.append(tmpArrSplitsThree[1])
                .append(" ");

        try {
            if (tmpArrSplitsThree.length > 2) {
                int j = 2;
                while (j < tmpArrSplitsThree.length) {
                    tempThreeListBuildString.append(tmpArrSplitsThree[j])
                            .append(" ");
                    j++;
                }
            }
        } catch (Exception e) {
          System.out.println("Failed to parse");
        }


        // Add the 1st column and type
        keyColumnMap
                .put(tmpArrSplitsThree[0], tempThreeListBuildString.toString());


        // Create a list to add the constraints like primary key and foreign key
        List<String> ColumnConstraints = new ArrayList<>();

        // Now loop and add other columns & data types
        for (int i = 1; i < ColumnsList.length; i++) {

            // Check for PRIMARY & FOREIGN keys!
            if (ColumnsList[i].trim().split(" ")[0]
                    .equalsIgnoreCase("primary")) {
                // Trim before adding
                ColumnConstraints.add(ColumnsList[i].trim());
                continue;
            } else if (ColumnsList[i].trim().split(" ")[0]
                    .equalsIgnoreCase("foreign")) {
                ColumnConstraints.add(ColumnsList[i].trim());
                continue;
            }

            String[] tmpArrSplitsFour = ColumnsList[i].trim().split(" ");
            StringBuilder buildString = new StringBuilder();
            buildString.append(tmpArrSplitsFour[1].trim()).append(" ");
            if (tmpArrSplitsFour.length > 2) {
                int j = 2; // Ignore the 0th element
                while (j < tmpArrSplitsFour.length) {
                    buildString.append(tmpArrSplitsFour[j]).append(" ");
                    j++;
                }
            }

            //Finally Make sure to remove any unwanted chars
            String key = tmpArrSplitsFour[0].trim();
            String keyValue =
                    buildString.toString().replace(')', ' ').trim();
            keyValue = keyValue.replace('(', ' ').trim();
            keyValue = keyValue.replace(';', ' ').trim();

            keyColumnMap.put(key, keyValue);
        }
        return keyColumnMap;
    }
}
