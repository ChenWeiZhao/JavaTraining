/*
 * <p>文件名称: LoadStudent</p>
 * <p>文件描述: </p>
 * <p>版权所有: 版权所有(C)2019-</p>
 * <p>内容摘要:  </p>
 * <p>其他说明:  </p>
 * <p>创建日期: 2022/6/8 23:16 </p>
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
package cwz.study.starterdemo;

import cwz.study.starterdemo.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * 根据配置属性创建bean,如果无配置按默认值创建
 */
@Configuration
@ConditionalOnProperty(prefix = "cwz.study.school", name = "enable", havingValue = "true", matchIfMissing = true)
public class LoadStudent {
    @Autowired
    SchoolPropeyties schoolPropeyties;

    @Bean
    public Student student1() {
        if (Objects.isNull(schoolPropeyties.getStudent1())) {
            return new Student("newStudentID1", "newStudent1Name1");
        } else {
            return schoolPropeyties.getStudent1();
        }
    }

    @Bean
    public Student student2() {
        if (Objects.isNull(schoolPropeyties.getStudent2())) {
            return new Student("newStudentID2", "newStudent1Name2");
        } else {
            return schoolPropeyties.getStudent2();
        }
    }


}
