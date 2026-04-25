package org.minttwo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class})
@ComponentScan("org.minttwo.configurations")
@ComponentScan("org.minttwo.controllers")
public class MintTwo {
    public static void main(String[] args) {
        SpringApplication.run(MintTwo.class, args);
    }
}
