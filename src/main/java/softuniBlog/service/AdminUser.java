package softuniBlog.service;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import softuniBlog.bindingModel.UserEditBindingModel;

public interface AdminUser {

    String loadlistUsersView(Model model);

    String loadUserEditView(@PathVariable Integer id, Model model);

    String editUser(@PathVariable Integer id, UserEditBindingModel userEditBindingModel);

    String loadUserDeleteView(@PathVariable Integer id, Model model);

    String deleteUser(@PathVariable Integer id);
}
