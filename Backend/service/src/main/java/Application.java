
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"controller"})
public class Application {

    public static void main(String[] args) {
        // TODO maybe we need to set port number for deploy purpose (80 for heroku)
        SpringApplication.run(Application.class, args);
    }
}