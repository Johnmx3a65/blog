package blog.service;

import org.springframework.ui.Model;
import blog.model.CategoryModel;

public interface CategoryService {

    String loadCategoryListView(Model model);

    String loadCategoryCreateView(Model model);

    String createCategory(CategoryModel categoryModel);

    String loadCategoryEditView(Model model, Integer id);

    String editCategory(Integer id, CategoryModel categoryModel);

    String loadCategoryDeleteView(Model model, Integer id);

    String deleteCategory(Integer id);
}
