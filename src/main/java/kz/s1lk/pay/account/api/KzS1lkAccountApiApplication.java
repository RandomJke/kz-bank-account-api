package kz.s1lk.pay.account.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info=@Info(title="Bank account API"))
public class KzS1lkAccountApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(KzS1lkAccountApiApplication.class, args);
    }

}
