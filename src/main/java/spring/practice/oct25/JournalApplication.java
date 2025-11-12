package spring.practice.oct25;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@SpringBootApplication
public class JournalApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext context= SpringApplication.run(JournalApplication.class, args);
        Environment environment =context.getEnvironment();
        System.out.print("active envs:");
        Arrays.stream(environment.getActiveProfiles()).forEach(System.out::println);
       // System.out.println("active envs:"+ Arrays.stream(environment.getActiveProfiles()));

    }



}