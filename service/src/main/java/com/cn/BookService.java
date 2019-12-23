//package com.cn;
//
//import com.cn.config.HighLightResultMapper;
//import com.cn.dao.BookRepository;
//import com.cn.entity.Book;
//import com.cn.util.RestUtil;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
//import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
//import org.springframework.data.elasticsearch.core.query.SearchQuery;
//import org.springframework.stereotype.Service;
//import org.springframework.ui.ModelMap;
//
///**
// * @author ngcly
// * ES 文章 service
// */
//@Service
//public class BookService {
//    @Autowired
//    private BookRepository bookRepository;
//    @Autowired
//    private ElasticsearchRestTemplate elasticsearchRestTemplate;
//    @Autowired
//    private HighLightResultMapper highLightResultMapper;
//
//    public Page<Book> findBook(String title, String content, Pageable pageable){
//        return bookRepository.findBooksByTitleOrContent(title,content,pageable);
//    }
//
//    public ModelMap searchEssay(String keyword){
//        // 构建查询条件
//        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.multiMatchQuery(keyword, "title","content")).build();
//        // 搜索，获取结果
//        Page<Book> items = bookRepository.search(searchQuery);
//        return RestUtil.success(items);
//    }
//
//    /**高亮查询*/
//    public ModelMap highLightSearchEssay(String keyword){
//        // 构建查询条件
//        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.multiMatchQuery(keyword, "title","content"))
//                .withHighlightFields(new HighlightBuilder.Field("*").preTags("<span style='color:red'>").postTags("</span>")).build();
//        // 搜索，获取结果
//        Page<Book> items = elasticsearchRestTemplate.queryForPage(searchQuery,Book.class,highLightResultMapper);
//        return RestUtil.success(items);
//    }
//
//    public Book save(Book book){
//        return bookRepository.save(book);
//    }
//
//    public void delete(Book book){
//        bookRepository.delete(book);
//    }
//}
