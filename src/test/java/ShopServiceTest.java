import java.util.ArrayList;
import java.util.List;

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

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() throws ProductNotFoundException {
        service = new ShopService();
        ids = new ArrayList<>();
        ids.add("1");
        order = service.addOrder(ids);
    }

    @Test
    void addOrder_returnsAddedOrder_givenValidIds() throws ProductNotFoundException {
        Order actual = service.addOrder(ids);
        Order expected = new Order("-1", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING);
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
        ShopService shopService = new ShopService();
        assertThat(shopService.getOrdersWithStatus(OrderStatus.PROCESSING)).isEmpty();

    }
}
