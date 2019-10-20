import java.sql.*;

public class SqliteDB {
    public Connection co;
    public SqliteDB()
    {
        try {
            Class.forName("org.sqlite.JDBC");
            co = DriverManager.getConnection("jdbc:sqlite:db/testDB.db");
            System.out.println("Connected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String ... args)
    {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection co = DriverManager.getConnection("jdbc:sqlite:db/testDB.db");
            System.out.println("Connected");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}