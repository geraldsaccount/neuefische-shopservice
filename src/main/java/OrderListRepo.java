import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderListRepo implements OrderRepo {
    private final List<Order> orders = new ArrayList<>();

    @Override
    public List<Order> getOrders() {
        return orders;
    }

    @Override
    public Optional<Order> getOrderById(String id) {
        return orders.stream().filter(o -> id.equals(o.id())).findFirst();
    }

    @Override
    public Order addOrder(Order newOrder) {
        orders.add(newOrder);
        return newOrder;
    }

    @Override
    public void removeOrder(String id) {
        for (Order order : orders) {
            if (order.id().equals(id)) {
                orders.remove(order);
                return;
            }
        }
    }
}
