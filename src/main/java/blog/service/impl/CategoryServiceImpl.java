package blog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import blog.model.CategoryModel;
import blog.entity.Category;
import blog.repository.ArticleRepository;
import blog.repository.CategoryRepository;
import blog.service.CategoryService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final ArticleRepository articleRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ArticleRepository articleRepository) {
        this.categoryRepository = categoryRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public String loadCategoryListView(Model model){
        model.addAttribute("view", "admin/categories/list");

        List<Category> categories = this.categoryRepository.findAll();

        categories = categories.stream()
                .sorted(Comparator.comparingInt(Category::getId))
                .collect(Collectors.toList());

        model.addAttribute("categories", categories);

        return "base-layout";
    }

    @Override
    public String loadCategoryCreateView(Model model){
        model.addAttribute("view", "admin/categories/create");

        return "base-layout";
    }

    @Override
    public String createCategory(CategoryModel categoryModel){
        if(StringUtils.isEmpty(categoryModel.getName())){
            return "redirect:/admin/categories/create";
        }

        Category category = Category.builder().name(categoryModel.getName()).build();

        this.categoryRepository.saveAndFlush(category);

        return "redirect:/admin/categories/";
    }

    @Override
    public String loadCategoryEditView(Model model, @PathVariable Integer id){
        if(!this.categoryRepository.existsById(id)){
            return "redirect:/admin/categories";
        }
        Category category = this.categoryRepository.getOne(id);

        model.addAttribute("category", category);
        model.addAttribute("view", "admin/categories/edit");

        return "base-layout";
    }

    @Override
    public String editCategory(@PathVariable Integer id,
                              CategoryModel categoryModel){
        if(!this.categoryRepository.existsById(id)){
            return "redirect:/admin/categories";
        }

        Category category = this.categoryRepository.getOne(id);

        category.setName(categoryModel.getName());

        this.categoryRepository.saveAndFlush(category);

        return "redirect:/admin/categories/";
    }

    @Override
    public String loadCategoryDeleteView(Model model, @PathVariable Integer id){
        if(!this.categoryRepository.existsById(id)){
            return "redirect:/admin/categories/";
        }
        Category category = this.categoryRepository.getOne(id);

        model.addAttribute("category", category);
        model.addAttribute("view", "admin/categories/delete");

        return "base-layout";
    }

    @Override
    public String deleteCategory(@PathVariable Integer id){
        if(!this.categoryRepository.existsById(id)){
            return "redirect:/admin/categories/";
        }
        Category category = this.categoryRepository.getOne(id);

        this.articleRepository.deleteAll(category.getArticles());
        this.categoryRepository.delete(category);

        return "redirect:/admin/categories/";
    }
}
