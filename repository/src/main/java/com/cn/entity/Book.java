package com.cn.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author ngcly
 */
@Document(indexName = "book")
@Getter
@Setter
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
