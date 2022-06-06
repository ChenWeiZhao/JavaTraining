/*
 * <p>文件名称: Student</p>
 * <p>文件描述: </p>
 * <p>版权所有: 版权所有(C)2019-</p>
 * <p>内容摘要:  </p>
 * <p>其他说明:  </p>
 * <p>创建日期: 2022/6/7 0:31 </p>
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
package cwz.study.starterdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.context.ApplicationContext;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Student {

    private int id;
    private String name;
    private String beanName;
    private ApplicationContext applicationContext;

    public void init() {
        System.out.println("hello...........");
    }

    public static Student create() {
        return new Student(102, "KK102", null, null);
    }

    public void print() {
        System.out.println(this.beanName);
        System.out.println("context.getBeanDefinitionNames() ===>> " + String.join(",", applicationContext.getBeanDefinitionNames()));

    }
}
