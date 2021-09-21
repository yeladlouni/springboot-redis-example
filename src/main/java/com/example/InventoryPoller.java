package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.repository.support.RedisRepositoryFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@EnableScheduling
@Component
public class InventoryPoller {
    private WebClient client = WebClient.create("http://localhost:7654/products");
    private final RedisConnectionFactory connectionFactory;
    private final ProductRepository repository;

    InventoryPoller(RedisConnectionFactory connectionFactory, ProductRepository repository) {
        this.connectionFactory = connectionFactory;
        this.repository = repository;
    }

    @Scheduled(fixedRate = 1000)
    private void pollProducts() {
        //connectionFactory.getConnection().serverCommands().flushDb();

        client.get()
                .retrieve()
                .bodyToFlux(Product.class)
                .filter(product -> !product.getLabel().isEmpty())
                .toStream()
                .forEach(repository::save);


        repository.findAll().forEach(System.out::println);
    }
}
