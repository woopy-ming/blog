package org.woopy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.woopy.entity.Comment;
import org.woopy.entity.Type;
import org.woopy.mapper.BlogMapper;
import org.woopy.mapper.TypeMapper;
import org.woopy.service.TypeService;
import org.woopy.util.PageQueryUtil;
import org.woopy.util.PageResult;

import java.util.List;

/**
 * @author woopy
 * @data 2020/8/18 - 15:26
 */
@Service
public class TypeServiceImpl implements TypeService{

    @Autowired
    TypeMapper typeMapper;

    @Autowired
    BlogMapper blogMapper;

    @Override
    public PageResult getAllList(PageQueryUtil pageQueryUtil) {
        List<Type> types = typeMapper.getAllList(pageQueryUtil);
        int total = typeMapper.getTotalCount();
        PageResult pageResult = new PageResult(types, total, pageQueryUtil.getLimit(), pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public List<Type> getAllType() {
        return typeMapper.getAllType();
    }

    @Override
    public int getTotalCount() {
        return typeMapper.getTotalCount();
    }

    @Override
    public boolean save(String name, String icon) {
        Type type = typeMapper.selByName(name);
        if (type == null){
            Type temp = new Type();
            temp.setName(name);
            temp.setIcon(icon);
            return typeMapper.save(temp) > 0;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean update(int id, String name, String icon) {
        Type type = typeMapper.selectById(id);
        if (type != null){
            type.setName(name);
            type.setIcon(icon);
            blogMapper.updateBlogType(name,id,new int[]{id});
            return typeMapper.updateById(type) > 0;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean delete(int[] ids) {
        if (ids.length < 1)
            return false;

        blogMapper.updateBlogType("默认分类",0,ids);
        return typeMapper.deleteBatch(ids) > 0;
    }

    @Cacheable(cacheNames = "type",key = "'types'+#size")
    @Override
    public List<Type> listTypeTop(int size) {
        List<Type> types = typeMapper.getTypyTop(size);
        for (int i = 0; i < types.size(); i++){
            Type temp = types.get(i);
            types.get(i).setBlogs(blogMapper.selByTypeName(temp.getName()));
        }
        return types;
    }


}
