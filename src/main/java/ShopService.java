import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ShopService {
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final IdService idService;

    public Order addOrder(List<String> productIds) throws ProductNotFoundException {
        List<Product> products = new ArrayList<>();

        for (String productId : productIds) {
            Optional<Product> productToOrder = productRepo.getProductById(productId);
            if (productToOrder.isEmpty()) {
                throw new ProductNotFoundException("No product with an id of " + productId + " was found.");

            }
            products.add(productToOrder.get());
        }

        Order newOrder = new Order(idService.generateId().toString(), products, OrderStatus.PROCESSING, Instant.now());

        return orderRepo.addOrder(newOrder);
    }

    public List<Order> getOrdersWithStatus(OrderStatus status) {
        return orderRepo.getOrders().stream().filter(o -> o.status().equals(status)).toList();
    }

    public Order updateOrder(String orderId, OrderStatus status) throws OrderNotFoundException {
        Order updatedOrder = orderRepo.getOrderById(orderId)
                .orElseThrow(() -> {
                    return new OrderNotFoundException("No order with an id of " + orderId + " was found.");
                })
                .withStatus(status);

        orderRepo.removeOrder(orderId);
        return orderRepo.addOrder(updatedOrder);
    }
}
