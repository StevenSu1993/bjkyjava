package com.baojikouyu.teach.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.elasticsearch.watcher.Index;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.baojikouyu.teach.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EsController {

    @Autowired
    private ElasticsearchClient client;

    public static String indexName = "products";

    //创建索引
    @GetMapping("/search")
    public String search() throws Exception {
//        CreateIndexRequest indexRequest = new CreateIndexRequest.Builder()
//                .index(indexName).build();
        //创建一个索引
        client.indices().create(c -> c.index(indexName));
//        CreateIndexRequest.Builder builder = new CreateIndexRequest.Builder();
//        esclient.indices().create(builder.build());
        return "nihaa";
    }

    //测试索引存在不不在
    @GetMapping("/testIndex")
    public String testExistsIndex() throws Exception {

        ExistsRequest build = new ExistsRequest.Builder().index(indexName).build();
        final BooleanResponse exists = client.indices().exists(build);
        System.out.println("看看索引是否存在 =" + exists);
        return "nihaa";
    }

    //删除索引
    @GetMapping("/deleteIndex")
    public String deleteIndex() throws Exception {

        DeleteIndexRequest request = new DeleteIndexRequest.Builder().index(indexName).build();
        DeleteIndexResponse response = client.indices().delete(request);
        return "nihaa";
    }

    //    添加文档
    @GetMapping("/addIndexDoc")
    public String addIndexDoc() throws Exception {
        User user = new User();
//        ObjectMapper objectMapper = new ObjectMapper();
//        final String s = objectMapper.writeValueAsString(user);
        IndexRequest request = new IndexRequest.Builder<User>().document(user).index(indexName).build();
        final IndexResponse index = client.index(request);
        return "nihaa";
    }

    //根据term进行查询
    @GetMapping("/searchIndex")
    public String searchIndex() throws Exception {

        SearchResponse<User> search = client.search(s -> s
                        .index("products")
                        .query(q -> q
                                .term(t -> t
                                        .field("name")
                                        .value(v -> v.stringValue("steven"))
                                )),
                User.class);

        for (Hit<User> hit : search.hits().hits()) {
            System.out.println(hit.source());
        }
        return "serchIndex";
    }

    //查询 no  lambad 表达式
    @GetMapping("/searchIndex1")
    public String searchIndex1() throws Exception {
        final SearchRequest.Builder build = new SearchRequest.Builder().index("products");
        final Query.Builder queryBuilder = new Query.Builder();
        final TermQuery.Builder termBuilder = new TermQuery.Builder();
        termBuilder.field("name");
        FieldValue fieldValue = new FieldValue.Builder().stringValue("steven").build();
        termBuilder.value(fieldValue);
        queryBuilder.term(termBuilder.build());
        build.query(queryBuilder.build());
        final SearchResponse<User> response = client.search(build.build(), User.class);
        for (Hit<User> hit : response.hits().hits()) {
            System.out.println(hit.source());
        }
        return "破解版本";
    }

    //简单易懂操作
    @GetMapping("/searchIndex2")
    public String searchIndex2() throws Exception {

        Query query = new Query.Builder()
                .term(t -> t
                        .field("name")
                        .value(v -> v.stringValue("foo"))
                )
                .build();

        SearchResponse<User> search = client.search(s -> s.index("products").query(query), User.class);


        return "破解版本";
    }


    @GetMapping("/searchhighlight")
    public String searchhighlight() throws Exception {
//        同一个字段查询多值
//         client.search(s -> s
//                        .index("products")
//                        .query(q -> q.multiMatch(
//                                v->v.query("steven")
//                                .fields("name","title")
//                        ))
//                 ,User.class);

//        match 用法
//        client.search(s -> s
//                        .index("products").query(q -> q
//                            .match( v -> v.queryName("name").field("stven")
//                            )

//                        )
//                 ,User.class);


        new Index.Builder();
        SearchResponse<User> search = client.search(s -> s
                        .index("products").query(q -> q
                                .term(t -> t
                                        .field("name")
                                        .value(str -> str.stringValue("steven"))
                                )
                        ).highlight(h -> h.preTags("<p>").postTags("</p>"))


                , User.class);


        return "破解版本";
    }

}
