package com.mouse.service;
/*
 *created by mouse on 2020/2/5
 */

import com.mouse.NotFoundException;
import com.mouse.dao.ArticleRepository;
import com.mouse.po.Article;
import com.mouse.po.Tag;
import com.mouse.po.Type;
import com.mouse.util.MarkdownUtils;
import com.mouse.util.TimeString;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService{

    @Autowired
    private ArticleRepository articleRepository;

    //  保存一份文章
    @Override
    public Article saveArticle(Article article) {
        if (article.getId() == null){
            //  新编辑的一份文章
            article.setCreateTime(TimeString.getTime());
            article.setUpdateTime(TimeString.getTime());
            article.setViews(0);
            return articleRepository.save(article);
        } else {
            //  修改文章
            article.setUpdateTime(TimeString.getTime());
            Article a = articleRepository.getOne(article.getId());
            article.setCreateTime(a.getCreateTime());
            article.setViews(a.getViews());
            return articleRepository.save(article);
        }
    }

    @Override
    public Article getOneArticle(Long id) {
        return articleRepository.getOne(id);
    }

    //  初始化页面时，充实表格
    @Override
    public Page<Article> listArticle(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }


    @Override
    public Page<Article> listArticleByTime(Pageable pageable) {
//        Sort sort = new Sort(Sort.Direction.DESC,"updateTime");
//        Pageable pageable = new PageRequest(0,10000,sort);

        return articleRepository.findShowArticle(pageable);
    }




    @Override
    public Article getAndConvert(Long id) {
        Article article = articleRepository.findArticleByIdAndStatus(id, true);
        if (article == null) {
            throw new NotFoundException("文章飞了，没有了~");
        }
        Article a = new Article();
        BeanUtils.copyProperties(article, a);
        String content = a.getContent();
        a.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        articleRepository.updateViews(id);
        return a;
    }

    @Override
    public List<Article> getArticleByType(Type type) {
        return articleRepository.findArticlesByTypeAndStatus(type, true);
    }



    @Override
    public Article updateArticle(Long id) {
        Article a = articleRepository.getOne(id);
        if (a == null) {
            throw new NotFoundException("文章：不存在此文章");
        }
        a.setStatus(true);
        return articleRepository.save(a);
    }


    @Override
    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    @Override
    public List<Article> getByTag(Tag tag) {
        return articleRepository.findArticlesByTags(tag);
    }
}
