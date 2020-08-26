package softuniBlog.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import softuniBlog.bindingModel.UserEditBindingModel;
import softuniBlog.service.impl.AdminUserImpl;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController extends AdminUserImpl {

    @GetMapping("/")
    public String listUsers(Model model){
        return loadlistUsersView(model);
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        return loadUserEditView(id, model);
    };

    @PostMapping("/edit/{id}")
    public String editProcess(@PathVariable Integer id, UserEditBindingModel userEditBindingModel){
        return editUser(id, userEditBindingModel);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model){
        return loadUserDeleteView(id, model);
    }

    @PostMapping("/delete/{id}")
    public String deleteProcess(@PathVariable Integer id){
        return deleteUser(id);
    }
}
