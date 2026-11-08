package org.example.gongcheng1.mapper;

import org.apache.ibatis.annotations.*;
import org.example.gongcheng1.pojo.Category;

import java.util.List;

@Mapper
public interface CategoryMapper {

    //新增文章
    @Insert("INSERT INTO category (category_name, category_alias, create_user, create_time, update_time) " +
            "VALUES (#{categoryName}, #{categoryAlias}, #{createUser}, #{createTime}, #{updateTime})")
    void add(Category category);

    //查询所有
    @Select("SELECT * FROM category WHERE create_user = #{userid}")
    List<Category> list(Integer userid);

    //根据id查询
    @Select("SELECT * FROM category WHERE id = #{id}")
    Category findById(Integer id);

    //更新分类
    @Update("UPDATE category SET category_name = #{categoryName}, category_alias = #{categoryAlias}, update_time = #{updateTime} WHERE id = #{id}")
    void update(Category category);

    //删除分类
    @Delete("DELETE FROM category WHERE id = #{id}")
    void delete(Integer id);
}
