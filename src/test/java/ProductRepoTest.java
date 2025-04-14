import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
        expected.add(new Product("1", "Apfel"));
        assertEquals(actual, expected);
    }

    @Test
    void getProductById_returnsProduct_withValidId() {
        // GIVEN
        ProductRepo repo = new ProductRepo();

        // WHEN
        Optional<Product> actual = repo.getProductById("1");

        // THEN
        Product expected = new Product("1", "Apfel");
        assertThat(actual).isNotEmpty().contains(expected);
    }

    @Test
    void getProductById_returnsEmpty_withInvalidId() {
        ProductRepo repo = new ProductRepo();
        Optional<Product> actual = repo.getProductById("2");
        assertThat(actual).isEmpty();
    }

    @Test
    void addProduct() {
        // GIVEN
        ProductRepo repo = new ProductRepo();
        Product newProduct = new Product("2", "Banane");

        // WHEN
        Product actual = repo.addProduct(newProduct);

        // THEN
        Product expected = new Product("2", "Banane");
        assertEquals(actual, expected);
        assertEquals(repo.getProductById("2"), expected);
    }

    @Test
    void removeProduct() {
        // GIVEN
        ProductRepo repo = new ProductRepo();

        // WHEN
        repo.removeProduct("1");

        // THEN
        assertNull(repo.getProductById("1"));
    }
}
