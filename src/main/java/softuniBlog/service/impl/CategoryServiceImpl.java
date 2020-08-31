package softuniBlog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import softuniBlog.bindingModel.CategoryBindingModel;
import softuniBlog.entity.Article;
import softuniBlog.entity.Category;
import softuniBlog.repository.ArticleRepository;
import softuniBlog.repository.CategoryRepository;
import softuniBlog.service.CategoryService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ArticleRepository articleRepository;

    public CategoryRepository getCategoryRepository() {
        return categoryRepository;
    }

    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public ArticleRepository getArticleRepository() {
        return articleRepository;
    }

    public void setArticleRepository(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public void createCategory(CategoryBindingModel categoryBindingModel){

        Category category = new Category(categoryBindingModel.getName());

        getCategoryRepository().saveAndFlush(category);
    }

    @Override
    public void editCategory(Integer id,
                              CategoryBindingModel categoryBindingModel){

        Category category = this.categoryRepository.getOne(id);

        category.setName(categoryBindingModel.getName());

        getCategoryRepository().saveAndFlush(category);
    }

    @Override
    public void deleteCategory(@PathVariable Integer id){
        Category category = this.categoryRepository.getOne(id);

        for(Article article : category.getArticles()){
            getArticleRepository().delete(article);
        }
        getCategoryRepository().delete(category);
    }
}
