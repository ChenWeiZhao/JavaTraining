package cwz.study.switchdatasource2;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
class Switchdatasource2ApplicationTests {

    @Autowired
    ShardingMasterSlaveDataSource shardingMasterSlaveDataSource;

    /**
     * 运行后从日志可以看出主从之间的切换和从库负载均衡
     */
    @Test
    @Transactional
    public void test() throws SQLException {
        DataSource dataSource = shardingMasterSlaveDataSource.createDataSource();
        log.info("ShardingMasterSlaveDataSource info::" + dataSource.getConnection().getMetaData().getURL());

        Connection conn = dataSource.getConnection();
        Statement statement = conn.createStatement();

        String sql = "select * from t_order limit 5";
        statement.execute(sql);
        statement.execute(sql);

        sql = "insert into t_order (order_id, user_id) VALUES (1, 1);";
        statement.execute(sql);

        sql = "select * from t_order limit 5";
        statement.execute(sql);
    }
}
