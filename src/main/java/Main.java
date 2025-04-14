
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ProductRepo productRepo = new ProductRepo();
        OrderRepo orderRepo = new OrderListRepo();
        ShopService shopService = new ShopService(productRepo, orderRepo);

        productRepo.addProduct(new Product("P2", "Laptop"));
        productRepo.addProduct(new Product("P3", "Keyboard"));
        productRepo.addProduct(new Product("P4", "Mouse"));

        try {
            shopService.addOrder(List.of("P1", "P2", "P3"));
            shopService.addOrder(List.of("P2", "P2", "P3"));
            shopService.addOrder(List.of("P4", "P3"));
        } catch (ProductNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
