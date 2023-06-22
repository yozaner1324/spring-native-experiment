package org.example;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

public record Order(@Id Long id, String productName, Integer quantity) implements Serializable {
}
