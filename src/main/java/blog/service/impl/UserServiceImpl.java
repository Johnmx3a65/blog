package blog.service.impl;

import blog.service.MailSenderService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import blog.model.UserModel;
import blog.model.UserEditModel;
import blog.entity.Article;
import blog.entity.Role;
import blog.entity.User;
import blog.repository.RoleRepository;
import blog.repository.UserRepository;
import blog.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static blog.util.StringUtils.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final MailSenderService mailSenderService;

    @Override
    public String loadRegisterView(Model model){
        model.addAttribute(VIEW, USER_REGISTER);

        return BASE_LAYOUT;
    }

    @Override
    public String registerUser(UserModel userModel) throws IOException {

        if(!userModel.getPassword().equals(userModel.getConfirmPassword())){
            return REDIRECT_REGISTER;
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        User user = User
                .builder()
                .email(userModel.getEmail())
                .fullName(userModel.getFullName())
                .password(bCryptPasswordEncoder.encode(userModel.getPassword()))
                .build();

        if(userModel.getProfilePicture() != null){
            byte[] imageFile = userModel.getProfilePicture().getBytes();
            user.setProfilePicture(imageFile);
        }

        Role userRole = this.roleRepository.findByName(ROLE_USER);
        user.setRoles(Collections.singletonList(userRole));

        this.userRepository.saveAndFlush(user);

        return REDIRECT_LOGIN;
    }

    @Override
    public String loadLoginView(Model model){
        model.addAttribute(VIEW, USER_LOGIN);
        return BASE_LAYOUT;
    }

    @Override
    public String loadInputEmailView(Model model){
        model.addAttribute(VIEW, USER_FORGOT_PASSWORD_INPUT_EMAIL);
        return BASE_LAYOUT;
    }

    @Override
    public String sendMail(UserModel userModel){
        if(userModel.getEmail().isEmpty()){
            return USER_FORGOT_PASSWORD_INPUT_EMAIL;
        }

        User user = this.userRepository.findByEmail(userModel.getEmail());

        if(user == null){
            return USER_FORGOT_PASSWORD_INPUT_EMAIL;
        }

        user.setConfirmCode(UUID.randomUUID().toString());

        String message = MessageFormat.format(
            CONFIRMATION_CODE_MAIL,
            user.getFullName(),
            user.getId(),
            user.getConfirmCode()
        );

        mailSenderService.send(user.getEmail(), CHANGE_PASSWORD, message);
        this.userRepository.saveAndFlush(user);

        return REDIRECT_SEND_MAIL;
    }

    @Override
    public String logoutFromPage(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null){
            new SecurityContextLogoutHandler().logout(request,response,auth);
        }

        return MessageFormat.format(REDIRECT_LOGIN_WITH_QUERY, LOGOUT);
    }

    @Override
    public String loadProfilePageView(Model model) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = this.userRepository.findByEmail(principal.getUsername());

        if(user.getProfilePicture() != null){
            user.setProfilePictureBase64(Base64.getEncoder().encodeToString(user.getProfilePicture()));
        }

        List<Article> articles = user.getArticles();

        for (Article article : articles){
            if(article.getArticlePicture() != null){
                article.setArticlePictureBase64(Base64.getEncoder().encodeToString(article.getArticlePicture()));
            }
        }

        model.addAttribute(ARTICLES, articles);
        model.addAttribute(USER, user);
        model.addAttribute(VIEW, USER_PROFILE);

        return BASE_LAYOUT;
    }

    @Override
    public String loadSendPasswordForgotMailPageView(Model model){
        model.addAttribute(VIEW, USER_SEND_MAIL);
        return BASE_LAYOUT;
    }

    @Override
    public String loadForgotPasswordView(Integer id, Model model){
        if(!this.userRepository.existsById(id)){
            return REDIRECT_LOGIN;
        }

        User user = this.userRepository.getReferenceById(id);

        model.addAttribute(USER, user);
        model.addAttribute(VIEW, USER_FORGOT_PASSWORD);

        return BASE_LAYOUT;
    }

    @Override
    public String changeForgotPassword(Integer id, HttpServletRequest request, UserEditModel userEditModel){
        if(!this.userRepository.existsById(id)){
            return REDIRECT_LOGIN;
        }

        User user = this.userRepository.getReferenceById(id);

        if(request.getParameter("sendAgain") != null){
            user.setConfirmCode(UUID.randomUUID().toString());

            String message = MessageFormat.format(NEW_CONFIRMATION_CODE_MAIL, user.getFullName(), user.getConfirmCode());

            mailSenderService.send(user.getEmail(), CHANGE_PASSWORD, message);
            this.userRepository.saveAndFlush(user);

            return MessageFormat.format(REDIRECT_USER_FORGOT_PASSWORD_ID, user.getId());
        }

        if(!userEditModel.getPassword().equals(userEditModel.getConfirmPassword()) || !userEditModel.getConfirmCode().equals(user.getConfirmCode())){
            return MessageFormat.format(REDIRECT_USER_FORGOT_PASSWORD_ID, user.getId());
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        user.setPassword(bCryptPasswordEncoder.encode(userEditModel.getPassword()));

        user.setConfirmCode(null);

        this.userRepository.saveAndFlush(user);

        return REDIRECT_LOGIN;
    }

    @Override
    public String loadEditView(Integer id, Model model){

        if(!this.userRepository.existsById(id)){
            return REDIRECT_PROFILE;
        }

        User user = this.userRepository.getReferenceById(id);

        if(isNotMyProfile(user)){
            return REDIRECT_PROFILE;
        }

        model.addAttribute(USER, user);
        model.addAttribute(VIEW, USER_EDIT);

        return BASE_LAYOUT;
    }

    @Override
    public String editUser(Integer id, UserEditModel userEditModel) throws IOException {

        if(!this.userRepository.existsById(id)){
            return REDIRECT_PROFILE;
        }

        User user = this.userRepository.getReferenceById(id);

        if(isNotMyProfile(user)){
            return REDIRECT_PROFILE;
        }

        String password = userEditModel.getPassword();
        String confirmPassword = userEditModel.getConfirmPassword();

        if(!(password.isEmpty() || confirmPassword.isEmpty()) && password.equals(confirmPassword)){
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            user.setPassword(bCryptPasswordEncoder.encode(password));
        }


        if(!userEditModel.getProfilePicture().isEmpty()){
            byte[] imageFile = userEditModel.getProfilePicture().getBytes();
            user.setProfilePicture(imageFile);
        }

        user.setFullName(userEditModel.getFullName());
        user.setEmail(userEditModel.getEmail());

        this.userRepository.saveAndFlush(user);

        return REDIRECT_PROFILE;
    }

    private boolean isNotMyProfile(User user){
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return !user.getEmail().equals(currentUser.getUsername());
    }
}
