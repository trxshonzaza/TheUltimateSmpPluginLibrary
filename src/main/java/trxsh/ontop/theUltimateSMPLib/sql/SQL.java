package trxsh.ontop.theUltimateSMPLib.sql;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;
import java.util.Map;

public class SQL {
    public static String url, username, password, database;
    private static HikariDataSource source;
    private static boolean initialized = false;
    
    public static void Initialize(String url, int port, String username, String password, String database) throws ClassNotFoundException, SQLException {
        SQL.url = url;
        SQL.username = username;
        SQL.password = password;
        SQL.database = database;

        source = new HikariDataSource();

        source.setDataSourceClassName("com.mysql.cj.jdbc.MysqlDataSource");

        source.addDataSourceProperty("serverName", url);
        source.addDataSourceProperty("port", port);
        source.addDataSourceProperty("databaseName", database);
        source.addDataSourceProperty("user", username);
        source.addDataSourceProperty("password", password);

        Connection connection = getConnection();
        
        if(connection == null)
            throw new SQLException("could not establish a connection to the database");

        initialized = true;
        Bukkit.getLogger().info("SQL initalized");
        connection.close();
    }

    private static Connection getConnection() throws SQLException {
        return source.getConnection();
    }

    public static void createTable(String tableName, Map<String, String> args) {
        try(Connection con = getConnection()) {
            if(!initialized)
                throw new SQLException("SQL is not initialized. (initialized boolean is false or Initialize not called)");

            StringBuilder sb = new StringBuilder();

            sb.append("CREATE TABLE ");
            sb.append(tableName + " (");

            int index = 0;

            for(Map.Entry<String, String> entry : args.entrySet()) {
                sb.append(entry.getKey() + " " + entry.getValue());

                if(index != args.entrySet().size() - 1) {
                    sb.append(", ");
                }

                index++;
            }

            sb.append(");");

            Bukkit.getLogger().info(sb.toString());

            try(Statement ps = con.createStatement()) {
                ps.executeUpdate(sb.toString());
            }
        } catch(SQLException e) {
            Bukkit.getLogger().info("could not create table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static CachedRowSet select(String tableName, String columns, String condition) {
        CachedRowSet rws = null;

        try(Connection con = getConnection()) {
            if(!initialized)
                throw new SQLException("SQL is not initialized. (initialized boolean is false or Initialize not called)");

            StringBuilder sb = new StringBuilder();

            sb.append("SELECT ");
            sb.append(columns);
            sb.append(" FROM ");
            sb.append(tableName);

            if(condition != null && !condition.isEmpty()) {
                sb.append(" WHERE ");
                sb.append(condition);
            }

            Bukkit.getLogger().info("query: " + sb);

            try(Statement ps = con.createStatement()) {
                ResultSet rs = ps.executeQuery(sb.toString());

                rws = RowSetProvider.newFactory().createCachedRowSet();
                rws.populate(rs);
            }
        }catch(SQLException e) {
            Bukkit.getLogger().info("could not execute select: " + e.getMessage());
            e.printStackTrace();
        }

        return rws;
    }

    public static int insert(String tableName, Object... args) {
        try(Connection con = getConnection()) {
            if(!initialized)
                throw new SQLException("SQL is not initialized. (initialized boolean is false or Initialize not called)");

            StringBuilder sb = new StringBuilder();

            sb.append("INSERT INTO ");
            sb.append(tableName);

            sb.append(" VALUES(");

            for(int i = 0; i < args.length; i++) {
                sb.append("?");

                if(i != args.length - 1) {
                    sb.append(", ");
                }
            }

            sb.append(");");

            Bukkit.getLogger().info("query: " + sb);

            try(PreparedStatement ps = con.prepareStatement(sb.toString())) {
                for(int i = 0; i < args.length; i++) {
                    ps.setObject(i + 1, args[i]);
                }

                return ps.executeUpdate();
            }
        }catch(SQLException e) {
            Bukkit.getLogger().info("could not execute insert: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    public static void update(String tableName, String conditon, Map<String, Object> args) {
        try(Connection con = getConnection()) {
            if(!initialized)
                throw new SQLException("SQL is not initialized. (initialized boolean is false or Initialize not called)");

            StringBuilder sb = new StringBuilder();

            sb.append("UPDATE ");
            sb.append(tableName);

            sb.append(" SET ");

            int index = 0;

            for(Map.Entry<String, Object> entry : args.entrySet()) {
                sb.append(entry.getKey());
                sb.append(" = ?");

                if(index != args.entrySet().size() -1) {
                    sb.append(", ");
                }

                index++;
            }

            if(conditon != null && !conditon.isEmpty()) {
                sb.append(" WHERE ");
                sb.append(conditon);
            }

            try(PreparedStatement ps = con.prepareStatement(sb.toString())) {
                for(int i = 0; i < args.values().size(); i++) {
                    ps.setObject(i + 1, args.values().toArray()[i]);
                }

                Bukkit.getLogger().info("query: " + sb);

                ps.executeUpdate();
            }
        }catch(SQLException e) {
            Bukkit.getLogger().info("could not execute update: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public static int executeCustomUpdate(String sql, Object... args) {
        try(Connection con = getConnection()) {
            if(!initialized)
                throw new SQLException("SQL is not initialized. (initialized boolean is false or Initialize not called)");

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                for (int i = 0; i < args.length; i++) {
                    ps.setObject(i + 1, args[i]);
                }

                return ps.executeUpdate();
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info("could not execute custom update: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    public static CachedRowSet executeCustomQuery(String sql, Object... args) {
        CachedRowSet rws = null;

        try(Connection con = getConnection()) {
            if(!initialized)
                throw new SQLException("SQL is not initialized. (initialized boolean is false or Initialize not called)");

            try(PreparedStatement ps = con.prepareStatement(sql)) {

                for(int i = 0; i < args.length; i++) {
                    ps.setObject(i + 1, args[i]);
                }

                ResultSet rs = ps.executeQuery();
                rws = RowSetProvider.newFactory().createCachedRowSet();

                rws.populate(rs);
            }
        } catch (SQLException e) {
            Bukkit.getLogger().info("could not execute custom query: " + e.getMessage());
            e.printStackTrace();
        }

        return rws;
    }

    public static boolean tableExists(String tableName) {
        try(CachedRowSet rws = select(tableName, SqlConstants.EVERY_COLUMN, null)) {
            return rws != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean rowExists(String tableName, String condition) {
        try (CachedRowSet rws = select(tableName, SqlConstants.EVERY_COLUMN, condition)) {
            return rws != null && rws.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String convertString(String in) {
        return "'" + in + "'";
    }

    public static boolean isValid() throws SQLException {
        Connection con = getConnection();

        if(con != null) {
            con.close();
            return true;
        } else {
            return false;
        }
    }
}
