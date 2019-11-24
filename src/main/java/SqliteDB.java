import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SqliteDB {
    public Connection co = null;
    public SqliteDB()
    {
        try {
            Class.forName("org.sqlite.JDBC");
            co = DriverManager.getConnection("jdbc:sqlite:db/testDB.db");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CreateLogTableMonth()
    {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM");

        String sql = "CREATE TABLE IF NOT EXISTS t" + dateFormat.format(date) + " (\n"
                + "    origin text NOT NULL,\n"
                + "    destination text NOT NULL,\n"
                + "    depart_date text NOT NULL,\n"
                + "    return_date text NOT NULL,\n"
                + "    currency text NOT NULL,\n"
                + "    code_reply integer,\n"
                + "    full_request text NOT NULL\n"
                + ");";

        try
        {
            Statement stmt = co.createStatement();
            stmt.execute(sql);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    public void CreateLogTableDay()
    {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String sql = "CREATE TABLE IF NOT EXISTS t" + dateFormat.format(date) + " (\n"
                + "    origin text NOT NULL,\n"
                + "    destination text NOT NULL,\n"
                + "    depart_date text NOT NULL,\n"
                + "    return_date text NOT NULL,\n"
                + "    currency text NOT NULL,\n"
                + "    code_reply integer,\n"
                + "    full_request text NOT NULL\n"
                + ");";

        try
        {
            Statement stmt = co.createStatement();
            stmt.execute(sql);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void InsertLogLineInTableMonth(String origin, String destination,
                                          String depart_date, String return_date,
                                          String currency, int code_reply, String full_request)
    {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM");
        String sql = "INSERT INTO t" + dateFormat.format(date) +
                " (origin,destination,depart_date,return_date,currency,code_reply,full_request) VALUES(?,?,?,?,?,?,?)";

        try {
            PreparedStatement pstmt = co.prepareStatement(sql);
            pstmt.setString(1, origin);
            pstmt.setString(2, destination);
            pstmt.setString(3, depart_date);
            pstmt.setString(4, return_date);
            pstmt.setString(5, currency);
            pstmt.setInt(6, code_reply);
            pstmt.setString(7, full_request);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void InsertLogLineInTableDay(String origin, String destination,
                                        String depart_date, String return_date,
                                        String currency, int code_reply, String full_request)
    {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String sql = "INSERT INTO t" + dateFormat.format(date) + " (origin,destination,depart_date,return_date,currency,code_reply,full_request) VALUES(?,?,?,?,?,?,?)";

        try {
            PreparedStatement pstmt = co.prepareStatement(sql);
            pstmt.setString(1, origin);
            pstmt.setString(2, destination);
            pstmt.setString(3, depart_date);
            pstmt.setString(4, return_date);
            pstmt.setString(5, currency);
            pstmt.setInt(6, code_reply);
            pstmt.setString(7, full_request);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int GetCountMonth(String origin, String destination)
    {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM");
        String sql = "SELECT COUNT(*) FROM t" +
                dateFormat.format(date) + " WHERE origin LIKE '%" +
                origin + "%' AND destination LIKE '%" + destination + "%'";
        try {
            Statement stmt = co.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int GetCountDay(String origin, String destination)
    {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String sql = "SELECT COUNT(*) FROM t" +
                dateFormat.format(date) + " WHERE origin LIKE '%" +
                origin + "%' AND destination LIKE '%" + destination + "%'";
        try {
            Statement stmt = co.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public String getMostPopularInDay()
    {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String sql = "SELECT destination FROM t" +
                dateFormat.format(date) + " WHERE code_reply = '1' GROUP BY destination ORDER BY COUNT(destination) DESC";
        try {
            Statement stmt = co.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            return resultSet.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally
        {
            if (co != null)
            {
                try
                {
                    co.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return "ERROR";
    }

    public String getMostPopularInMonth()
    {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM");
        String sql = "SELECT destination FROM t" +
                dateFormat.format(date) + " WHERE code_reply = '1' GROUP BY destination ORDER BY COUNT(destination) DESC";
        try {
            Statement stmt = co.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            return resultSet.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally
        {
            if (co != null)
            {
                try
                {
                    co.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return "ERROR";
    }
}