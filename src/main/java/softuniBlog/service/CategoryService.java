package softuniBlog.service;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import softuniBlog.bindingModel.CategoryBindingModel;

public interface CategoryService {

    String loadCategoryListView(Model model);

    String loadCategoryCreateView(Model model);

    String createCategory(CategoryBindingModel categoryBindingModel);

    String loadCategoryEditView(Model model, @PathVariable Integer id);

    String editCategory(@PathVariable Integer id, CategoryBindingModel categoryBindingModel);

    String loadCategoryDeleteView(Model model, @PathVariable Integer id);

    String deleteCategory(@PathVariable Integer id);
}
