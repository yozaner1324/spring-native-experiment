package org.example;

import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.EnableClusterConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;

@RestController
@EnableClusterConfiguration(useHttp = true, requireHttps = false)
@SpringBootApplication
public class MyApplication {

    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
        long currentTime = System.currentTimeMillis();
        long vmStartTime = ManagementFactory.getRuntimeMXBean().getStartTime();
        System.out.println("Startup time: " + (currentTime - vmStartTime) + "ms");
    }

    @Bean
    ApplicationRunner runner(ProductRepo productRepo) {
        return args -> {
            Product chair = new Product("chair", 49.99f, 7);
            productRepo.save(chair);

            System.out.println(productRepo.findById("chair"));
        };
    }

    @Bean
    public ClientRegionFactoryBean<String, Product> productsRegionFactoryBean(GemFireCache cache) {
        ClientRegionFactoryBean<String, Product> clientRegionFactoryBean = new ClientRegionFactoryBean<>();
        clientRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
        clientRegionFactoryBean.setShortcut(ClientRegionShortcut.PROXY);
        clientRegionFactoryBean.setRegionName("Product");
        clientRegionFactoryBean.setCache(cache);
        return clientRegionFactoryBean;
    }
}