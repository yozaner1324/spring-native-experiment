package org.example;

import org.springframework.data.gemfire.repository.GemfireRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends GemfireRepository<Product, String> {
}
