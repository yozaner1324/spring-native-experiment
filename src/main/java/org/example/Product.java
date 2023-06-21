package org.example;

import org.springframework.data.annotation.Id;

public record Product(@Id String name, Float price, Integer quantity) {
}
