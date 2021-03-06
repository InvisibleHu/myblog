package com.mouse.service;
/*
 *created by mouse on 2020/2/5
 */

import com.mouse.po.Article;
import com.mouse.po.Tag;
import com.mouse.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ArticleService {

    //  管理页面
    Article saveArticle(Article article);

    Article getOneArticle(Long id);

    Page<Article> listArticle(Pageable pageable);

    Article updateArticle(Long id);     //  更新状态

    void deleteArticle(Long id);


    List<Article> getByTag(Tag tag);

    //  前端显示页面

    Page<Article> listArticleByTime(Pageable pageable);


    Article getAndConvert(Long id);


    List<Article> getArticleByType(Type type);



}
