/*
 * <p>文件名称: OrderServiceImpl</p>
 * <p>文件描述: </p>
 * <p>版权所有: 版权所有(C)2019-</p>
 * <p>内容摘要:  </p>
 * <p>其他说明:  </p>
 * <p>创建日期: 2022/6/21 21:49 </p>
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
package cwz.study.switchdatasource.service.impl;

import cwz.study.switchdatasource.annotation.SwitchReadSlave;
import cwz.study.switchdatasource.service.OrderService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("OrderService")
public class OrderServiceImpl implements OrderService {

    @SneakyThrows
    @Override
    public void insert(DataSource dataSource, String sql) {
        System.out.println(dataSource.getConnection().getMetaData().getURL());
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @SwitchReadSlave
    @SneakyThrows
    @Override
    public List<Map<String, Object>> list(DataSource dataSource, String sql) {
        System.out.println(dataSource.getConnection().getMetaData().getConnection());
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            List<Map<String, Object>> models = new ArrayList<>();
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Map<String, Object> model = new HashMap<>();
                model.put("id", resultSet.getLong("id"));
                model.put("name", resultSet.getString("name"));
                models.add(model);
            }
            return models;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }


        }
        return null;
    }
}
