package org.example.gongcheng1.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.gongcheng1.anno.State;
import org.example.gongcheng1.mapper.ArticleMapper;
import org.example.gongcheng1.pojo.Article;
import org.example.gongcheng1.service.ArticleService;
import org.example.gongcheng1.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.gongcheng1.pojo.PageBean;

import java.util.List;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public void add(Article article) {

        //补充属性值
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        article.setCreateUser(userId);
        articleMapper.add(article);

    }

    @Override
    public PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state) {
        //创建pagebean对象
        PageBean<Article> pb = new PageBean<>();

        //开启分页查询
        PageHelper.startPage(pageNum, pageSize);


        //调用mapper
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        List<Article> as = articleMapper.list(userId, categoryId, state);

        Page<Article> p = (Page<Article>) as;
        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());
        return pb;
    }
    @Override
    public Article get(Integer id) {
        return articleMapper.get(id);
    }
    @Override
    public void delete(Integer id) {
        articleMapper.delete(id);
    }
    @Override
    public void update(Article article) {
        articleMapper.update(article);
    }
}
