/*
 * <p>文件名称: HikariDemo</p>
 * <p>文件描述: </p>
 * <p>版权所有: 版权所有(C)2019-</p>
 * <p>内容摘要:  </p>
 * <p>其他说明:  </p>
 * <p>创建日期: 2022/6/8 21:07 </p>
 * <p>完成日期: </p>
 * <p>修改记录1:</p>
 * <pre>
 *    修改日期：
 *    版 本 号：
 *    修 改 人：
 *    修改内容：
 * </pre>
 *
 * @version 1.0
 * @author chenwz
 */
package cwz.study.jdbcdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication
public class HikariDemo {

    public static void main(String[] args) throws SQLException {
        ApplicationContext context = SpringApplication.run(HikariDemo.class, args);
        //1.获取数据源
        DataSource dataSource = context.getBean(DataSource.class);
        System.out.println(dataSource);
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            System.out.println(connection);
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.close();
        }

    }
}
