package blog.service;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import blog.model.CategoryModel;

public interface CategoryService {

    String loadCategoryListView(Model model);

    String loadCategoryCreateView(Model model);

    String createCategory(CategoryModel categoryModel);

    String loadCategoryEditView(Model model, @PathVariable Integer id);

    String editCategory(@PathVariable Integer id, CategoryModel categoryModel);

    String loadCategoryDeleteView(Model model, @PathVariable Integer id);

    String deleteCategory(@PathVariable Integer id);
}
