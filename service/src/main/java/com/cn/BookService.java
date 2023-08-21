package com.cn;

import com.cn.dao.BookRepository;
import com.cn.entity.Book;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.stereotype.Service;

/**
 * @author ngcly
 * ES 文章 service
 */
@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    /**
     * 高亮查询文章
     *
     * @param keyword  关键词
     * @param pageable 分页
     * @return Page<SearchHit < Book>>
     */
    public Page<SearchHit<Book>> highLightSearchEssay(String keyword, Pageable pageable) {
        var list = bookRepository.findBooksByTitleOrContent(keyword, keyword, pageable);
        return new PageImpl<>(list);
    }

    /**
     * 保存到 es
     *
     * @param book 文章
     */
    public void save(Book book) {
        bookRepository.save(book);
    }

    /**
     * 从ES删除文章
     *
     * @param book 文章
     */
    public void delete(Book book) {
        bookRepository.delete(book);
    }
}
