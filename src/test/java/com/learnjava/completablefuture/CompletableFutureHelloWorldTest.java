package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

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

}