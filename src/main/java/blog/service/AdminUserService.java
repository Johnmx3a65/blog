package blog.service;

import org.springframework.ui.Model;
import blog.model.UserEditModel;

public interface AdminUserService {

    String loadListUsersView(Model model);

    String loadUserEditView(Integer id, Model model);

    String editUser(Integer id, UserEditModel userEditModel);

    String loadUserDeleteView(Integer id, Model model);

    String deleteUser(Integer id);
}
