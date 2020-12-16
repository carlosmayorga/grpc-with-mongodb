package com.cmayorga.grpc.blog.server;

import com.cmayorga.grpc.blog.server.utils.Utils;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.proto.blog.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class BlogServerImpl extends BlogServiceGrpc.BlogServiceImplBase {

    private MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    private MongoDatabase database = mongoClient.getDatabase("grpc_blog_db");
    private MongoCollection<Document> collection = database.getCollection("blog");

    Utils utils = new Utils();

    @Override
    public void createBlog(CreateBlogRequest request, StreamObserver<CreateBlogResponse> responseObserver) {

        Blog blog = request.getBlog();

        Document doc = new Document("author_id",
                blog.getAuthorId())
                .append("title", blog.getTitle())
                .append("content", blog.getContent());
        // Create new document in MongoDB
        collection.insertOne(doc);

        String id = doc.getObjectId("_id").toString();

        CreateBlogResponse response = CreateBlogResponse.newBuilder()
                .setBlog(Blog.newBuilder()
                        .setAuthorId(blog.getAuthorId())
                        .setContent(blog.getContent())
                        .setTitle(blog.getTitle())
                        .setId(id))
                .build();

        responseObserver.onNext(response);

        responseObserver.onCompleted();
    }

    @Override
    public void readBlog(ReadBlogRequest request, StreamObserver<ReadBlogResponse> responseObserver) {

        String blogId = request.getBlogId();

        Document result = null;

        try {
            result = collection.find(eq("_id",
                    new ObjectId(blogId)))
                    .first();
        }catch (Exception e) {
            responseObserver.onError(
                    Status.NOT_FOUND.withDescription("The blog with corresponding id was not found")
                            .asRuntimeException());
        }

        if(result == null) {
            responseObserver.onError(
                    Status.NOT_FOUND.withDescription("The blog with corresponding id was not found")
                    .asRuntimeException()
            );
        } else {
            Blog blog = utils.documentToBlog(result);

            responseObserver.onNext(ReadBlogResponse
                    .newBuilder()
                    .setBlog(blog)
                    .build());

            responseObserver.onCompleted();
        }
    }

    @Override
    public void updateBlog(UpdateBlogRequest request, StreamObserver<UpdateBlogResponse> responseObserver) {

        Blog blog = request.getBlog();

        String blogId = blog.getId();

        Document result = collection
                .find(eq("_id", new ObjectId(blogId)))
                .first();

        if(result == null) {
            responseObserver.onError(
                    Status.NOT_FOUND.withDescription("The blog with corresponding id was not found")
                            .asRuntimeException());
        } else {
            Document blogUpdate = new Document("author_id", blog.getAuthorId())
                    .append("title", blog.getTitle())
                    .append("content", blog.getContent());

            collection.replaceOne(eq("_id", result.getObjectId("_id"))
                    , blogUpdate);

            // Return the response to client
            responseObserver.onNext(
                    UpdateBlogResponse.newBuilder()
                    .setBlog(utils.documentToBlog(result))
                    .build()
            );

            responseObserver.onCompleted();
        }

    }

    @Override
    public void deleteBlog(DeleteBlogRequest request, StreamObserver<DeleteBlogResponse> responseObserver) {

        String blogId = request.getBlogId();
        DeleteResult result = null;
        try {
            result = collection.deleteOne(eq("_id",new ObjectId(blogId)));
        } catch (Exception e){
            responseObserver.onError(
                    Status.NOT_FOUND.withDescription("The blog with corresponding id was not found")
                            .asRuntimeException());
        }

        if(result.getDeletedCount() == 0) {
            responseObserver.onError(
                    Status.NOT_FOUND.withDescription("The blog with corresponding id was not found")
                            .asRuntimeException());
        } else {
            responseObserver.onNext(DeleteBlogResponse
                    .newBuilder()
                    .setBlogId(blogId)
                    .build());

            responseObserver.onCompleted();
        }



    }

    @Override
    public void listBlog(ListBlogRequest request, StreamObserver<ListBlogResponse> responseObserver) {
        System.out.println("Received List Blog Request");

        collection.find().iterator().forEachRemaining(document -> responseObserver.onNext(ListBlogResponse
                    .newBuilder()
                    .setBlog(utils.documentToBlog(document))
                    .build()
        ));

        responseObserver.onCompleted();
    }
}
