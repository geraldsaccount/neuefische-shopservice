
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ProductRepo productRepo = new ProductRepo();
        OrderRepo orderRepo = new OrderListRepo();
        IdService idService = new UUIDService();
        ShopService shopService = new ShopService(productRepo, orderRepo, idService);

        productRepo.addProduct(new Product("P2", "Peach"));
        productRepo.addProduct(new Product("P3", "Avocado"));
        productRepo.addProduct(new Product("P4", "Honeydew"));

        try {
            shopService.addOrder(List.of("P1", "P2", "P3"));
            shopService.addOrder(List.of("P2", "P2", "P3"));
            shopService.addOrder(List.of("P4", "P3"));
        } catch (ProductNotFoundException e) {
            System.out.println(e.getMessage());
        }
        OrderCommandHandler commandHandler = new OrderCommandHandler(shopService);
        commandHandler.execute("file path here");
    }
}
