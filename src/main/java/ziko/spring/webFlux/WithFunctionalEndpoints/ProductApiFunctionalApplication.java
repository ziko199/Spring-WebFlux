package ziko.spring.webFlux.WithFunctionalEndpoints;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import ziko.spring.webFlux.WithFunctionalEndpoints.model.Product;
import ziko.spring.webFlux.WithFunctionalEndpoints.repository.ProductRepository;
import ziko.spring.webFlux.WithFunctionalEndpoints.handler.ProductHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
public class ProductApiFunctionalApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApiFunctionalApplication.class);
    }

    @Bean
    CommandLineRunner init (ProductRepository repository) {
        return args -> {
            Flux<Product> productFlux = Flux.just(
                    new Product(null, "Big Latte", 2.99),
                    new Product(null, "Big Decaf", 2.49),
                    new Product(null, "Green Tea", 1.99)
                    ).flatMap(repository::save);

            productFlux
                    .thenMany(repository.findAll())
                    .subscribe(System.out::println);
        };
    }

    @Bean
    RouterFunction<ServerResponse> routes(ProductHandler handler) {
//        return route()
//                .GET("/products/events", accept(MediaType.TEXT_EVENT_STREAM), handler::getProductEvents)
//                .GET("/products/{id}", accept(MediaType.APPLICATION_JSON), handler::getProduct)
//                .GET("/products", accept(MediaType.APPLICATION_JSON), handler::getAllProducts)
//                .PUT("/products/{id}", accept(MediaType.APPLICATION_JSON), handler::updateProduct)
//                .POST("/products", accept(MediaType.TEXT_EVENT_STREAM), handler::saveProduct)
//                .DELETE("/products/{id}", accept(MediaType.APPLICATION_JSON), handler::deleteProduct)
//                .DELETE("/products", accept(MediaType.APPLICATION_JSON), handler::deleteAllProducts)
//                .build();

        return route()
                .path("/products",
                        builder -> builder
                                .nest(accept(MediaType.APPLICATION_JSON)
                                        .or(contentType(MediaType.APPLICATION_JSON))
                                        .or(accept(MediaType.TEXT_EVENT_STREAM)),
                                        nestedBuilder -> nestedBuilder
                                                .GET("/events", handler::getProductEvents)
                                                .GET("/{id}", handler::getProduct)
                                                .GET(handler::getAllProducts)
                                                .PUT("/{id}", handler::updateProduct)
                                                .POST(handler::saveProduct)
                                )
                                .DELETE("/{id}", handler::deleteProduct)
                                .DELETE( handler::deleteAllProducts)

                ).build();
    }
}
