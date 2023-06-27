package org.example;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.lang.management.ManagementFactory;

import static org.apache.geode.cache.client.ClientRegionShortcut.PROXY;

@SpringBootApplication
public class MyApplication {

    public static void main(String[] args) {
        long currentTime = System.currentTimeMillis();
        long vmStartTime = ManagementFactory.getRuntimeMXBean().getStartTime();
        System.out.println("Startup time: " + (currentTime - vmStartTime) + "ms");
        SpringApplication.run(MyApplication.class, args);
    }

    @Bean
    ApplicationRunner runner(Region<String, Product> productRegion) {
        return args -> {
            Product chair = new Product("chair", 49.99f, 7);
           productRegion.put(chair.name(), chair);

            System.out.println(productRegion.get("chair"));
        };
    }

    @Bean(name = "cache")
    public ClientCache createCache() {
        return new ClientCacheFactory().create();
    }

    @Bean(name = "products")
    public Region<String, Product> productRegion(ClientCache cache) {

        ClientRegionFactory<String, Product> cRegionFactory = cache.createClientRegionFactory(PROXY);
        return cRegionFactory.create("Product");
    }

    @Bean(name = "orders")
    public Region<Long, Order> orderRegion(ClientCache cache) {
        ClientRegionFactory<Long, Order> cRegionFactory = cache.createClientRegionFactory(PROXY);
        return cRegionFactory.create("Order");
    }
}