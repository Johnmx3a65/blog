package softuniBlog.service;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import softuniBlog.bindingModel.UserBindingModel;
import softuniBlog.bindingModel.UserEditBindingModel;
import softuniBlog.entity.Article;
import softuniBlog.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public interface UserService {

    void registerUser(UserBindingModel userBindingModel) throws IOException;

    void sendMail(UserBindingModel userBindingModel);

    void logoutFromPage(HttpServletRequest request, HttpServletResponse response);

    User loadUserInfoForProfilePageView();

    Set<Article> loadArticlesInfoForProfilePageView(User user);

    User changeForgotPassword(Integer id, HttpServletRequest request, UserEditBindingModel userEditBindingModel);

    void editUser(@PathVariable Integer id, UserEditBindingModel userEditBindingModel) throws IOException;
}
