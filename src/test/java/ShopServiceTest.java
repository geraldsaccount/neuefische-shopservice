import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShopServiceTest {
    ShopService service;
    List<String> ids;
    Order order;
    OrderRepo orderRepo;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() throws ProductNotFoundException {
        orderRepo = new OrderListRepo();
        service = new ShopService(new ProductRepo(), orderRepo, new UUIDService());
        ids = new ArrayList<>();
        ids.add("P1");
        order = service.addOrder(ids);
    }

    @Test
    void addOrder_returnsAddedOrder_givenValidIds() throws ProductNotFoundException {
        Order actual = service.addOrder(ids);
        Order expected = new Order("-1", List.of(new Product("P1", "Apple")), OrderStatus.PROCESSING, Instant.now());
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());
    }

    @Test
    void addOrderTest_throwsProductNotFoundException_withInvalidId() {
        String invalidId = "2";
        ids.add(invalidId);

        assertThatThrownBy(() -> {
            service.addOrder(ids);
        }).isInstanceOf(ProductNotFoundException.class)
                .hasMessage("No product with an id of " + invalidId + " was found.");
    }

    @Test
    void getOrdersWithStatus_returnsMatchingProducts_whenStatusIsPresent() throws ProductNotFoundException {
        Order order1 = service.addOrder(ids);

        assertThat(service.getOrdersWithStatus(OrderStatus.PROCESSING))
                .containsExactlyInAnyOrder(order1, order);
    }

    @Test
    void getOrderWithStatus_returnsEmptyList_whenStatusIsNotPresent() {
        assertThat(service.getOrdersWithStatus(OrderStatus.COMPLETED)).isEmpty();
    }

    @Test
    void getOrderWithStatus_returnsEmptyList_whenNoOrdersPresent() {
        service = new ShopService(new ProductRepo(), new OrderListRepo(), new UUIDService());
        assertThat(service.getOrdersWithStatus(OrderStatus.PROCESSING)).isEmpty();

    }

    @Test
    void getOldestPerStatus_returnsCorrectOrders_whenOrdersMade() {
        Order oldProcessingOrder = order.withTimestamp(order.timestamp().minus(Duration.ofMinutes(10)));
        Order oldCompletedOrder = order.withStatus(OrderStatus.COMPLETED);

        // Circumvent ShopService due to it overriding the timestamp
        orderRepo.addOrder(oldProcessingOrder);
        orderRepo.addOrder(oldCompletedOrder);

        assertThat(service.getOldestPerStatus())
                .containsExactlyEntriesOf(Map.of(
                        OrderStatus.PROCESSING, oldProcessingOrder,
                        OrderStatus.COMPLETED, oldCompletedOrder));
    }

    @Test
    void getOldestPerStatus_returnsEmpty_whenNoOrders() {
        service = new ShopService(new ProductRepo(), new OrderListRepo(), new UUIDService());
        assertThat(service.getOldestPerStatus()).isEmpty();
    }

    @Test
    void updateOrder_returnsUpdatedOrder_whenValidId() throws OrderNotFoundException {
        Order updatedOrder = service.updateOrder(order.id(), OrderStatus.COMPLETED);

        assertThat(updatedOrder.status()).isEqualTo(OrderStatus.COMPLETED);
    }

    @Test
    void updatedOrder_doesSomethingLetsSee_whenInvalidID() {
        String invalidId = "invalid id";
        assertThatThrownBy(() -> {
            service.updateOrder(invalidId, OrderStatus.COMPLETED);
        })
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessage("No order with an id of " + invalidId + " was found.");
    }

}
