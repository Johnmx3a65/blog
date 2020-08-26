package softuniBlog.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import softuniBlog.bindingModel.UserBindingModel;
import softuniBlog.bindingModel.UserEditBindingModel;
import softuniBlog.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
public class UserController extends UserServiceImpl {

    @GetMapping("/register")
    public String register(Model model){
        return loadRegisterView(model);
    }

    @PostMapping("/register")
    public String registerProcess(UserBindingModel userBindingModel) throws IOException {
        return registerUser(userBindingModel);
    }

    @GetMapping("/user/forgot-password/{id}")
    public String forgotPassword(@PathVariable Integer id, Model model){
        return loadForgotPasswordView(id, model);
    }

    @PostMapping("/user/forgot-password/{id}")
    public String forgotPassProcess(@PathVariable Integer id, HttpServletRequest request, UserEditBindingModel userEditBindingModel){
        return changeForgotPassword(id, request, userEditBindingModel);
    }

    @GetMapping("/login")
    public String login(Model model){
        return loadLoginView(model);
    }

    @GetMapping("/forgot-password-input-email")
    public String inputEmail(Model model){
        return loadInputEmailView(model);
    }

    @PostMapping("/forgot-password-input-email")
    public String inputEmailProcess(UserBindingModel userBindingModel){
        return sendMail(userBindingModel);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response){
        return logoutFromPage(request, response);
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profilePage(Model model) throws IOException {
        return loadProfilePageView(model);
    }

    @GetMapping("/send-mail")
    public String sendPasswordForgotMailPage(Model model){
        return loadSendPasswordForgotMailPageView(model);
    }

    @GetMapping("/user/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(@PathVariable Integer id, Model model){
        return loadEditView(id, model);
    }

    @PostMapping("/user/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editProcess(@PathVariable Integer id, UserEditBindingModel userEditBindingModel) throws IOException {
        return editUser(id, userEditBindingModel);
    }
}