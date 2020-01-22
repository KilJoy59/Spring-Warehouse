package main.controller;

import main.entity.Product;
import main.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/products")
    public ResponseEntity addProduct(@Valid @RequestBody Product product) {
        productService.addProduct(product);
        return new ResponseEntity(product, HttpStatus.OK);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity getProduct(@PathVariable int id) {
        Product product = productService.getProduct(id);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity(product, HttpStatus.OK);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity editProduct(@Valid @RequestBody Product product, @PathVariable int id) {
        Product newProduct = productService.editProduct(id, product);
        return newProduct == null ?
                new ResponseEntity(null, HttpStatus.NOT_FOUND) : new ResponseEntity(newProduct, HttpStatus.OK);
    }

    @GetMapping(value = "/products/")
    public ResponseEntity getAllProducts() {
        Iterable<Product> productIterable = productService.getAllProducts();
        List<Product> products = new ArrayList<>();
        for (Product product : productIterable) {
            products.add(product);
        }
        return new ResponseEntity(products, HttpStatus.OK);
    }
}
