package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
public class ApplicationController {

    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;

    @Autowired
    public ApplicationController(ProductRepo productRepo, OrderRepo orderRepo) {

        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
    }

    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }

    @RequestMapping("/products")
    List<Product> products() {
        LinkedList<Product> products = new LinkedList<>();
        productRepo.findAll().forEach(products::add);
        return products;
    }

    @RequestMapping("/orders")
    List<Order> orders() {
        LinkedList<Order> orders = new LinkedList<>();
        orderRepo.findAll().forEach(orders::add);
        return orders;
    }

    @RequestMapping("/order")
    @Transactional
    public boolean order(@RequestParam String productName, @RequestParam Integer quantity) {
        Optional<Product> foundProduct = productRepo.findById(productName);
        if (foundProduct.isEmpty()) {
            System.out.println("Product \"" + productName + "\" not found");
            return false;
        }
        Product product = foundProduct.get();
        if (product.quantity() < quantity) {
            System.out.println("Not enough \"" + productName + "\", requested " + quantity + ", but there are only " + product.quantity());
            return false;
        }
        orderRepo.save(new Order(orderRepo.count(), productName, quantity));
        productRepo.save(new Product(product.name(), product.price(), product.quantity() - quantity));
        return true;
    }

    @RequestMapping("/stock")
    public boolean stock(@RequestParam String productName, @RequestParam Float price, @RequestParam Integer quantity) {
        Optional<Product> foundProduct = productRepo.findById(productName);
        if (foundProduct.isEmpty()) {
            productRepo.save(new Product(productName, price, quantity));
            return true;
        }
        Product product = foundProduct.get();

        productRepo.save(new Product(product.name(), price, product.quantity() + quantity));
        return true;
    }
}
