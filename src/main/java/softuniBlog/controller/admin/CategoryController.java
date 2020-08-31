package softuniBlog.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import softuniBlog.bindingModel.CategoryBindingModel;
import softuniBlog.entity.Category;
import softuniBlog.service.impl.CategoryServiceImpl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController extends CategoryServiceImpl {

    @GetMapping("/")
    public String list(Model model){
        List<Category> categories = getCategoryRepository().findAll();

        categories = categories.stream()
                .sorted(Comparator.comparingInt(Category::getId))
                .collect(Collectors.toList());

        model.addAttribute("view", "admin/categories/list");
        model.addAttribute("categories", categories);

        return "base-layout";
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("view", "admin/categories/create");
        return "base-layout";
    }

    @PostMapping("/create")
    public String createProcess(CategoryBindingModel categoryBindingModel){
        if(StringUtils.isEmpty(categoryBindingModel.getName())){
            return "redirect:/admin/categories/create";
        }
        createCategory(categoryBindingModel);
        return "redirect:/admin/categories/";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Integer id){
        if(!getCategoryRepository().existsById(id)){
            return "redirect:/admin/categories";
        }
        model.addAttribute("category", getCategoryRepository().getOne(id));
        model.addAttribute("view", "admin/categories/edit");

        return "base-layout";
    }

    @PostMapping("/edit/{id}")
    public String editProcess(@PathVariable Integer id, CategoryBindingModel categoryBindingModel){
        if(!getCategoryRepository().existsById(id)){
            return "redirect:/admin/categories";
        }
        editCategory(id, categoryBindingModel);
        return "redirect:/admin/categories/";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable Integer id){
        if(!getCategoryRepository().existsById(id)){
            return "redirect:/admin/categories/";
        }
        model.addAttribute("category", getCategoryRepository().getOne(id));
        model.addAttribute("view", "admin/categories/delete");

        return "base-layout";
    }

    @PostMapping("/delete/{id}")
    public String deleteProcess(@PathVariable Integer id){
        if(!getCategoryRepository().existsById(id)){
            return "redirect:/admin/categories/";
        }
        deleteCategory(id);
        return "redirect:/admin/categories/";
    }
}
