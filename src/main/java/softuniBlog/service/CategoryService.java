package softuniBlog.service;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import softuniBlog.bindingModel.CategoryBindingModel;

public interface CategoryService {

    void createCategory(CategoryBindingModel categoryBindingModel);

    void editCategory(Integer id, CategoryBindingModel categoryBindingModel);

    void deleteCategory(@PathVariable Integer id);
}
