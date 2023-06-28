package org.example;

import java.io.Serializable;
import java.util.List;

public record Order(Long id, List<Product> products, Float total) implements Serializable {
    public Order(Long id, Product product) {
        this(id, List.of(product));
    }

    public Order(Long id, List<Product> products) {
        this(id, products, calcTotal(products));
    }

    private static Float calcTotal(List<Product> products) {
        return (float)products.stream().mapToDouble(product -> product.price() * product.quantity()).sum();
    }
}
