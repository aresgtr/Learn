package academy.learnprogramming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by jasonsmac on 2020-02-02.
 */
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    private static final String CONFIG_LOCATION = "beans.xml";

    public static void main(String[] args) {
        log.info("Guess the number game");

        /* 📖 L.24: Using a Spring Container */
        // create context (container)
        ConfigurableApplicationContext context = new ClassPathXmlApplicationContext(CONFIG_LOCATION);
        // ⬆️ defining the actual IOC container; coding to the interface (Java best practice)

        // get the number generator bean from the context (container) (again we code to the interface)
        NumberGenerator numberGenerator = context.getBean("numberGenerator", NumberGenerator.class);

        // call method next() to get a random number
        int number = numberGenerator.next();

        // log generated number
        log.info("number = {}", number);
        // ⬆️ Parameterized Logging: slf4j will automatically replace "{}" with the arguments after comma

        /* 📖 L.26 */
        // get game bean from context (container)
        Game game = context.getBean(Game.class);

        // call reset method
        game.reset();

        // close context (container)
        context.close();
    }
}
