package cwz.study.switchdatasource;

import cwz.study.switchdatasource.datasource.DataSourceManagement;
import cwz.study.switchdatasource.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@SpringBootTest
class SwitchdatasourceApplicationTests {


    @Autowired
    DataSourceManagement dataSourceCenter;
    @Autowired
    OrderServiceImpl orderService;

    /**
     * 对读写分离 - 动态 切换数据源版本 1.0 进行测试
     * 测试时会打印当前使用的数据库的URL，用这个来判断是否切换成功
     * insert 使用主库
     * query 从库负载均衡
     */
    @Test @Transactional
    public void testInsertAndQuery() {
        String sql = "insert into t_order (order_id, user_id) VALUES (1, 1);";
        // 使用主库 master
        orderService.insert(dataSourceCenter.getDefaultDataSource(), sql);

        sql = "select * from t_order limit 5;";
        // 使用从库 slave1
        List<Map<String, Object>> entities = orderService.list(dataSourceCenter.getDefaultDataSource(), sql);
        for (Map item: entities) {
            System.out.println(item.toString());
        }

        // 使用从库slave2
        orderService.list(dataSourceCenter.getDefaultDataSource(), sql);
    }
}
