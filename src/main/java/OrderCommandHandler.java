import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderCommandHandler {
    public final ShopService shopService;
    public final Map<String, String> aliasMap = new HashMap<>();
    public Map<String, Function<List<String>, Optional<String>>> commands;

    public void execute(String filePath) {
        commands = Map.of(
                "addOrder", this::addOrder,
                "setStatus", this::setStatus,
                "printOrders", this::printOrder);

        try {
            List<String> lines = Files.readAllLines(Path.of(filePath));
            lines.forEach(this::handleCommand);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
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
            System.out.println("Could not find command " + command);
            return;
        }

        Optional<String> errorMessage = commands.get(command).apply(params);
        if (errorMessage.isPresent()) {
            System.out.println(errorMessage);
        }
    }

    public Optional<String> addOrder(List<String> params) {
        if (params == null || params.size() < 2) {
            return Optional.of("Not enough parameters for command addOrder.");
        }

        String orderAlias = params.get(0);
        if (aliasMap.containsKey(orderAlias)) {
            return Optional.of("Command addOrder cannot add multiple orders with same alias.");
        }

        List<String> productIds = params.subList(1, params.size());
        Order newOrder;
        try {
            newOrder = shopService.addOrder(productIds);

        } catch (ProductNotFoundException e) {
            return Optional.of(e.getMessage());
        }

        aliasMap.put(orderAlias, newOrder.id());

        return Optional.empty();
    }

    public Optional<String> setStatus(List<String> params) {
        if (params == null || params.size() != 2) {
            return Optional.of("Not correct amount of parameters for setStatus.");
        }

        String orderAlias = params.get(0);
        if (!aliasMap.containsKey(orderAlias)) {
            return Optional
                    .of("Command setStatus cannot set the status of an order whos alias has not been ordered yet.");
        }

        String newStatus = params.get(1);
        OrderStatus orderStatus;
        switch (newStatus) {
            case "PROCESSING" -> orderStatus = OrderStatus.PROCESSING;
            case "IN_DELIVERY" -> orderStatus = OrderStatus.IN_DELIVERY;
            case "COMPLETED" -> orderStatus = OrderStatus.COMPLETED;
            default -> {
                return Optional.of("Unexpected order status was entered.");
            }
        }

        try {
            shopService.updateOrder(aliasMap.get(orderAlias), orderStatus);
        } catch (OrderNotFoundException e) {
            return Optional.of(e.getMessage());
        }

        return Optional.empty();
    }

    public Optional<String> printOrder(List<String> params) {
        if (params != null && params.size() > 1) {
            return Optional.of("Too many parameters were given for command printOrders.");
        }

        if (params == null || params.isEmpty()) {
            shopService.getOrders().stream()
                    .forEach(o -> {
                        System.out.println(o);
                    });
            return Optional.empty();
        }

        OrderStatus status;

        switch (params.get(0)) {
            case "PROCESSING" -> status = OrderStatus.PROCESSING;
            case "IN_DELIVERY" -> status = OrderStatus.IN_DELIVERY;
            case "COMPLETED" -> status = OrderStatus.COMPLETED;
            default -> {
                return Optional.of("Unexpected parameter for command printOrders.");
            }
        }
        shopService.getOrdersWithStatus(status).stream()
                .forEach(o -> {
                    System.out.println(o);
                });

        return Optional.empty();
    }
}
