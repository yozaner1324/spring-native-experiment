package org.example;

import java.io.Serializable;

public record Order(Long id, String productName, Integer quantity) implements Serializable {
}
