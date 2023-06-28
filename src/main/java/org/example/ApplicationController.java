package org.example;

import org.apache.geode.cache.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class ApplicationController {

    private final Region<String, Product> productRegion;
    private final Region<Long, Order> orderRegion;

    @Autowired
    public ApplicationController(@Qualifier("products") Region<String, Product> productRegion, @Qualifier("orders") Region<Long, Order> orderRegion) {

        this.productRegion = productRegion;
        this.orderRegion = orderRegion;
    }

    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }

    @RequestMapping("/products")
    Collection<Product> products() {
        return productRegion.getAll(productRegion.keySetOnServer()).values();
    }

    @RequestMapping("/orders")
    Collection<Order> orders() {
        return orderRegion.getAll(orderRegion.keySetOnServer()).values();
    }

    @RequestMapping("/order")
    public boolean order(@RequestParam String productName, @RequestParam Integer quantity) {
        Product foundProduct = productRegion.get(productName);
        if (foundProduct == null) {
            System.out.println("Product \"" + productName + "\" not found");
            return false;
        }

        if (foundProduct.quantity() < quantity) {
            System.out.println("Not enough \"" + productName + "\", requested " + quantity + ", but there are only " + foundProduct.quantity());
            return false;
        }
        long size = orderRegion.keySetOnServer().size();
        orderRegion.put(size, new Order(size, new Product(foundProduct.name(), foundProduct.price(), quantity)));
        productRegion.put(foundProduct.name(), new Product(foundProduct.name(), foundProduct.price(), foundProduct.quantity() - quantity));
        return true;
    }

    @RequestMapping("/stock")
    public boolean stock(@RequestParam String productName, @RequestParam Float price, @RequestParam Integer quantity) {
        Product foundProduct = productRegion.get(productName);
        if (foundProduct == null) {
            productRegion.put(productName, new Product(productName, price, quantity));
            return true;
        }

        productRegion.put(foundProduct.name(), new Product(foundProduct.name(), price, foundProduct.quantity() + quantity));
        return true;
    }
}
