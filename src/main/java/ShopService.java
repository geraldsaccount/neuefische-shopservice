import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShopService {
    private final ProductRepo productRepo = new ProductRepo();
    private final OrderRepo orderRepo = new OrderMapRepo();

    public Order addOrder(List<String> productIds) {
        List<Product> products = new ArrayList<>();
        for (String productId : productIds) {
            Product productToOrder = productRepo.getProductById(productId);
            if (productToOrder == null) {
                System.out.println("Product mit der Id: " + productId + " konnte nicht bestellt werden!");
                return null;
            }
            products.add(productToOrder);
        }

        Order newOrder = new Order(UUID.randomUUID().toString(), products, OrderStatus.PROCESSING);

        return orderRepo.addOrder(newOrder);
    }

    public List<Order> getOrdersWithStatus(OrderStatus status) {
        return orderRepo.getOrders().stream().filter(o -> o.status().equals(status)).toList();
    }

    public Order updateOrder(String id, OrderStatus status) {
        Order updatedOrder = orderRepo.getOrderById(id).withStatus(status);

        orderRepo.removeOrder(id);
        return orderRepo.addOrder(updatedOrder);
    }
}
