package com.learnjava.completablefuture;

import com.learnjava.domain.*;
import com.learnjava.service.InventoryService;
import com.learnjava.service.ProductInfoService;
import com.learnjava.service.ReviewService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.learnjava.util.LoggerUtil.log;

/**
 * @author csgear
 */
public class ProductServiceUsingCompletableFuture {
    private final ProductInfoService productInfoService;
    private final ReviewService reviewService;
    private InventoryService inventoryService ;

    public ProductServiceUsingCompletableFuture(ProductInfoService productInfoService, ReviewService reviewService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
    }

    public ProductServiceUsingCompletableFuture(ProductInfoService productInfoService, ReviewService reviewService, InventoryService inventoryService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
        this.inventoryService = inventoryService;
    }

    public Product retrieveProductDetails(String productId) {
        CompletableFuture<ProductInfo> productInfoCompletableFuture
                = CompletableFuture.supplyAsync(() -> productInfoService.retrieveProductInfo(productId));

        CompletableFuture<Review> reviewCompletableFuture
                = CompletableFuture.supplyAsync(() -> reviewService.retrieveReviews(productId));

        return productInfoCompletableFuture
                .thenCombine(
                        reviewCompletableFuture, (productInfo, review) ->
                                new Product(productId, productInfo, review))
                .join();
    }

    public Product retrieveProductDetailsWithInventory(String productId) {
        CompletableFuture<ProductInfo>  productInfoCompletableFuture =
                CompletableFuture.supplyAsync( () -> productInfoService.retrieveProductInfo(productId))
                        .thenApply((productInfo -> {
                            productInfo.setProductOptions(updateInventoryToProductOption(productInfo));
                            return productInfo ;
                        })) ;

        CompletableFuture<Review> reviewCompletableFuture =
                CompletableFuture.supplyAsync( () -> reviewService.retrieveReviews(productId)) ;

        return productInfoCompletableFuture.thenCombine(
                reviewCompletableFuture, (productInfo, review) ->
                        new Product(productId, productInfo, review)
        ).join();
    }

    public Product retrieveProductDetailsWithInventoryApproach2(String productId) {
        CompletableFuture<ProductInfo> productInfoCompletableFuture =
                CompletableFuture.supplyAsync(() -> productInfoService.retrieveProductInfo(productId))
                        .thenApply(productInfo -> {
                            productInfo.setProductOptions(updateInventoryToProductOptionApproach2(productInfo));
                            return productInfo ;
                }) ;

        CompletableFuture<Review> reviewCompletableFuture =
                CompletableFuture.supplyAsync(() -> reviewService.retrieveReviews(productId))
                        .exceptionally(ex -> {
                            log("Handled the Exception in review Service : " + ex.getMessage());
                            return Review.builder()
                                    .noOfReviews(0)
                                    .overallRating(0.0)
                                    .build() ;
                        }) ;

        return productInfoCompletableFuture.thenCombine( reviewCompletableFuture,
                (productInfo, review) -> new Product(productId, productInfo, review))
                .whenComplete((prod, ex) -> {
                    log("Inside whenComplete : " + prod + "and the exception is " + ex);
                    if (ex != null) {
                        log("Exception in whenComplete is : " + ex);
                    }
                }).join();
    }

    private List<ProductOption> updateInventoryToProductOptionApproach2(ProductInfo productInfo) {
        List<CompletableFuture<ProductOption>> productOptionList =
                productInfo.getProductOptions()
                .stream()
                .map(productOption -> CompletableFuture.supplyAsync(() -> inventoryService.retrieveInventory(productOption))
                        .exceptionally((ex) -> {
                            log("Exception in Inventory Service : " + ex.getMessage());
                            return Inventory.builder()
                                    .count(1).build();
                        })
                        .thenApply((inventory -> {
                            productOption.setInventory(inventory);
                            return productOption;
                        }))).collect(Collectors.toList()) ;

        return productOptionList.stream().map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    private List<ProductOption> updateInventoryToProductOption(ProductInfo productInfo) {
        return productInfo.getProductOptions()
                .stream()
                .peek(productOption -> {
                    Inventory inventory = inventoryService.retrieveInventory(productOption) ;
                    productOption.setInventory(inventory);
                }).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        ProductInfoService productInfoService = new ProductInfoService();
        ReviewService reviewService = new ReviewService();
        ProductServiceUsingCompletableFuture productService = new ProductServiceUsingCompletableFuture(productInfoService, reviewService);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is " + product);
    }
}
