package com.cn;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.cn.dao.BookRepository;
import com.cn.entity.Book;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author ngcly
 * ES 文章 service
 */
@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final ElasticsearchClient elasticsearchClient;

    /**
     * 高亮查询文章
     * @param title 标题
     * @param content 内容
     * @param pageable 分页
     * @return Page<Book>
     */
    public Page<SearchHits<Book>> findBook(String title, String content, Pageable pageable){
        return bookRepository.findBooksByTitleOrContent(title,content,pageable);
    }

    /**
     * 高亮查询
     * @param keyword  关键字
     * @param pageable 分页
     * @return SearchHits<Book>
     */
    public SearchResponse<Book> highLightSearchEssay(String keyword, Pageable pageable) throws IOException {
        return elasticsearchClient.search(queryBuilder ->
                queryBuilder.from(pageable.getPageNumber() + pageable.getPageSize())
                        .size(pageable.getPageSize())
                        .query(query -> query.multiMatch(multiMatchQueryBuilder->
                                multiMatchQueryBuilder.fields("title","content").query(keyword).operator(Operator.Or)))
                        //高亮查询
                        .highlight(highlightBuilder-> highlightBuilder.preTags("<span style='color:red'>")
                                .postTags("</span>")
                                //多字段时，需要设置为false
                                .requireFieldMatch(false)
                                .fields("*", highlightFieldBuilder->highlightFieldBuilder)),
                Book.class);
    }

    /**
     * 保存到 es
     * @param book 文章
     */
    public void save(Book book){
        bookRepository.save(book);
    }

    /**
     * 从ES删除文章
     * @param book 文章
     */
    public void delete(Book book){
        bookRepository.delete(book);
    }
}
