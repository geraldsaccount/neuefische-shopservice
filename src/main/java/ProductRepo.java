import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepo {
    private final List<Product> products;

    public ProductRepo() {
        products = new ArrayList<>();
        products.add(new Product("P1", "Apple"));
    }

    public List<Product> getProducts() {
        return products;
    }

    public Optional<Product> getProductById(String id) {
        return products.stream().filter(p -> id.equals(p.id())).findFirst();
    }

    public Product addProduct(Product newProduct) {
        products.add(newProduct);
        return newProduct;
    }

    public void removeProduct(String id) {
        for (Product product : products) {
            if (product.id().equals(id)) {
                products.remove(product);
                return;
            }
        }
    }
}
