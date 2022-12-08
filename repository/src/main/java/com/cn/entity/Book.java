package com.cn.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author ngcly
 */
@Data
@Document(indexName = "book")
public class Book {
    @Id
    private Long id;
    /**标题*/
    private String title;
    /**作者*/
    private String author;
    /**内容*/
    private String content;
}
