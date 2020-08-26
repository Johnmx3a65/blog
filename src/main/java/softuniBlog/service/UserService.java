package softuniBlog.service;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import softuniBlog.bindingModel.UserBindingModel;
import softuniBlog.bindingModel.UserEditBindingModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface UserService {

    String loadRegisterView(Model model);

    String registerUser(UserBindingModel userBindingModel) throws IOException;

    String loadLoginView(Model model);

    String loadInputEmailView(Model model);

    String sendMail(UserBindingModel userBindingModel);

    String logoutFromPage(HttpServletRequest request, HttpServletResponse response);

    String loadProfilePageView(Model model) throws IOException;

    String loadSendPasswordForgotMailPageView(Model model);

    String loadForgotPasswordView(@PathVariable Integer id, Model model);

    String changeForgotPassword(@PathVariable Integer id, HttpServletRequest request, UserEditBindingModel userEditBindingModel);

    String loadEditView(@PathVariable Integer id, Model model);

    String editUser(@PathVariable Integer id, UserEditBindingModel userEditBindingModel) throws IOException;
}
