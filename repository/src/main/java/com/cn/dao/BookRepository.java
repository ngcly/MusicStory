package com.cn.dao;

import com.cn.entity.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.HighlightParameters;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author ngcly
 */
public interface BookRepository extends ElasticsearchRepository<Book, String> {

    /**
     * 从ES搜索文章
     *
     * @param title    标题
     * @param content  内容
     * @param pageable 分页
     * @return Page<Book>
     */
    @Highlight(
            fields = {
                    @HighlightField(name = "title"),
                    @HighlightField(name = "content")
            },
            parameters = @HighlightParameters(
                    preTags = "<span style='color:red'>",
                    postTags = "</span>"
            )
    )
    List<SearchHit<Book>> findBooksByTitleOrContent(String title, String content, Pageable pageable);

}
