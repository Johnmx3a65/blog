package blog.service.impl;

import blog.service.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
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
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final MailSender mailSender;

    @Autowired
    public UserServiceImpl(RoleRepository roleRepository, UserRepository userRepository, MailSender mailSender) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    @Override
    public String loadRegisterView(Model model){
        model.addAttribute("view", "user/register");

        return "base-layout";
    }

    @Override
    public String registerUser(UserModel userModel) throws IOException {

        if(!userModel.getPassword().equals(userModel.getConfirmPassword())){
            return "redirect:/register";
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

        Role userRole = this.roleRepository.findByName("ROLE_USER");
        user.setRoles(Collections.singletonList(userRole));

        this.userRepository.saveAndFlush(user);

        return "redirect:/login";
    }

    @Override
    public String loadLoginView(Model model){
        model.addAttribute("view", "user/login");

        return "base-layout";
    }

    @Override
    public String loadInputEmailView(Model model){

        model.addAttribute("view", "user/forgot-password-input-email");

        return "base-layout";
    }

    @Override
    public String sendMail(UserModel userModel){
        if(userModel.getEmail().isEmpty()){
            return "redirect:/forgot-password-input-email";
        }

        if(this.userRepository.findByEmail(userModel.getEmail()) == null){
            return "redirect:/forgot-password-input-email";
        }
        User user = this.userRepository.findByEmail(userModel.getEmail());

        user.setConfirmCode(UUID.randomUUID().toString());

        String message = String.format("Hello, %s!\n" +
                "If you tried to change your password, please go to the next link: http://localhost:8080/user/forgot-password/%s . Your confirm code: %s\n" +
                "If it's not you, please ignore this message!", user.getFullName(), user.getId().toString(), user.getConfirmCode());

        mailSender.send(user.getEmail(), "Change Password", message);
        this.userRepository.saveAndFlush(user);

        return "redirect:/send-mail";
    }

    @Override
    public String logoutFromPage(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null){
            new SecurityContextLogoutHandler().logout(request,response,auth);
        }
        return "redirect:/login?logout";
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

        model.addAttribute("articles", articles);
        model.addAttribute("user", user);
        model.addAttribute("view", "user/profile");

        return "base-layout";
    }

    public String loadSendPasswordForgotMailPageView(Model model){

        model.addAttribute("view", "user/send-mail");

        return "base-layout";
    }

    @Override
    public String loadForgotPasswordView(@PathVariable Integer id, Model model){

        if(!this.userRepository.existsById(id)){
            return "redirect:/login";
        }

        User user = this.userRepository.getOne(id);

        model.addAttribute("user", user);
        model.addAttribute("view", "/user/forgot-password");

        return "base-layout";
    }


    @Override
    public String changeForgotPassword(@PathVariable Integer id, HttpServletRequest request, UserEditModel userEditModel){

        if(!this.userRepository.existsById(id)){
            return "redirect:/login";
        }

        User user = this.userRepository.getOne(id);

        if(request.getParameter("sendAgain") != null){
            user.setConfirmCode(UUID.randomUUID().toString());

            String message = String.format("Hello, %s!\n" +
                    "Your confirm code: %s\n" +
                    "If it's not you, please ignore this message!", user.getFullName(), user.getConfirmCode());

            mailSender.send(user.getEmail(), "Change Password", message);
            this.userRepository.saveAndFlush(user);

            return "redirect:/user/forgot-password/" + user.getId();
        }

        if(!userEditModel.getPassword().equals(userEditModel.getConfirmPassword()) || !userEditModel.getConfirmCode().equals(user.getConfirmCode())){
            return "redirect:/user/forgot-password/" + user.getId();
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        user.setPassword(bCryptPasswordEncoder.encode(userEditModel.getPassword()));

        user.setConfirmCode(null);

        this.userRepository.saveAndFlush(user);

        return "redirect:/login";
    }


    @Override
    public String loadEditView(@PathVariable Integer id, Model model){

        if(!this.userRepository.existsById(id)){
            return "redirect:/profile";
        }

        User user = this.userRepository.getOne(id);

        if(!isMyProfile(user)){
            return "redirect:/profile";
        }

        model.addAttribute("user", user);
        model.addAttribute("view", "/user/edit");

        return "base-layout";
    }

    @Override
    public String editUser(@PathVariable Integer id, UserEditModel userEditModel) throws IOException {

        if(!this.userRepository.existsById(id)){
            return "redirect:/profile";
        }

        User user = this.userRepository.getOne(id);

        if(!isMyProfile(user)){
            return "redirect:/profile";
        }

        if(!StringUtils.isEmpty(userEditModel.getPassword())
                && !StringUtils.isEmpty(userEditModel.getConfirmPassword())){
            if(userEditModel.getPassword().equals(userEditModel.getConfirmPassword())){
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

                user.setPassword(bCryptPasswordEncoder.encode(userEditModel.getPassword()));
            }
        }

        if(!userEditModel.getProfilePicture().isEmpty()){
            byte[] imageFile = userEditModel.getProfilePicture().getBytes();
            user.setProfilePicture(imageFile);
        }

        user.setFullName(userEditModel.getFullName());
        user.setEmail(userEditModel.getEmail());


        this.userRepository.saveAndFlush(user);

        return "redirect:/profile";
    }

    private boolean isMyProfile(User user){
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return user.getEmail().equals(currentUser.getUsername());
    }
}
