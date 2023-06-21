package org.example;

import org.springframework.data.annotation.Id;

public record Order(@Id Long id, String productName, Integer quantity) {
}
