import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Query {

    FileHandler fh;
    Logger logger = Logger.getLogger(Query.class.getName());
    public Scanner sc = new Scanner(System.in);
    String createExp = "(create).(table).([a-zA-Z_]+)(.?)\\(.*";

    public Query(FileHandler fh){
        this.fh = fh;
        // This block configure the logger with handler and formatter
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
    }
    public void queryMenu(Login login){

        System.out.println("Enter a SQL Query ");
        String input = sc.nextLine();
        parser(input,login);
    }

    private void parser(String query,Login login) {

        //Login login = new Login();

        if (query.startsWith("create table") && query.matches(createExp)) {
            new ParserCreateTable(fh).createUserQuery(query);
            login.mainMenu();
        } else if (query.startsWith("select")) {
            new selectQuery(fh).selectUserQuery(query);
            login.mainMenu();
        } else if (query.startsWith("insert into")) {
            new InsertQuery(fh).insertUserQuery(query);
            login.mainMenu();
        } else if (query.startsWith("update")) {
            new updateQuery(fh).updateUserQuery(query);
            login.mainMenu();
        } else if (query.startsWith("delete")) {
            new deleteQuery(fh).deleteUserQuery(query);
            login.mainMenu();
        } else if (query.startsWith("drop")) {
            new dropTable(fh).userDropTable(query);
            login.mainMenu();
        }
    }

}
