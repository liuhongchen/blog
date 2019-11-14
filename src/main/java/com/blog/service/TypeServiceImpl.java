package com.blog.service;

import com.blog.dao.TypeRepository;
import com.blog.exception.NotFoundException;
import com.blog.po.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
public class TypeServiceImpl implements TypeService {


    @Autowired
    private TypeRepository repository;


    @Transactional
    @Override
    public Type saveType(Type type) {
        return repository.save(type);
    }

    @Transactional
    @Override
    public Type getType(Long id) {
        return repository.getOne(id);
    }

    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type t=repository.getOne(id);
        if (t==null){
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(type,t);
        return repository.save(t);
    }

    @Transactional
    @Override
    public void deleteType(Long id) {
        repository.delete(id);
    }

    @Override
    public Type getTypeByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public List<Type> listType() {
        return repository.findAll();
    }


    @Override
    public List<Type> listTypeTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC,"blogs.size");
        Pageable pageable = new PageRequest(0,size,sort);
        return repository.findTop(pageable);

    }


}
