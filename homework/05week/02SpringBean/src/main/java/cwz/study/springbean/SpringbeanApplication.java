package cwz.study.springbean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
public class SpringbeanApplication {
    @Autowired
    static Bean1 bean1;


    public static void main(String[] args) {
        //SpringApplication.run(SpringbeanApplication.class, args);
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:config/applicationContext.xml");

        //Bean1
        Bean1 bean1 = (Bean1) applicationContext.getBean("Bean1");
        bean1.myName();

        //Bean2
        Bean2 bean2 = (Bean2) applicationContext.getBean("bean2");
        bean2.myName();
    }

}
