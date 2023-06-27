package org.example;

import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.query.CqEvent;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableClusterConfiguration;
import org.springframework.data.gemfire.config.annotation.EnableContinuousQueries;
import org.springframework.data.gemfire.listener.annotation.ContinuousQuery;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

import java.lang.management.ManagementFactory;

@EnableClusterConfiguration(useHttp = true, requireHttps = false)
@SpringBootApplication
@ClientCacheApplication(readyForEvents = true, subscriptionEnabled = true)
@EnableGemfireRepositories
@EnableContinuousQueries
public class MyApplication {

    public static void main(String[] args) {
        long currentTime = System.currentTimeMillis();
        long vmStartTime = ManagementFactory.getRuntimeMXBean().getStartTime();
        System.out.println("Startup time: " + (currentTime - vmStartTime) + "ms");
        SpringApplication.run(MyApplication.class, args);
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
    public ClientRegionFactoryBean<String, Product> productRegionFactoryBean(GemFireCache cache) {
        ClientRegionFactoryBean<String, Product> clientRegionFactoryBean = new ClientRegionFactoryBean<>();
        clientRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
        clientRegionFactoryBean.setShortcut(ClientRegionShortcut.PROXY);
        clientRegionFactoryBean.setRegionName("Product");
        clientRegionFactoryBean.setCache(cache);
        return clientRegionFactoryBean;
    }

    @Bean
    public ClientRegionFactoryBean<String, Order> orderRegionFactoryBean(GemFireCache cache) {
        ClientRegionFactoryBean<String, Order> clientRegionFactoryBean = new ClientRegionFactoryBean<>();
        clientRegionFactoryBean.setDataPolicy(DataPolicy.REPLICATE);
        clientRegionFactoryBean.setShortcut(ClientRegionShortcut.PROXY);
        clientRegionFactoryBean.setRegionName("Order");
        clientRegionFactoryBean.setCache(cache);
        return clientRegionFactoryBean;
    }

    @ContinuousQuery(name = "ProductCQ", query = "SELECT * FROM /Product where name like 'a%'")
    public void handleEvent(CqEvent event) {
        System.out.println("Event: " + event);
    }
}