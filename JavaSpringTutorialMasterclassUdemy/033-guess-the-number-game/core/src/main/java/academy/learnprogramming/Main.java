package academy.learnprogramming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by jasonsmac on 2020-02-02.
 */
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("Guess the number game");

        /* 📖 L.33 Using Java Annotation Configuration */
        // create context (container)
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        // ⬆️ defining the actual IOC container; coding to the interface (Java best practice)

        // get the number generator bean from the context (container) (again we code to the interface)
//        NumberGenerator numberGenerator = context.getBean("numberGenerator", NumberGenerator.class);
        NumberGenerator numberGenerator = context.getBean(NumberGenerator.class);   // 📖 L.32 Remove the bean name since it is auto-assigned

        // call method next() to get a random number
        int number = numberGenerator.next();

        // log generated number
        log.info("number = {}", number);
        // ⬆️ Parameterized Logging: slf4j will automatically replace "{}" with the arguments after comma

        /* 📖 L.26 */
        // get game bean from context (container)
        Game game = context.getBean(Game.class);

        /* 📖 L.34: Message Generator Challenge */
        MessageGenerator messageGenerator = context.getBean(MessageGenerator.class);
        log.info("getMainMessage = {}", messageGenerator.getMainMessage());
        log.info("getResultMessage = {}", messageGenerator.getResultMessage());

        // call reset method
//        game.reset(); // 📖 L.29 - we can configure the container to call it automatically (see beans.xml)

        // close context (container)
        context.close();
    }
}
