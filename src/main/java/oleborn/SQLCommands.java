package oleborn;

public class SQLCommands {
    public static String createTable(){
        return "CREATE TABLE users (" +
                "id SERIAL PRIMARY KEY, " +
                "name VARCHAR (30) NOT NULL," +
                "salary DECIMAL (10, 2) NOT NULL)";
    }
    public static String insertUser(String name, int salary){
        return "INSERT INTO users (name, salary) VALUES ('%s', '%d')".formatted(name, salary);
    }
    public static String returnAllUsers(){
        return "SELECT * FROM users";
    }
    public static String returnUserByID(int id){
        return "SELECT * FROM users WHERE id = '%d'".formatted(id);
    }
    public static String returnSalaryByID(int id){
        return "SELECT salary FROM users WHERE id = '%d'".formatted(id);
    }
    public static String updateSalary(int id, int newSalary){
        return "UPDATE users SET salary = '%d' WHERE id = '%d'".formatted(newSalary, id);
    }
    public static String deleteUser(int id){
        return "DELETE FROM users WHERE id = '%d'".formatted(id);
    }
    public static String deleteTable(String name){
        return "DROP TABLE %s".formatted(name);
    }

}
