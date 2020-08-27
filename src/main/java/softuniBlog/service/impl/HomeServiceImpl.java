package softuniBlog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import softuniBlog.entity.Article;
import softuniBlog.entity.Category;
import softuniBlog.repository.CategoryRepository;
import softuniBlog.service.HomeService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Set;

@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public String loadIndexView(Model model) {
        List<Category> categories = this.categoryRepository.findAll();

        model.addAttribute("view", "home/index");
        model.addAttribute("categories", categories);

        return "base-layout";
    }

    @Override
    public String loadListArticlesView(Model model, @PathVariable Integer id) throws IOException {
        if(!this.categoryRepository.existsById(id)){
            return "redirect:/";
        }
        Category category = this.categoryRepository.getOne(id);
        Set<Article> articles = category.getArticles();

        for (Article article : articles){
            if(article.getArticlePicture() != null){
                article.setArticleBase64(Base64.getEncoder().encodeToString(article.getArticlePicture()));
            }
        }

        model.addAttribute("articles", articles);
        model.addAttribute("category", category);
        model.addAttribute("view", "home/list-articles");

        return "base-layout";
    }

    @Override
    public String loadError403View(Model model){
        model.addAttribute("view", "error/403");

        return "base-layout";
    }
}
