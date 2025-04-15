import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TransactionHandler {
    public final ShopService shopService;
    public final Map<String, String> aliasMap = new HashMap<>();
    public Map<String, Consumer<List<String>>> commands;

    public void execute(List<String> transactions) {
        commands = Map.of(
                "addOrder", this::addOrder,
                "setStatus", this::setStatus,
                "printOrders", this::printOrders);
        aliasMap.clear();

        transactions.forEach(this::handleCommand);
    }

    public void handleCommand(String line) {
        String[] commandArguments = line.split(" ");
        if (commandArguments.length < 1) {
            return;
        }

        List<String> params = commandArguments.length > 1
                ? Arrays.asList(commandArguments).subList(1, commandArguments.length)
                : Arrays.asList();

        String command = commandArguments[0];

        if (!commands.containsKey(command)) {
            throw new IllegalArgumentException("Could not find command " + command);
        }

        try {
            commands.get(command).accept(params);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void addOrder(List<String> params) {
        if (params == null || params.size() < 2) {
            throw new IllegalArgumentException("addOrder: not enough parameters given. expected >=2");
        }

        String orderAlias = params.get(0);
        if (aliasMap.containsKey(orderAlias)) {
            throw new IllegalStateException("addOrder: order alias already present");
        }

        List<String> productIds = params.subList(1, params.size());
        Order newOrder;
        try {
            newOrder = shopService.addOrder(productIds);

        } catch (ProductNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }

        aliasMap.put(orderAlias, newOrder.id());
    }

    public void setStatus(List<String> params) {
        if (params == null || params.size() != 2) {
            throw new IllegalArgumentException("setStatus: wrong amount of parameters given. expected =2");
        }

        String orderAlias = params.get(0);
        if (!aliasMap.containsKey(orderAlias)) {
            throw new IllegalStateException("setStatus: order alias not set yet");
        }

        try {
            shopService.updateOrder(aliasMap.get(orderAlias), OrderStatus.valueOf(params.get(1)));
        } catch (OrderNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void printOrders(List<String> params) {
        if (params != null && params.size() > 1) {
            throw new IllegalArgumentException("printOrders: too many parameters given. expected 0-1");
        }

        if (params == null || params.isEmpty()) {
            shopService.getOrders().stream()
                    .forEach(o -> {
                        System.out.println(o);
                    });
            return;
        }

        shopService.getOrdersWithStatus(OrderStatus.valueOf(params.get(1))).stream()
                .forEach(o -> {
                    System.out.println(o);
                });
    }
}
