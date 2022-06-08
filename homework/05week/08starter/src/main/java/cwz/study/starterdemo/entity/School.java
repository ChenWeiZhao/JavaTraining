/*
 * <p>文件名称: School</p>
 * <p>文件描述: </p>
 * <p>版权所有: 版权所有(C)2019-</p>
 * <p>内容摘要:  </p>
 * <p>其他说明:  </p>
 * <p>创建日期: 2022/6/7 0:30 </p>
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

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Data
@Component("School")
public class School implements ISchool{

    @Autowired(required = true)
    Klass class100;

    @Autowired
    @Qualifier("student2")
    Student student2;

    @Override
    public void ding() {

        System.out.println("Class1 have " + this.class100.getStudents().size() + " students and one is " + this.student2);

    }
}
