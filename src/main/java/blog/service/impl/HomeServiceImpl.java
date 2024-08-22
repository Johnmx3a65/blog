package blog.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import blog.entity.Article;
import blog.entity.Category;
import blog.repository.CategoryRepository;
import blog.service.HomeService;

import java.text.MessageFormat;
import java.util.List;

import static blog.util.StringUtils.*;

@Service
@AllArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final CategoryRepository categoryRepository;

    @Override
    public String loadIndexView(Model model) {
        List<Category> categories = this.categoryRepository.findAll();

        model.addAttribute(VIEW, HOME_INDEX);
        model.addAttribute(CATEGORIES, categories);

        return BASE_LAYOUT;
    }

    @Override
    public String loadListArticlesView(Model model, Integer id) {
        if(!this.categoryRepository.existsById(id)){
            return REDIRECT_HOME;
        }

        Category category = this.categoryRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException(MessageFormat.format(INVALID_CATEGORY_ID, id))
        );
        List<Article> articles = category.getArticles();

        model.addAttribute(ARTICLES, articles);
        model.addAttribute(CATEGORY, category);
        model.addAttribute(VIEW, HOME_LIST_ARTICLES);

        return BASE_LAYOUT;
    }

    @Override
    public String loadError403View(Model model){
        model.addAttribute(VIEW, ERROR_403);
        return BASE_LAYOUT;
    }
}
