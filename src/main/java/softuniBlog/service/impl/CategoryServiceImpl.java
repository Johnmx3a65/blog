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
    public String createCategory(CategoryBindingModel categoryBindingModel){
        if(StringUtils.isEmpty(categoryBindingModel.getName())){
            return "redirect:/admin/categories/create";
        }

        Category category = new Category(categoryBindingModel.getName());

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
                              CategoryBindingModel categoryBindingModel){
        if(!this.categoryRepository.existsById(id)){
            return "redirect:/admin/categories";
        }

        Category category = this.categoryRepository.getOne(id);

        category.setName(categoryBindingModel.getName());

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

        for(Article article : category.getArticles()){
            this.articleRepository.delete(article);
        }
        this.categoryRepository.delete(category);

        return "redirect:/admin/categories/";
    }
}
