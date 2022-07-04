package com.learnjava.completablefuture;

import com.learnjava.domain.Product;
import com.learnjava.service.InventoryService;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ReviewService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceUsingCompletableFutureTest {


    ProductInfoService pis = new ProductInfoService();
    ReviewService rs = new ReviewService();
    InventoryService is = new InventoryService();

    ProductServiceUsingCompletableFuture pscf = new ProductServiceUsingCompletableFuture(pis, rs, is);

    @Test
    void retrieveProductDetailsWithInventory() {
        String productId = "ABC123";

        Product product = pscf.retrieveProductDetailsWithInventory(productId);

        assertNotNull(product) ;
        assertTrue(product.getProductInfo().getProductOptions().size() > 0);
        product.getProductInfo().getProductOptions().forEach(
                productOption -> assertNotNull(productOption.getInventory())
        );
    }

    @Test
    void retrieveProductDetailsWithInventoryApproach2() {
        String productId = "ABC123";

        Product product = pscf.retrieveProductDetailsWithInventoryApproach2(productId);

        assertNotNull(product) ;
        assertTrue(product.getProductInfo().getProductOptions().size() > 0);
        product.getProductInfo().getProductOptions().forEach(
                productOption -> assertNotNull(productOption.getInventory())
        );
    }
}