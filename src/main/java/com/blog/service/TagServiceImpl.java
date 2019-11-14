package com.blog.service;

import com.blog.dao.TagRepository;
import com.blog.exception.NotFoundException;
import com.blog.po.Tag;
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
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository repository;

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return repository.save(tag);
    }

    @Transactional
    @Override
    public Tag getTag(Long id) {
        return repository.getOne(id);
    }

    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag t = repository.getOne(id);
        if (t==null){
            throw new NotFoundException("标签不存在！");
        }
        BeanUtils.copyProperties(tag,t);
        return repository.save(t);
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        repository.delete(id);
    }

    @Override
    public Tag getTagByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public List<Tag> listTag() {
        return repository.findAll();
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort=new Sort(Sort.Direction.DESC,"blogs.size");
        Pageable pageable=new PageRequest(0,size,sort);
        return repository.findTop(pageable);
    }

    @Override
    public List<Tag> listTag(String ids) {
        return repository.findAll(convertToList(ids));
    }


    public static List<Long> convertToList(String ids){
        List<Long> list=new ArrayList<>();
        if (ids!=null&&!ids.isEmpty()){
            String[] idarray=ids.split(",");
            for (int i=0;i<idarray.length;i++){
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }
}
