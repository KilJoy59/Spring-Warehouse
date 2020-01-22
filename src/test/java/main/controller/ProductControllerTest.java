package main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.entity.Product;
import main.repository.ProductRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Project TestWork
 * Created by End on янв., 2020
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    private static final ObjectMapper om = new ObjectMapper();
    private static Product product;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ProductRepository productRepository;

    @Before
    public void init() {
        product = new Product();
        product.setId(1);
        product.setName("notebook");
        product.setShortTitle("lenovo");
        product.setSpecifications("black");
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productRepository.findById(product.getId())).thenReturn(java.util.Optional.of(product));
        doNothing().when(productRepository).deleteById(product.getId());
    }


    @Test
    public void addProductProductControllerTest() throws Exception {
        mockMvc.perform(post("/products/")
                .content(om.writeValueAsString(product))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("notebook")));
        Mockito.verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void getProductProductControllerTest() throws Exception {
        mockMvc.perform(get("/products/{id}", 1)
                .content(om.writeValueAsString(product)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)));

        verify(productRepository, times(1)).findById(product.getId());
    }

    @Test
    public void deleteProductControllerTest() throws Exception {
        mockMvc.perform(delete("/products/{id}", 1)
                .content(om.writeValueAsString(product)))
                .andDo(print())
                .andExpect(status().isNoContent());
        verify(productRepository, times(1)).deleteById(1);
    }

    @Test
    public void editProductControllerTest() throws Exception {
        Product newProduct = new Product();
        newProduct.setId(1);
        newProduct.setName("tablePc");
        newProduct.setShortTitle("Microsoft");
        newProduct.setSpecifications("white");
        when(productRepository.save(any(Product.class))).thenReturn(newProduct);
        mockMvc.perform(put("/products/{id}", 1)
                .content(om.writeValueAsString(newProduct))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.name", Matchers.is("tablePc")));
        verify(productRepository, times(1)).findById(newProduct.getId());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void getAllProductControllerTest() throws Exception {
        List<Product> products = Arrays.asList(
                new Product(1, "pen", "parker", "black"),
                new Product(2, "pencil", "any", "white"));
        when(productRepository.findAll()).thenReturn(products);

        mockMvc.perform(get("/products/"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[1].id", Matchers.is(2)));
        verify(productRepository, times(1)).findAll();
    }
}
