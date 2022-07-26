package cwz.study.jmsactivemp;

import cwz.study.jmsactivemp.jms.JmsProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableJms
@Slf4j
public class Application implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private JmsProducer producer;

    @Override
    public void run(ApplicationArguments args) {
        String topic = "activeTest";
        Map<String, String> message = new HashMap<>(1);
        message.put("test", "test");
        log.info("send message to topic " + topic + " :: " + message);
        producer.sendMessage(topic, message);
    }

}
