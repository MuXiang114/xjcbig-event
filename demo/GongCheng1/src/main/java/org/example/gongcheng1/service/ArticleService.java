package org.example.gongcheng1.service;

import org.example.gongcheng1.anno.State;
import org.example.gongcheng1.pojo.Article;
import org.example.gongcheng1.pojo.PageBean;

//新增文章
public interface ArticleService {
    void add(Article article);

    //条件分页列表查询
    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer category, String state);

    //获取文章详情
    Article get(Integer id);

    //删除文章
    void delete(Integer id);

    //更新文章
    void update(Article article);
}


