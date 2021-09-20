import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@EnableScheduling
@Component
public class InventoryPoller {
    private WebClient client = WebClient.create("http://localhost:7654/product");
    private final RedisConnectionFactory connectionFactory;
    private final RedisOperations<String, Product> redisOperations;

    InventoryPoller(RedisConnectionFactory connectionFactory, RedisOperations redisOperations) {
        this.connectionFactory = connectionFactory;
        this.redisOperations = redisOperations;
    }

    @Scheduled(fixedRate = 1000)
    private void pollProducts() {
        connectionFactory.getConnection().serverCommands().flushDb();

        client.get()
                .retrieve()
                .bodyToFlux(Product.class)
                .filter(product -> !product.getLabel().isEmpty())
                .toStream()
                .forEach(p -> redisOperations.opsForValue().set(p.getId() + "", p));


        redisOperations.opsForValue()
                .getOperations()
                .keys("*")
                .forEach(p ->
                        System.out.println(redisOperations.opsForValue().get(p)));
    }
}
