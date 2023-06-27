package org.example;

import java.io.Serializable;

public record Product(String name, Float price, Integer quantity) implements Serializable {
}
