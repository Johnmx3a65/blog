package softuniBlog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import softuniBlog.entity.Article;
import softuniBlog.entity.Category;
import softuniBlog.repository.CategoryRepository;
import softuniBlog.service.HomeService;

import java.io.IOException;
import java.util.Base64;
import java.util.Set;

@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryRepository getCategoryRepository() {
        return categoryRepository;
    }

    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Set<Article> articlesInCategory(Integer id) throws IOException {

        Category category = getCategoryRepository().getOne(id);
        Set<Article> articles = category.getArticles();

        for (Article article : articles){
            if(article.getArticlePicture() != null){
                article.setArticlePictureBase64(Base64.getEncoder().encodeToString(article.getArticlePicture()));
            }
        }

        return articles;
    }

    @Override
    public String loadError403View(Model model){
        model.addAttribute("view", "error/403");

        return "base-layout";
    }
}
