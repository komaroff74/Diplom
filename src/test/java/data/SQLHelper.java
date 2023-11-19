package data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLHelper {
    private SQLHelper() {}
    private static final QueryRunner runner = new QueryRunner();

    private static final String url = System.getProperty("db.url");
    private static final String user = System.getProperty("db.user");
    private static final String password = System.getProperty("db.password");

    @SneakyThrows
    private static Connection getConn(){
        return DriverManager.getConnection(url, user, password);
    }

    @SneakyThrows
    public static String getPaymentStatus() {
        var codeSQL = "SELECT status FROM payment_entity";
        return runner.query(getConn(), codeSQL, new ScalarHandler<>());
    }

    @SneakyThrows
    public static String getCreditStatus() {
        var codeSQL = "SELECT status FROM credit_request_entity";
        return runner.query(getConn(), codeSQL, new ScalarHandler<>());
    }

    @SneakyThrows
    public static void cleanDatabase() {
        var connection = getConn();
        runner.execute(connection, "DELETE FROM order_entity");
        runner.execute(connection, "DELETE FROM payment_entity;");
        runner.execute(connection, "DELETE FROM credit_request_entity;");
    }
}