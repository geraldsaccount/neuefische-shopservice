import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderListRepoTest {
    OrderRepo repo;
    Product product;
    Order order;

    @BeforeEach
    void setUp() {
        repo = new OrderListRepo();
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
        // WHEN
        repo.removeOrder("O1");
        Order actual = repo.addOrder(order);

        // THEN
        assertThat(actual)
                .isEqualTo(order);
        assertThat(repo.getOrderById("O1"))
                .containsSame(order);

    }

    @Test
    void removeOrder() {
        // WHEN
        repo.removeOrder("O1");

        // THEN
        assertThat(repo.getOrderById("O1")).isEmpty();
    }
}
