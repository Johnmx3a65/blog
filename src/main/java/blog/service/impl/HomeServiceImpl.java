package blog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import blog.entity.Article;
import blog.entity.Category;
import blog.repository.CategoryRepository;
import blog.service.HomeService;

import java.util.Base64;
import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public HomeServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public String loadIndexView(Model model) {
        List<Category> categories = this.categoryRepository.findAll();

        model.addAttribute("view", "home/index");
        model.addAttribute("categories", categories);

        return "base-layout";
    }

    @Override
    public String loadListArticlesView(Model model, @PathVariable Integer id) {
        if(!this.categoryRepository.existsById(id)){
            return "redirect:/";
        }

        Category category = this.categoryRepository.getOne(id);
        List<Article> articles = category.getArticles();

        for (Article article : articles){
            if(article.getArticlePicture() != null){
                article.setArticlePictureBase64(Base64.getEncoder().encodeToString(article.getArticlePicture()));
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
