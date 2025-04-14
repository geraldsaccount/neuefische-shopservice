import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ShopService {
    private final ProductRepo productRepo = new ProductRepo();
    private final OrderRepo orderRepo = new OrderMapRepo();

    public Order addOrder(List<String> productIds) throws ProductNotFoundException {
        List<Product> products = new ArrayList<>();

        for (String productId : productIds) {
            Optional<Product> productToOrder = productRepo.getProductById(productId);
            if (productToOrder.isEmpty()) {
                throw new ProductNotFoundException("No product with an id of " + productId + " was found.");

            }
            products.add(productToOrder.get());
        }

        Order newOrder = new Order(UUID.randomUUID().toString(), products, OrderStatus.PROCESSING);

        return orderRepo.addOrder(newOrder);
    }

    public List<Order> getOrdersWithStatus(OrderStatus status) {
        return orderRepo.getOrders().stream().filter(o -> o.status().equals(status)).toList();
    }
}
