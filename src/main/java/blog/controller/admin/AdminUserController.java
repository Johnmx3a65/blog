package blog.controller.admin;

import blog.service.AdminUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import blog.model.UserEditModel;

@Controller
@RequestMapping("/admin/users")
@AllArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping("/")
    public String listUsers(Model model){
        return this.adminUserService.loadListUsersView(model);
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        return this.adminUserService.loadUserEditView(id, model);
    }

    @PostMapping("/edit/{id}")
    public String editProcess(@PathVariable Integer id, UserEditModel userEditModel){
        return this.adminUserService.editUser(id, userEditModel);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model){
        return this.adminUserService.loadUserDeleteView(id, model);
    }

    @PostMapping("/delete/{id}")
    public String deleteProcess(@PathVariable Integer id){
        return this.adminUserService.deleteUser(id);
    }
}
