package cl.GestionDrones.v1.Contratistas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "cl.GestionDrones.v1")
@EnableJpaRepositories(basePackages = "cl.GestionDrones.v1")
public class ContratistasApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContratistasApplication.class, args);
    }

}
