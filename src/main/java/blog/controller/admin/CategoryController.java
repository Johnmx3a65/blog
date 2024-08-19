package blog.controller.admin;

import blog.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import blog.model.CategoryModel;

@Controller
@RequestMapping("/admin/categories")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/")
    public String list(Model model){
        return this.categoryService.loadCategoryListView(model);
    }

    @GetMapping("/create")
    public String create(Model model){
       return this.categoryService.loadCategoryCreateView(model);
    }

    @PostMapping("/create")
    public String createProcess(CategoryModel categoryModel){
        return this.categoryService.createCategory(categoryModel);
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Integer id){
        return this.categoryService.loadCategoryEditView(model, id);
    }

    @PostMapping("/edit/{id}")
    public String editProcess(@PathVariable Integer id, CategoryModel categoryModel){
        return this.categoryService.editCategory(id, categoryModel);
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable Integer id){
        return this.categoryService.loadCategoryDeleteView(model, id);
    }

    @PostMapping("/delete/{id}")
    public String deleteProcess(@PathVariable Integer id){
        return this.categoryService.deleteCategory(id);
    }
}
