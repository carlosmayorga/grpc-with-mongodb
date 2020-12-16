package com.cmayorga.grpc.blog.client.impl;

import com.cmayorga.grpc.blog.client.BlogClient;
import com.cmayorga.grpc.blog.client.utils.Utils;
import com.proto.blog.*;
import io.grpc.ManagedChannel;

public class BlogClientImpl {

    Utils utils = new Utils();

    public void create (ManagedChannel channel) {

        BlogServiceGrpc.BlogServiceBlockingStub blogClient =
                BlogServiceGrpc.newBlockingStub(channel);

        Blog blog = utils.getBlog(Blog.newBuilder(), "New Blog!", "Carlos", "Hello world this my blog");

        CreateBlogResponse createBlogResponse = blogClient.createBlog(
                CreateBlogRequest.newBuilder()
                        .setBlog(blog)
                        .build());

        System.out.println("Received create blog response");
        System.out.println(createBlogResponse.toString());
    }

    public void read (ManagedChannel channel) {

        BlogServiceGrpc.BlogServiceBlockingStub blogClient =
                BlogServiceGrpc.newBlockingStub(channel);

        Blog blog = utils.getBlog(Blog.newBuilder(), "New Blog!", "Carlos", "Hello world this my blog");

        // First create a new blog into MongoBD
        CreateBlogResponse createBlogResponse = blogClient.createBlog(
                CreateBlogRequest.newBuilder()
                        .setBlog(blog)
                        .build());

        System.out.println("The Blog that already created");
        System.out.println(createBlogResponse.toString());


        String blogId = createBlogResponse.getBlog().getId();
        System.out.println("---> Finding the Blog with _id" + blogId);

        // Second read the already created blog
        ReadBlogResponse readBlogResponse = blogClient
                .readBlog(ReadBlogRequest.newBuilder()
                        .setBlogId(blogId)
                        .build());

        System.out.println(readBlogResponse.toString());
    }

    public void update (ManagedChannel channel) {

        BlogServiceGrpc.BlogServiceBlockingStub blogClient =
                BlogServiceGrpc.newBlockingStub(channel);

        Blog blog = utils.getBlog(Blog.newBuilder(),
                "New Blog!", "Carlos", "Hello world this my blog");

        // First create a new blog into MongoBD
        CreateBlogResponse createBlogResponse = blogClient.createBlog(
                CreateBlogRequest.newBuilder()
                        .setBlog(blog)
                        .build());

        String blogId = createBlogResponse.getBlog().getId();
        System.out.println("---> Finding the Blog with _id" + blogId);

        // Second read the already created blog
        ReadBlogResponse readBlogResponse = blogClient
                .readBlog(ReadBlogRequest.newBuilder()
                        .setBlogId(blogId)
                        .build());

        System.out.println(readBlogResponse.toString());

        // Update the Blog that already created
        Blog newBlog = utils.getBlog(Blog.newBuilder()
                .setId(blogId), "(update) New Blog!", "(update) Changed Author", "(update) Hello world this my blog");

        UpdateBlogResponse updateBlogResponse = blogClient.updateBlog(UpdateBlogRequest
                .newBuilder()
                .setBlog(newBlog)
                .build());

        System.out.println("Update blog");
        System.out.println(updateBlogResponse.toString());

        // Second read the already created blog
        ReadBlogResponse readBlogUpdateResponse = blogClient
                .readBlog(ReadBlogRequest.newBuilder()
                        .setBlogId(blogId)
                        .build());

        System.out.println(readBlogUpdateResponse.toString());
    }

    public void delete (ManagedChannel channel) {

        BlogServiceGrpc.BlogServiceBlockingStub blogClient =
                BlogServiceGrpc.newBlockingStub(channel);

        Blog blog = utils.getBlog(Blog.newBuilder(),
                "New Blog!", "Carlos", "Hello world this my blog");

        // First create a new blog into MongoBD
        CreateBlogResponse createBlogResponse = blogClient.createBlog(
                CreateBlogRequest.newBuilder()
                        .setBlog(blog)
                        .build());
        String blogId = createBlogResponse.getBlog().getId();

        System.out.println("---> Finding the Blog with _id" + blogId);
        // Second read the already created blog
        ReadBlogResponse readBlogResponse = blogClient
                .readBlog(ReadBlogRequest.newBuilder()
                        .setBlogId(blogId)
                        .build());

        System.out.println(readBlogResponse.toString());

        // Update the Blog that already created
        Blog newBlog = utils.getBlog(Blog.newBuilder()
                .setId(blogId), "(update) New Blog!", "(update) Changed Author", "(update) Hello world this my blog");

        UpdateBlogResponse updateBlogResponse = blogClient.updateBlog(UpdateBlogRequest
                .newBuilder()
                .setBlog(newBlog)
                .build());

        System.out.println("Update blog");
        System.out.println(updateBlogResponse.toString());

        // Second read the already created blog
        ReadBlogResponse readBlogUpdateResponse = blogClient
                .readBlog(ReadBlogRequest.newBuilder()
                        .setBlogId(blogId)
                        .build());
        System.out.println(readBlogUpdateResponse.toString());

        System.out.println("Deleting the blog");
        DeleteBlogResponse deleteBlogResponse = blogClient.deleteBlog(
                DeleteBlogRequest.newBuilder()
                        .setBlogId(blogId)
                        .build());

        // Read the already deleted blog
        ReadBlogResponse readBlogDeleteResponse = blogClient
                .readBlog(ReadBlogRequest.newBuilder()
                        .setBlogId(blogId)
                        .build());
        System.out.println(readBlogDeleteResponse.toString());
    }

    public void list (ManagedChannel channel) {

        BlogServiceGrpc.BlogServiceBlockingStub blogClient =
                BlogServiceGrpc.newBlockingStub(channel);

        blogClient.listBlog(ListBlogRequest
                .newBuilder()
                .build()).forEachRemaining(
                        listBlogResponse -> System.out.println(listBlogResponse
                                .getBlog()
                                .toString()));

    }
}
