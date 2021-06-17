package connect.as2.as2middleware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
//@PropertySource(value = {"file:OpenAS2Servers/as2-middleware/application.properties"}, ignoreResourceNotFound = false)
public class As2MiddlewareApplication {

    public static void main(String[] args) {
        SpringApplication.run(As2MiddlewareApplication.class, args);
    }

}
