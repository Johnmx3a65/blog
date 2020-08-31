package softuniBlog.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import softuniBlog.bindingModel.UserBindingModel;
import softuniBlog.bindingModel.UserEditBindingModel;
import softuniBlog.entity.Article;
import softuniBlog.entity.User;
import softuniBlog.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;


@Controller
public class UserController extends UserServiceImpl {

    @GetMapping("/register")
    public String register(Model model){

        model.addAttribute("view", "user/register");

        return "base-layout";
    }

    @PostMapping("/register")
    public String registerProcess(UserBindingModel userBindingModel) throws IOException {
        if(!userBindingModel.getPassword().equals(userBindingModel.getConfirmPassword())){
            return "redirect:/register";
        }
        registerUser(userBindingModel);
        return "redirect:/login";
    }

    @GetMapping("/user/forgot-password/{id}")
    public String forgotPassword(@PathVariable Integer id, Model model){
        if(!getUserRepository().existsById(id))
            return "redirect:/login";

        model.addAttribute("user", getUserRepository().getOne(id));
        model.addAttribute("view", "/user/forgot-password");
        return "base-layout";
    }

    @PostMapping("/user/forgot-password/{id}")
    public String forgotPassProcess(@PathVariable Integer id, HttpServletRequest request, UserEditBindingModel userEditBindingModel){
        if(!getUserRepository().existsById(id))
            return "redirect:/login";
        User user = changeForgotPassword(id, request, userEditBindingModel);
        if(user!= null)
            return "redirect:/user/forgot-password/" + user.getId();
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("view", "user/login");

        return "base-layout";
    }

    @GetMapping("/forgot-password-input-email")
    public String inputEmail(Model model){
        model.addAttribute("view", "user/forgot-password-input-email");

        return "base-layout";
    }

    @PostMapping("/forgot-password-input-email")
    public String inputEmailProcess(UserBindingModel userBindingModel){
        if(userBindingModel.getEmail().isEmpty()){
            return "redirect:/forgot-password-input-email";
        }
        if(getUserRepository().findByEmail(userBindingModel.getEmail()) == null){
            return "redirect:/forgot-password-input-email";
        }
        sendMail(userBindingModel);
        return "redirect:/send-mail";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response){
        logoutFromPage(request, response);
        return "redirect:/login?logout";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profilePage(Model model) throws IOException {

        User user = loadUserInfoForProfilePageView();
        Set<Article> articles = loadArticlesInfoForProfilePageView(user);

        model.addAttribute("articles", articles);
        model.addAttribute("user", user);
        model.addAttribute("view", "user/profile");

        return "base-layout";
    }

    @GetMapping("/send-mail")
    public String sendPasswordForgotMailPage(Model model){
        model.addAttribute("view", "user/send-mail");

        return "base-layout";
    }

    @GetMapping("/user/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(@PathVariable Integer id, Model model){
        if(!getUserRepository().existsById(id)){
            return "redirect:/profile";
        }
        User user = getUserRepository().getOne(id);
        if(!isMyProfile(user)){
            return "redirect:/profile";
        }
        model.addAttribute("user", user);
        model.addAttribute("view", "/user/edit");

        return "base-layout";
    }

    @PostMapping("/user/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editProcess(@PathVariable Integer id, UserEditBindingModel userEditBindingModel) throws IOException {
        if(!getUserRepository().existsById(id)){
            return "redirect:/profile";
        }
        if(!isMyProfile(getUserRepository().getOne(id))){
            return "redirect:/profile";
        }
        editUser(id, userEditBindingModel);
        return "redirect:/profile";
    }
}