package com.project.bookuluv.domain.article.service;

import com.project.bookuluv.domain.article.repository.ArticleRepository;
import com.project.bookuluv.domain.article.domain.Article;
import com.project.bookuluv.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public List<Article> getAll() {
        return this.articleRepository.findAll();
    }

    public Article getById(Integer id) {
        return this.articleRepository.findById(id);
    }

    public void create(String subject, String content, Member member, MultipartFile[] files) throws IOException {

        String projectPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "files";

        List<String> filenames = new ArrayList<>();
        List<String> filespaths = new ArrayList<>();

        for (MultipartFile file : files) {
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getName();
            String filePath = "/files/" + fileName;

            File saveFile = new File(projectPath, fileName);
            file.transferTo(saveFile);

            filenames.add(fileName);
            filespaths.add(filePath);

        }

        Article article = new Article();
        article.setSubject(subject);
        article.setContent(content);
        article.setCreateDate(LocalDateTime.now());
        article.setRegister(member);
        article.setFileNames(filenames);
        article.setFilePaths(filespaths);
        this.articleRepository.save(article);

    }

    public void modify(String subject, String content, Article article) {
        article.setSubject(subject);
        article.setContent(content);
        article.setModifyDate(LocalDateTime.now());
        this.articleRepository.save(article);
    }

    public void delete(Article article) {
        this.articleRepository.delete(article);
    }
}
