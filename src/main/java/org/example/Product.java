package org.example;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

public record Product(@Id String name, Float price, Integer quantity) implements Serializable {
}
