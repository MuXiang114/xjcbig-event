package org.example.gongcheng1.mapper;


import org.apache.ibatis.annotations.*;
import org.example.gongcheng1.pojo.Article;

import java.util.List;

@Mapper
public interface ArticleMapper {


    //新增
    @Insert("INSERT INTO article (title, content, cover_img, state, category_id,create_user,create_time,update_time) " +
            "VALUES (#{title}, #{content}, #{coverImg}, #{state}, #{categoryId},#{createUser},#{createTime},#{updateTime})")
    void add(Article article);


    List<Article> list(Integer userId, Integer categoryId, String state);

    //获取
    @Select("SELECT * FROM article WHERE id = #{id}")
    Article get(Integer id);

    //删除
    @Delete("DELETE FROM article WHERE id = #{id}")
    void delete(Integer id);

    //更新
    @Update("UPDATE article SET title = #{title}, content = #{content}, cover_img = #{coverImg}, state = #{state}, category_id = #{categoryId} WHERE id = #{id}")
    void update(Article article);
}
