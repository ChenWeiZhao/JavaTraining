package cwz.study.jdbcdemo;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;
import java.util.Arrays;

@SpringBootApplication
public class JdbcDemo {

    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/sakila?serverTimezone=Asia/Shanghai";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    public static void main(String[] args) throws Exception {
        //0.加载驱动
        Class.forName(DB_DRIVER);
        //1.使用 PreparedStatement 执行数据库操作，手动提交
        jdbcPreparedStatement();

        // 2.批处理方式
        jdbcBatch();

    }

    private static void jdbcBatch() throws SQLException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            // 3、开启手动提交
            connection.setAutoCommit(false);
            String sql = "insert into city(city_id,city,country_id) value(?,?,?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                for (int i = 0; i < 100; i++) {
                    int initId = 1000 + i;
                    preparedStatement.setInt(1, initId);
                    preparedStatement.setString(2, "cwz" + i);
                    preparedStatement.setInt(3, initId);
                    preparedStatement.addBatch();
                }
                int[] rowCount = preparedStatement.executeBatch();
                System.out.println("插入行数" + Arrays.stream(rowCount).sum());
                connection.commit();
            }
        } catch (Exception e) {
            // 出现异常回滚
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            // 关闭连接
            if (connection != null) {
                connection.close();
            }
        }
    }

    private static void jdbcPreparedStatement() throws SQLException {
        //创建连接
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            //手动提交
            connection.setAutoCommit(false);
            String sql = "select * from city";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                System.out.println(resultSet.getString("city"));
            }
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

}
