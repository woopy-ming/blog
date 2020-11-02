package org.woopy.service;

import org.woopy.entity.Tag;
import org.woopy.util.PageQueryUtil;
import org.woopy.util.PageResult;

import java.util.List;

/**
 * @author woopy
 * @data 2020/8/18 - 19:40
 */
public interface TagService {

    int getTotalCount();

    PageResult getListByPage(PageQueryUtil pageQueryUtil);

    boolean delete(int[] ids);

    boolean save(String name);

    List<Tag> listTagTop(int size);

    Tag getTagById(int id);
}
