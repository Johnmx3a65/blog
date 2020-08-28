package softuniBlog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuniBlog.entity.Article;
import softuniBlog.entity.Tag;
import softuniBlog.repository.TagRepository;
import softuniBlog.service.TagService;

import java.util.Base64;
import java.util.Set;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    public TagRepository getTagRepository() {
        return tagRepository;
    }

    public void setTagRepository(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public void loadArticlesPictureFromDB(String name){
        Tag tag = this.tagRepository.findByName(name);

        Set<Article> articles = tag.getArticles();

        for (Article article : articles){
            if(article.getArticlePicture() != null){
                article.setArticlePictureBase64(Base64.getEncoder().encodeToString(article.getArticlePicture()));
            }
        }
    }
}
