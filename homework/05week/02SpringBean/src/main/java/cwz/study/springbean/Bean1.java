/*
 * <p>文件名称: Bean1</p>
 * <p>文件描述: </p>
 * <p>版权所有: 版权所有(C)2019-</p>
 * <p>内容摘要:  </p>
 * <p>其他说明:  </p>
 * <p>创建日期: 2022/6/6 0:35 </p>
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
package cwz.study.springbean;

import lombok.Data;
import org.springframework.stereotype.Component;


@Component(value = "Bean1")
@Data
public class Bean1 {
    String id;
    String name;

    public void myName() {
        System.out.println(Bean1.class.getName());
        System.out.println(getId() + getName());
    }
}

