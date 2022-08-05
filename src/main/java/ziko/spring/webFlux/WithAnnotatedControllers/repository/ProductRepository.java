package ziko.spring.webFlux.WithAnnotatedControllers.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ziko.spring.webFlux.WithAnnotatedControllers.model.Product;

public interface ProductRepository extends ReactiveMongoRepository <Product, String> {

}
