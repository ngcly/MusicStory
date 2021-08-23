package com.cn;

import com.cn.dao.BookRepository;
import com.cn.entity.Book;
import lombok.AllArgsConstructor;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * @author ngcly
 * ES 文章 service
 */
@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 查询文章
     * @param title 标题
     * @param content 内容
     * @param pageable 分页
     * @return Page<Book>
     */
    public Page<Book> findBook(String title, String content, Pageable pageable){
        return bookRepository.findBooksByTitleOrContent(title,content,pageable);
    }

    /**
     * 高亮查询
     * @param keyword  关键字
     * @param pageable 分页
     * @return SearchHits<Book>
     */
    public SearchHits<Book> highLightSearchEssay(String keyword, Pageable pageable){
        // 构建查询条件
        Query searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.multiMatchQuery(keyword, "title","content"))
                .withHighlightFields(new HighlightBuilder.Field("*").preTags("<span style='color:red'>").postTags("</span>"))
                .withPageable(pageable)
                .build();
        // 搜索，获取结果
        return elasticsearchRestTemplate.search(searchQuery,Book.class);
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
