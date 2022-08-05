package ziko.spring.webFlux.WithFunctionalEndpoints.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ziko.spring.webFlux.WithFunctionalEndpoints.model.Product;

public interface ProductRepository extends ReactiveMongoRepository <Product, String> {

}
