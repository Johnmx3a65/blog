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
        model.addAttribute("users", getUserRepository().findAll());
        model.addAttribute("view", "admin/users/list");

        return "base-layout";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        if(!getUserRepository().existsById(id)){
            return "redirect:/admin/users/";
        }
        model.addAttribute("user", getUserRepository().getOne(id));
        model.addAttribute("roles", getRoleRepository().findAll());
        model.addAttribute("view", "admin/users/edit");
        return "base-layout";
    };

    @PostMapping("/edit/{id}")
    public String editProcess(@PathVariable Integer id, UserEditBindingModel userEditBindingModel){
        if(!getUserRepository().existsById(id)){
            return "redirect:/admin/users/";
        }
        editUser(id, userEditBindingModel);
        return "redirect:/admin/users/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model){
        if(!getUserRepository().existsById(id)){
            return "redirect:/admin/users";
        }
        model.addAttribute("user", getUserRepository().getOne(id));
        model.addAttribute("view", "admin/users/delete");
        return "base-layout";
    }

    @PostMapping("/delete/{id}")
    public String deleteProcess(@PathVariable Integer id){
        if(!getUserRepository().existsById(id)){
            return "redirect:/admin/users/";
        }
        deleteUser(id);
        return "redirect:/admin/users/";
    }
}
