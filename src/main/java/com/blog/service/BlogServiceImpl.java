package com.blog.service;

import com.blog.dao.BlogRepository;
import com.blog.exception.NotFoundException;
import com.blog.po.Blog;

import com.blog.po.Type;
import com.blog.util.MarkdownUtils;
import com.blog.util.MyBeanUtils;
import com.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository repository;



    @Override
    public Blog save(Blog blog) {
        if (blog.getId()==null) {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        }else{
            blog.setUpdateTime(new Date());
        }
        return repository.save(blog);
    }

    @Override
    public Blog getBlog(Long id) {
        return repository.getOne(id);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blogQuery) {

        return repository.findAll((Specification<Blog>) (root, cq, cb) -> {
            List<Predicate> predicates=new ArrayList<>();
            if (blogQuery.getTitle()!=null&&!blogQuery.getTitle().isEmpty()){
                predicates.add(cb.like(root.<String>get("title"),"%"+blogQuery.getTitle()+"%"));
            }
            if (blogQuery.getTypeId()!=null){
                predicates.add(cb.equal(root.<Type>get("type").get("id"),blogQuery.getTypeId()));
            }
            if (blogQuery.isRecommend()){
                predicates.add(cb.equal(root.<Boolean>get("recommend"),blogQuery.isRecommend()));
            }

            cq.where(predicates.toArray(new Predicate[predicates.size()]));
            return null;
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return repository.findAll(pageable);
    }


    @Transactional
    @Override
    public Blog save(Long id, Blog blog) {
        Blog b = repository.getOne(id);
        if (b==null){
            throw new NotFoundException("博客不存在！");
        }
        BeanUtils.copyProperties(blog,b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return repository.save(b);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        repository.delete(id);
    }

    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = repository.getOne(id);
        if (blog == null) {
            throw new NotFoundException("该博客不存在");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));

        repository.updateViews(id);
        return b;
    }


    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return repository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("tags");
                return cb.equal(join.get("id"),tagId);
            }
        },pageable);
    }


    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return repository.findByQuery(query,pageable);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable = new PageRequest(0, size, sort);
        return repository.findTop(pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = repository.findGroupYear();
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years) {
            map.put(year, repository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return repository.count();
    }
}
