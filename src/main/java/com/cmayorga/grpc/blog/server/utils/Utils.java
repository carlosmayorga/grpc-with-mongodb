package com.cmayorga.grpc.blog.server.utils;

import com.proto.blog.Blog;
import org.bson.Document;

public class Utils {

    public Blog documentToBlog(Document document) {
        Blog blog = Blog.newBuilder()
                .setAuthorId(document.getString("author_id"))
                .setTitle(document.getString("title"))
                .setContent(document.getString("content"))
                .setId(document.getObjectId("_id").toString())
                .build();
        return blog;
    }
}
