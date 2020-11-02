package org.woopy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.woopy.entity.Tag;
import org.woopy.mapper.BlogTagMapper;
import org.woopy.mapper.TagMapper;
import org.woopy.service.TagService;
import org.woopy.util.PageQueryUtil;
import org.woopy.util.PageResult;

import java.util.List;

/**
 * @author woopy
 * @data 2020/8/18 - 19:41
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    TagMapper tagMapper;

    @Autowired
    BlogTagMapper blogTagMapper;

    @Override
    public int getTotalCount() {
        return tagMapper.getTotalCount();
    }

    @Override
    public PageResult getListByPage(PageQueryUtil pageQueryUtil) {
        List<Tag> tags = tagMapper.getAllList(pageQueryUtil);
        int total = tagMapper.getTotalCount();
        PageResult pageResult = new PageResult(tags, total, pageQueryUtil.getLimit(), pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public boolean delete(int[] ids) {
        List<Integer> list = blogTagMapper.selByTagsId(ids);
        if (!CollectionUtils.isEmpty(list))
            return false;

        return tagMapper.deleteBatch(ids) > 0;
    }

    @Override
    public boolean save(String name) {
        Tag tag = tagMapper.selByName(name);
        if (tag == null){
            Tag temp = new Tag();
            temp.setName(name);
            return tagMapper.saveTag(temp) > 0;
        }
        return false;
    }

    @Cacheable(cacheNames = "tag",key = "'tags'+#size")
    @Override
    public List<Tag> listTagTop(int size) {
        List<Tag> tags = tagMapper.getTagTop(size);
        for (int i = 0; i < tags.size(); i++){
            Tag tag = tags.get(i);
            tags.get(i).setBlogsId(blogTagMapper.selByTagSIdGetBlogId(tag.getId()));
        }
        return tags;
    }

    @Override
    public Tag getTagById(int id) {
        return tagMapper.selById(id);
    }


}
