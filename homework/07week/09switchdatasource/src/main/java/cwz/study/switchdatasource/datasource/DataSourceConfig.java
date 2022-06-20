/*
 * <p>文件名称: DataSource</p>
 * <p>文件描述: </p>
 * <p>版权所有: 版权所有(C)2019-</p>
 * <p>公   司: 金证财富南京科技有限公司</p>
 * <p>内容摘要:  </p>
 * <p>其他说明:  </p>
 * <p>创建日期: 2022/6/20 20:48 </p>
 * <p>完成日期: </p>
 * <p>修改记录1:</p>
 * <pre>
 *    修改日期：
 *    版 本 号：
 *    修 改 人：
 *    修改内容：
 * </pre>
 * <p>修改记录2：…</p>
 *
 * @version 1.0
 * @author chenwz@szkingdom.com
 */
package cwz.study.switchdatasource.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.DriverManager;


@Configuration
public class DataSourceConfig {
    @Autowired
    Environment environment;

    @Primary
    @Bean(name = "mastersource")
    public DataSource masterDatasource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty("datasource.master.driver-class-name"));
        dataSource.setUrl(environment.getProperty("datasource.master.url"));
        dataSource.setUsername(environment.getProperty("datasource.master.username"));
        dataSource.setPassword(environment.getProperty("datasource.master.password"));
        return dataSource;
    }
    @Primary
    @Bean(name = "slave1source")
    public DataSource slave1Datasource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty("datasource.slave1.driver-class-name"));
        dataSource.setUrl(environment.getProperty("datasource.slave1.url"));
        dataSource.setUsername(environment.getProperty("datasource.slave1.username"));
        dataSource.setPassword(environment.getProperty("datasource.slave1.password"));
        return dataSource;
    }
    @Primary
    @Bean(name = "slave2source")
    public DataSource slave2Datasource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getProperty("datasource.slave2.driver-class-name"));
        dataSource.setUrl(environment.getProperty("datasource.slave2.url"));
        dataSource.setUsername(environment.getProperty("datasource.slave2.username"));
        dataSource.setPassword(environment.getProperty("datasource.slave2.password"));
        return dataSource;
    }


}
