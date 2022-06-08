package cwz.study.starterdemo;

import cwz.study.starterdemo.entity.Klass;
import cwz.study.starterdemo.entity.School;
import cwz.study.starterdemo.entity.Student;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class StarterdemoApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(StarterdemoApplication.class, args);
        School school = context.getBean("School", School.class);
        System.out.println(school);
        school.ding();

        System.out.println("------------------------------------");
        Klass class1 = context.getBean("class100", Klass.class);
        System.out.println(class1);
        class1.dong();

        System.out.println("------------------------------------");
        Student student1 = context.getBean("student2", Student.class);
        student1.print();


    }

}
