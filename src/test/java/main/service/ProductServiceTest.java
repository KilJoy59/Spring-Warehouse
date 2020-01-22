package main.service;

import main.entity.Product;
import main.repository.ProductRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ProductServiceTest {

    private static Product product;

    @MockBean
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Before
    public void init() {
        product = new Product();
        product.setName("notebook");
        product.setId(1);
        Mockito.when(productRepository.save(product)).thenReturn(product);
    }

    @Test
    public void add() {
        productService.addProduct(product);
        Assert.assertNotNull(product);
        Assert.assertNotNull(product.getId());
        String expected = product.getName();
        Assert.assertEquals(expected, "notebook");
    }


    @Test
    public void delete() {
        productService.addProduct(product);
        Mockito.when(productRepository.findById(product.getId())).thenReturn(java.util.Optional.of(product));
        Mockito.doNothing().when(productRepository).deleteById(product.getId());
        productService.deleteProduct(product.getId());
        Mockito.verify(productRepository, Mockito.times(1)).findById(product.getId());
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(product.getId());
    }

    @Test
    public void edit() {
        productService.addProduct(product);
        Mockito.when(productRepository.findById(product.getId())).thenReturn(java.util.Optional.of(product));
        Product newProduct = new Product();
        newProduct.setName("Table");
        productService.editProduct(product.getId(), newProduct);
        Assert.assertEquals(product.getName(), "Table");
    }

    @Test
    public void getAllProducts() {
        Product product = new Product();
        product.setName("first");
        Product product2 = new Product();
        product2.setName("second");
        List<Product> expected = new ArrayList<>();
        expected.add(product);
        expected.add(product2);
        Mockito.when(productRepository.findAll()).thenReturn(expected);
        List<Product> actual = productService.getAllProducts();
        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
        Mockito.verify(productRepository, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(productRepository);
        Assert.assertEquals(expected.get(0).getName(), actual.get(0).getName());
    }

    @Test
    public void get() {
        Mockito.when(productRepository.findById(product.getId())).thenReturn(java.util.Optional.of(product));
        productService.getProduct(product.getId());
        Mockito.verify(productRepository, Mockito.times(1)).findById(product.getId());
        Mockito.verifyNoMoreInteractions(productRepository);
        Assert.assertNotNull(product);
        Assert.assertEquals(product.getId(), 1);
    }
}