import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class ProductRepoTest {

    @Test
    void getProducts() {
        // GIVEN
        ProductRepo repo = new ProductRepo();

        // WHEN
        List<Product> actual = repo.getProducts();

        // THEN
        List<Product> expected = new ArrayList<>();
        expected.add(new Product("P1", "Apple"));
        assertEquals(actual, expected);
    }

    @Test
    void getProductById_returnsProduct_withValidId() {
        // GIVEN
        ProductRepo repo = new ProductRepo();

        // WHEN
        Optional<Product> actual = repo.getProductById("P1");

        // THEN
        Product expected = new Product("P1", "Apple");
        assertThat(actual).isNotEmpty().contains(expected);
    }

    @Test
    void getProductById_returnsEmpty_withInvalidId() {
        ProductRepo repo = new ProductRepo();
        Optional<Product> actual = repo.getProductById("P2");
        assertThat(actual).isEmpty();
    }

    @Test
    void addProduct() {
        // GIVEN
        ProductRepo repo = new ProductRepo();
        Product newProduct = new Product("P2", "Banana");

        // WHEN
        Product actual = repo.addProduct(newProduct);

        // THEN
        Product expected = new Product("P2", "Banana");
        assertEquals(actual, expected);
        assertEquals(repo.getProductById("P2").get(), expected);
    }

    @Test
    void removeProduct() {
        // GIVEN
        ProductRepo repo = new ProductRepo();

        // WHEN
        repo.removeProduct("P1");

        // THEN
        assertThat(repo.getProductById("P1")).isEmpty();
    }
}
