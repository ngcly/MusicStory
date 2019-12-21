package com.cn.dao;

import com.cn.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author ngcly
 */
public interface BookRepository extends ElasticsearchRepository<Book, String> {

    Page<Book> findBooksByTitleOrContent(String title, String content, Pageable pageable);

}
