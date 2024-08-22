package academy.learnprogramming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class L19Logback {

    private final static Logger log = LoggerFactory.getLogger(L19Logback.class);    // current class

    public static void main(String[] args) {
        log.info("Hello Info");
        log.debug("Hello Debug");
    }
}
