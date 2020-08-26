package softuniBlog.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import softuniBlog.bindingModel.CategoryBindingModel;
import softuniBlog.service.impl.CategoryServiceImpl;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController extends CategoryServiceImpl {


    @GetMapping("/")
    public String list(Model model){
        return loadCategoryListView(model);
    }

    @GetMapping("/create")
    public String create(Model model){
       return loadCategoryCreateView(model);
    }

    @PostMapping("/create")
    public String createProcess(CategoryBindingModel categoryBindingModel){
        return createCategory(categoryBindingModel);
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Integer id){
        return loadCategoryEditView(model, id);
    }

    @PostMapping("/edit/{id}")
    public String editProcess(@PathVariable Integer id, CategoryBindingModel categoryBindingModel){
        return editCategory(id, categoryBindingModel);
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable Integer id){
        return loadCategoryDeleteView(model, id);
    }

    @PostMapping("/delete/{id}")
    public String deleteProcess(@PathVariable Integer id){
        return deleteCategory(id);
    }
}
