package blog.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import blog.model.CategoryModel;
import blog.entity.Category;
import blog.repository.ArticleRepository;
import blog.repository.CategoryRepository;
import blog.service.CategoryService;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static blog.util.StringUtils.*;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final ArticleRepository articleRepository;

    @Override
    public String loadCategoryListView(Model model){
        List<Category> categories = this.categoryRepository.findAll();
        categories = categories.stream().sorted(Comparator.comparingInt(Category::getId)).collect(Collectors.toList());

        model.addAttribute(VIEW, ADMIN_CATEGORIES_LIST);
        model.addAttribute(CATEGORIES, categories);

        return BASE_LAYOUT;
    }

    @Override
    public String loadCategoryCreateView(Model model){
        model.addAttribute(VIEW, ADMIN_CATEGORIES_CREATE);
        return BASE_LAYOUT;
    }

    @Override
    public String createCategory(CategoryModel categoryModel){
        if(categoryModel.getName().isEmpty()){
            return REDIRECT_ADMIN_CATEGORIES_CREATE;
        }

        Category category = Category.builder().name(categoryModel.getName()).build();

        this.categoryRepository.saveAndFlush(category);

        return REDIRECT_ADMIN_CATEGORIES;
    }

    @Override
    public String loadCategoryEditView(Model model, Integer id){
        if(!this.categoryRepository.existsById(id)){
            return REDIRECT_ADMIN_CATEGORIES;
        }

        Category category = this.categoryRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException(MessageFormat.format(INVALID_CATEGORY_ID, id))
        );

        model.addAttribute(CATEGORY, category);
        model.addAttribute(VIEW, ADMIN_CATEGORIES_EDIT);

        return BASE_LAYOUT;
    }

    @Override
    public String editCategory(Integer id, CategoryModel categoryModel){
        if(!this.categoryRepository.existsById(id)){
            return REDIRECT_ADMIN_CATEGORIES;
        }

        Category category = this.categoryRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException(MessageFormat.format(INVALID_CATEGORY_ID, id))
        );
        category.setName(categoryModel.getName());

        this.categoryRepository.saveAndFlush(category);

        return REDIRECT_ADMIN_CATEGORIES;
    }

    @Override
    public String loadCategoryDeleteView(Model model, Integer id){
        if(!this.categoryRepository.existsById(id)){
            return REDIRECT_ADMIN_CATEGORIES;
        }

        Category category = this.categoryRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException(MessageFormat.format(INVALID_CATEGORY_ID, id))
        );

        model.addAttribute(CATEGORY, category);
        model.addAttribute(VIEW, ADMIN_CATEGORIES_DELETE);

        return BASE_LAYOUT;
    }

    @Override
    public String deleteCategory(Integer id){
        if(!this.categoryRepository.existsById(id)){
            return REDIRECT_ADMIN_CATEGORIES;
        }

        Category category = this.categoryRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException(MessageFormat.format(INVALID_CATEGORY_ID, id))
        );

        this.articleRepository.deleteAll(category.getArticles());
        this.categoryRepository.delete(category);

        return REDIRECT_ADMIN_CATEGORIES;
    }
}
