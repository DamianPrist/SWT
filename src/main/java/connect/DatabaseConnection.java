package connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接工具类
 */
public class DatabaseConnection {
    // 数据库连接参数
    private static final String URL = "jdbc:mysql://mysql5.sqlpub.com:3310/test_lib";
    private static final String USERNAME = "triangle_test";
    private static final String PASSWORD = "UDBKy4IHvygHHKrq";

    /**
     * 获取数据库连接
     * @return Connection 数据库连接对象
     * @throws SQLException 数据库连接异常
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    /**
     * 关闭数据库连接
     * @param connection 数据库连接对象
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
