package com.cmayorga.grpc.blog.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class BlogServer {

    public static void main(String[] arg) throws IOException, InterruptedException {
        System.out.println("Hello from GoogleRPC Server");

        Server server = ServerBuilder
                .forPort(50051)
                .addService(new BlogServerImpl())
                .build();

        server.start();

        Runtime.getRuntime()
                .addShutdownHook(new Thread(() -> {
                    System.out.println("Received Shutdown Request");
                    server.shutdown();
                    System.out.println("Successfully stopped the server");
                }));


        server.awaitTermination();
    }
}
