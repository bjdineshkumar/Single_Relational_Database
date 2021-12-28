import SQLDump.SqlDump;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.logging.SimpleFormatter;

public class Login {

    private String userName;
    FileHandler fh;
    Logger logger = Logger.getLogger(Login.class.getName());
    public Scanner sc = new Scanner(System.in);
    private String password;
    private String totalPath = "C:\\Users\\anami\\IdeaProjects\\database-project\\src\\main\\java\\Loggers\\LoggerText";

    public Login(){
        try {
            // This block configure the logger with handler and formatter
            fh = new FileHandler(totalPath);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public void loginMenu(){

        System.out.println("Enter username:");
        userName = sc.next();
        System.out.println("Enter password");
        password = sc.next();
        try {
            String encryptedPass = encryptToSha256(password);
            loginDatabase(userName,encryptedPass);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void loginDatabase(String userName, String password) throws NoSuchAlgorithmException {
        boolean match = false;
        String color = "";
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt")))
        {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null)
            {
                int name =  sCurrentLine.indexOf(",");
                String s = sCurrentLine.trim();
                int securityAnswer = sCurrentLine.indexOf("-");

                String user = s.substring(0,name);
                String pass = s.substring(name+1,securityAnswer);
                if(user.equals(userName) && pass.equals(password)){
                    color = s.substring(securityAnswer+1);
                    match = true;
                    break;
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if(match){
            while(true){
                System.out.println("Enter your favourite color");
                String a = sc.next();
                if(a.equals(color)){
                    System.out.println("Successfully logged in");
                    logger.info("Successfully logged in");
                    mainMenu();
                    break;
                } else{
                    System.out.println("Incorrect security question, try again");
                    logger.info("Incorrect security question, try again");
                }
            }
        } else {
            System.out.println("Incorrect login, try again");
            logger.info("Incorrect login, try again");
            loginMenu();
        }
    }

    public void mainMenu(){
        Query query = new Query(fh);
        ERD erd = new ERD(fh);
        System.out.println("\nMain Menu");
        System.out.println("=============================");
        System.out.println("Select action to perform");
        System.out.println("1. Enter an SQL query");
        System.out.println("2. Create an ERD");
        System.out.println("3. Generate SQL Dump");
        System.out.println("4. Exit Database");
        int input = sc.nextInt();
        logger.info("The entered option is "+input);
        switch (input){
            case 1:
                query.queryMenu(this);
                break;
            case 2:
                erd.createERD();
            case 3:
                //SQL Dump
                
                   String path1="C:\\Users\\anami\\IdeaProjects\\database" +
                    "-project\\tables\\metadata";
                String path2="C:\\Users\\anami\\IdeaProjects\\database" +
                    "-project\\tables";
                String table = "tableone";

                //SQL Dump
                SqlDump sql = new SqlDump(path1,path2,table, fh);
                sql.getValuesFromMetadata();
                mainMenu();
            case 4:
                String directory = "C:\\Users\\anami\\IdeaProjects\\database" +
                    "-project\\src\\main\\java\\Loggers";
              //  cleanDirectory(new File(directory));
                return;
        }


    }

    void cleanDirectory(File dir) {
        for (File file : dir.listFiles()) {
            //System.out.println("The file name is "+file.getName());
            if (file.getName().equals("LoggerText")) {
                //do nothing
            } else {
                System.out.println("The file deleted is "+file.getName());
                file.delete();
            }

        }
    }
    public  String encryptToSha256(final String base){
        try{
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(base.getBytes("UTF-8"));
            final StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                final String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(Exception ex){
            System.out.println("Couldn't create SHA256 Hash");
            logger.info("Could not create SHA256 Hash");
            System.exit(0);
            return "" ;
        }
    }


    public static void main (String[] args) throws NoSuchAlgorithmException {
        Login login = new Login();

        login.loginMenu();


    }
}
