package com.learnjava.completablefuture;

import com.learnjava.service.HelloWorldService;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;

/**
 * @author csgear
 */
@RequiredArgsConstructor
public class CompletableFutureHelloWorld {
    private final HelloWorldService hws ;

    public CompletableFuture<String> helloWorld() {
        return CompletableFuture.supplyAsync(hws::hello) ;
    }
}
