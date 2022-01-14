package com.carpenter.yan.elasticsearch.rest;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Index {
    public static void index(String[] args) throws IOException {
        //1. Document source provided as a String
        IndexRequest indexRequest1 = new IndexRequest("posts");
        indexRequest1.id("1");
        String jsonString = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";
        indexRequest1.source(jsonString, XContentType.JSON);

        //2. Document source provided as a Map which gets automatically converted to JSON format
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "kimchy");
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "trying out Elasticsearch");
        IndexRequest indexRequest2 = new IndexRequest("posts")
                .id("1").source(jsonMap);

        //3. Document source provided as an XContentBuilder object, the Elasticsearch built-in helpers to generate JSON content
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.field("user", "kimchy");
            builder.timeField("postDate", new Date());
            builder.field("message", "trying out Elasticsearch");
        }
        builder.endObject();
        IndexRequest indexRequest3 = new IndexRequest("posts")
                .id("1").source(builder);

        //4. Document source provided as Object key-pairs, which gets converted to JSON format
        IndexRequest indexRequest4 = new IndexRequest("posts")
                .id("1")
                .source("user", "kimchy",
                        "postDate", new Date(),
                        "message", "trying out Elasticsearch");

        //5. Synchronous execution
        IndexResponse indexResponse = Client.getClient().index(indexRequest4, RequestOptions.DEFAULT);

        //6. asynchronous execution
        ActionListener listener = new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        };
    }
}
