package com.cmayorga.grpc.blog.client;

import com.cmayorga.grpc.blog.client.impl.BlogClientImpl;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BlogClient {
    ManagedChannel channel;

    public static void main(String[] args) {
        System.out.println("Hello from GoogleRPC Client");
        new BlogClient().run();
        System.out.println("Creating Stub");
    }

    private void run() {
        channel = ManagedChannelBuilder
                .forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        new BlogClientImpl().list(channel);

        System.out.println("Shutting down channel");
        channel.shutdown();
    }

}

