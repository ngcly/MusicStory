package com.cn;

import com.cn.dao.BookRepository;
import com.cn.entity.Book;
import com.cn.util.RestUtil;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

/**
 * @author ngcly
 * ES 文章 service
 */
@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    public Page<Book> findBook(String title, String content, Pageable pageable){
        return bookRepository.findBooksByTitleOrContent(title,content,pageable);
    }

    /**高亮查询*/
    public ModelMap highLightSearchEssay(String keyword, Pageable pageable){
        // 构建查询条件
        Query searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.multiMatchQuery(keyword, "title","content"))
                .withHighlightFields(new HighlightBuilder.Field("*").preTags("<span style='color:red'>").postTags("</span>"))
                .withPageable(pageable)
                .build();
        // 搜索，获取结果
        SearchHits<Book> items = elasticsearchRestTemplate.search(searchQuery,Book.class);
        return RestUtil.success(items);
    }

    public Book save(Book book){
        return bookRepository.save(book);
    }

    public void delete(Book book){
        bookRepository.delete(book);
    }
}
