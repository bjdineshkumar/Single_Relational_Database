import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class QueryEngineCreateTable {

    FileHandler fileHandler = new FileHandler();

    public QueryEngineCreateTable() {

    }

    public void runQuery(LinkedHashMap<String, String> columnAndConditions,
                         String table) throws IOException {

            //Create the folders and files
            String folderPath = "tables/metadata";
            String columnFilePath = folderPath + File.separator + table + ".txt";
            String filePath = "tables";
            String tableFilePath = filePath + File.separator + table + ".txt";



        // Create files for database
        createColumnFile(columnFilePath, columnAndConditions, table);
        fileHandler.createFileIfNotExists(tableFilePath);



    }

    private void createColumnFile(String ddcFilePath,
                                  HashMap<String, String> columnAndConditions,
                                  String tableName) {

        String message = "";
        for (Map.Entry<String, String> entry : columnAndConditions.entrySet()) {
            message = entry.getKey() + "=" +
                    entry.getValue().replaceAll(" ", "|");
            fileHandler.writeToFile(ddcFilePath, message);
        }

        System.out.println("Column file succesfully created");


    }
}
