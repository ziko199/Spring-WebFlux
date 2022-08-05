package ziko.spring.webFlux.WithAnnotatedControllers;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;
import ziko.spring.webFlux.WithAnnotatedControllers.model.Product;
import ziko.spring.webFlux.WithAnnotatedControllers.repository.ProductRepository;

@SpringBootApplication
public class ProductApiAnnotationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApiAnnotationApplication.class);
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
}
