package org.example;

import org.springframework.data.gemfire.repository.GemfireRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends GemfireRepository<Order, Long> {
}
