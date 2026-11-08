package org.example.gongcheng1.service;

import org.example.gongcheng1.pojo.Category;

import java.util.List;

public interface CategoryService {
    //新增加分类
    void add(Category category);

    //列表查询
    List<Category> list();

    //根据id查询分类信息
    Category findById(Integer id);

    //更新分类信息
    void update(Category category);

    //删除分类信息
    void delete(Integer id);
}
