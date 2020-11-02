package org.woopy.service;

import org.woopy.entity.Type;
import org.woopy.util.PageQueryUtil;
import org.woopy.util.PageResult;

import java.util.List;

/**
 * @author woopy
 * @data 2020/8/18 - 15:25
 */
public interface TypeService {

    PageResult getAllList(PageQueryUtil pageQueryUtil);

    List<Type> getAllType();

    int getTotalCount();

    boolean save(String name,String icon);

    boolean update(int id,String name,String icon);

    boolean delete(int[] ids);

    List<Type> listTypeTop(int size);
}
