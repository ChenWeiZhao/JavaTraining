package cwz.study.switchdatasource.service;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public interface OrderService {

    void insert(DataSource dataSource, String sql);

    List<Map<String, Object>> list(DataSource dataSource, String sql);

}
