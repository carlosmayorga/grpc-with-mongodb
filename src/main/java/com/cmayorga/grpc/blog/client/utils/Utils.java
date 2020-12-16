package com.cmayorga.grpc.blog.client.utils;

import com.proto.blog.Blog;

public class Utils {

    public Blog getBlog(Blog.Builder builder, String title, String author_id, String content) {
        return builder
                .setTitle(title)
                .setAuthorId(author_id)
                .setContent(content)
                .build();
    }
}
