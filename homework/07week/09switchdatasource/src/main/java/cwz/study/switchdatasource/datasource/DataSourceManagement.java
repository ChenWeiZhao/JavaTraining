/*
 * <p>文件名称: DataSourceManagement</p>
 * <p>文件描述: </p>
 * <p>版权所有: 版权所有(C)2019-</p>
 * <p>内容摘要:  </p>
 * <p>其他说明:  </p>
 * <p>创建日期: 2022/6/21 22:25 </p>
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
package cwz.study.switchdatasource.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Random;

@Component
public class DataSourceManagement {

    @Autowired
    @Qualifier("mastersource")
    DataSource masterDataSource;
    @Autowired
    @Qualifier("slave1source")
    DataSource slave1DataSource;
    @Autowired
    @Qualifier("slave2source")
    DataSource slave2DataSource;

    public DataSource getDefaultDataSource() {
        return masterDataSource;
    }

    public DataSource getSlaveDataSource() {
        Random random = new Random();
        int i = random.nextInt(2);
        if (i == 1){
            return slave1DataSource;
        }
        return slave2DataSource;
    }

}
