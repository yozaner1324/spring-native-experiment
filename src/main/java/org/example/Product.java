package org.example;

import org.springframework.data.annotation.Id;

public record Product(@Id String name, float price, int quantity) {
}
