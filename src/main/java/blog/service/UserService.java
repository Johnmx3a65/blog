package blog.service;

import org.springframework.ui.Model;
import blog.model.UserModel;
import blog.model.UserEditModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface UserService {

    String loadRegisterView(Model model);

    String registerUser(UserModel userModel) throws IOException;

    String loadLoginView(Model model);

    String loadInputEmailView(Model model);

    String sendMail(UserModel userModel);

    String logoutFromPage(HttpServletRequest request, HttpServletResponse response);

    String loadProfilePageView(Model model) throws IOException;

    String loadSendPasswordForgotMailPageView(Model model);

    String loadForgotPasswordView(Integer id, Model model);

    String changeForgotPassword(Integer id, HttpServletRequest request, UserEditModel userEditModel);

    String loadEditView(Integer id, Model model);

    String editUser(Integer id, UserEditModel userEditModel) throws IOException;
}
