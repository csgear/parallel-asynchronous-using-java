package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class CompletableFutureHelloWorldTest {
    HelloWorldService hws ;
    CompletableFutureHelloWorld cfhw ;

    @BeforeEach
    void setup() {
         hws = new HelloWorldService();
         cfhw = new CompletableFutureHelloWorld(hws);
    }
    @Test
    void helloWorld() {
        CompletableFuture<String> completableFuture = cfhw.helloWorld() ;

        completableFuture
                .thenAccept(s -> assertEquals("hello", s))
                .join();
    }

    @Test
    void supplyAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<String>  future = CompletableFuture.supplyAsync(
                () -> {
                    try {
                        TimeUnit.SECONDS.sleep(1) ;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return "Result of the asynchronous computation";
                }
        ) ;

        String result = future.get() ;
        assertEquals(result, "Result of the asynchronous computation");
    }

    @Test
    void supplyAsyncWithThreadPool() throws ExecutionException, InterruptedException {
        Executor executor = Executors.newFixedThreadPool(10);
        CompletableFuture<String>  future = CompletableFuture.supplyAsync(
                () -> {
                    try {
                        TimeUnit.SECONDS.sleep(1) ;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return "Result of the asynchronous computation";
                }, executor
        ) ;

        String result = future.get() ;
        assertEquals(result, "Result of the asynchronous computation");
    }

    @Test
    void anyOf() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return "Result of Future 1";
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return "Result of Future 2";
        });

        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return "Result of Future 3";
        });

        CompletableFuture<Object> anyOfFuture = CompletableFuture.anyOf(future1, future2, future3) ;

        String expected = "Result of Future 2" ;

        assertEquals(expected, anyOfFuture.get());
    }
}