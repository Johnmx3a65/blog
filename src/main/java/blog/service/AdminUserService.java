package blog.service;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import blog.model.UserEditModel;

public interface AdminUserService {

    String loadlistUsersView(Model model);

    String loadUserEditView(@PathVariable Integer id, Model model);

    String editUser(@PathVariable Integer id, UserEditModel userEditModel);

    String loadUserDeleteView(@PathVariable Integer id, Model model);

    String deleteUser(@PathVariable Integer id);
}
