package cl.GestionDrones.v1.Contratistas.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient aeronavesWebClient(WebClient.Builder builder) {
        return builder.baseUrl("http://aeronaves.onrender.com/api/v1/aeronaves").build();
    }

    @Bean
    public WebClient planesDeVuelosWebClient(WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8083/api/v1/planesDeVuelos").build();
    }

    @Bean
    public WebClient pilotosWebClient(WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8081/api/v1/pilotos").build();
    }
}
