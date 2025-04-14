import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

class ShopServiceTest {

    @Test
    void addOrderTest() {
        // GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1");

        // WHEN
        Order actual = shopService.addOrder(productsIds);

        // THEN
        Order expected = new Order("-1", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING);
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());
    }

    @Test
    void addOrderTest_whenInvalidProductId_expectNull() {
        // GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1", "2");

        // WHEN
        Order actual = shopService.addOrder(productsIds);

        // THEN
        assertNull(actual);
    }

    @Test
    void getOrdersWithStatus_returnsMatchingProducts_whenStatusIsPresent() {
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1");

        Order order1 = shopService.addOrder(productsIds);
        Order order2 = shopService.addOrder(productsIds);

        assertThat(shopService.getOrdersWithStatus(OrderStatus.PROCESSING))
                .containsExactlyInAnyOrder(order1, order2);
    }

    @Test
    void getOrderWithStatus_returnsEmptyList_whenStatusIsNotPresent() {
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1");

        shopService.addOrder(productsIds);

        assertThat(shopService.getOrdersWithStatus(OrderStatus.COMPLETED)).isEmpty();

    }

    @Test
    void getOrderWithStatus_returnsEmptyList_whenNoOrdersPresent() {
        ShopService shopService = new ShopService();
        assertThat(shopService.getOrdersWithStatus(OrderStatus.PROCESSING)).isEmpty();

    }
}
