import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderMapRepoTest {
    OrderRepo repo;
    Product product;
    Order order;

    @BeforeEach
    void setUp() {
        repo = new OrderMapRepo();
        product = new Product("P1", "Apple");
        order = new Order("O1", List.of(product), OrderStatus.PROCESSING, Instant.now());
        repo.addOrder(order);
    }

    @Test
    void getOrders() {
        assertThat(repo.getOrders())
                .containsExactly(order);
    }

    @Test
    void getOrderById() {
        assertThat(repo.getOrderById("O1"))
                .contains(order);
    }

    @Test
    void addOrder() {
        // THEN
        repo.removeOrder("O1");
        Order actual = repo.addOrder(order);

        assertThat(actual)
                .isEqualTo(order);
        assertThat(repo.getOrderById("O1"))
                .containsSame(order);
    }

    @Test
    void removeOrder() {
        repo.removeOrder("O1");

        // THEN
        assertThat(repo.getOrderById("O1"))
                .isEmpty();
    }
}
