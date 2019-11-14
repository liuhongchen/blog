package com.blog.service;

import com.blog.po.Blog;
import com.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {


    Blog save(Blog blog);

    Blog getBlog(Long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blogQuery);

    Blog save(Long id,Blog blog);

    void deleteBlog(Long id);
    Blog getAndConvert(Long id);


    Page<Blog> listBlog(Long tagId,Pageable pageable);

    Page<Blog> listBlog(String query,Pageable pageable);

    List<Blog> listRecommendBlogTop(Integer size);

    Page<Blog> listBlog(Pageable pageable);

    Map<String,List<Blog>> archiveBlog();

    Long countBlog();
}
