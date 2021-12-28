import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ERD {

    Map<String, ArrayList<String>> erdValues = new HashMap<>();
    Logger logger = Logger.getLogger(ERD.class.getName());
   public ERD(FileHandler fh){
       logger.addHandler(fh);
       SimpleFormatter formatter = new SimpleFormatter();
       fh.setFormatter(formatter);
   }

    public void createERD(){
        File dir = new File("tables/metadata");
        for (File file : dir.listFiles()) {
            ArrayList<String> relations = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String sCurrentLine;
                while ((sCurrentLine = br.readLine()) != null) {
                    if(sCurrentLine.contains("primary")){
                        int attribute = sCurrentLine.indexOf("=");
                        String attributeName = sCurrentLine.substring(0,attribute);
                        for(File secondFiles : dir.listFiles()){
                            try (BufferedReader brk = new BufferedReader(new FileReader(secondFiles))) {
                                String cLine;
                                while ((cLine = brk.readLine()) != null) {
                                    if(cLine.contains("foreign")){
                                        String secondAttributeName = cLine.substring(0,attribute);
                                        if(attributeName.contains(secondAttributeName)){
                                            int k = secondFiles.getName().indexOf(".");
                                            String tableName = secondFiles.getName().substring(0,k);
                                            relations.add(tableName);
                                        }
                                    }
                                }
                                } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if(relations.size()>=1){
                            int k = file.getName().indexOf(".");
                            String tableName = file.getName().substring(0,k);
                            erdValues.put(tableName + " " + attributeName, relations);
                        }

                        }

                    }
                logger.info("ERD Created");


        } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Iterator it = erdValues.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

                System.out.println("Table " + pair.getKey() + "'s primary key is a foreign key in tables " + pair.getValue());
                logger.info("Table " + pair.getKey() + "'s primary key is a foreign key in tables " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
    }
}
