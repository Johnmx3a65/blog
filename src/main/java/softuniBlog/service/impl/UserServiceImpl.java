package softuniBlog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import softuniBlog.bindingModel.UserBindingModel;
import softuniBlog.bindingModel.UserEditBindingModel;
import softuniBlog.entity.Article;
import softuniBlog.entity.Role;
import softuniBlog.entity.User;
import softuniBlog.repository.CategoryRepository;
import softuniBlog.repository.RoleRepository;
import softuniBlog.repository.UserRepository;
import softuniBlog.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailSenderImpl mailSender;


    @Override
    public String loadRegisterView(Model model){
        model.addAttribute("view", "user/register");

        return "base-layout";
    }

    @Override
    public String registerUser(UserBindingModel userBindingModel) throws IOException {

        if(!userBindingModel.getPassword().equals(userBindingModel.getConfirmPassword())){
            return "redirect:/register";
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        User user = new User(
                userBindingModel.getEmail(),
                userBindingModel.getFullName(),
                bCryptPasswordEncoder.encode(userBindingModel.getPassword())
        );

        if(userBindingModel.getProfilePicture() != null){
            byte[] imageFile = userBindingModel.getProfilePicture().getBytes();
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + userBindingModel.getProfilePicture().getOriginalFilename();
            user.setImageName(resultFileName);
            user.setProfilePicture(imageFile);
        }

        Role userRole = this.roleRepository.findByName("ROLE_USER");
        user.addRole(userRole);

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
    public String sendMail(UserBindingModel userBindingModel){
        if(userBindingModel.getEmail().isEmpty()){
            return "redirect:/forgot-password-input-email";
        }

        if(this.userRepository.findByEmail(userBindingModel.getEmail()) == null){
            return "redirect:/forgot-password-input-email";
        }
        User user = this.userRepository.findByEmail(userBindingModel.getEmail());

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
    public String loadProfilePageView(Model model) throws IOException {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        User user = this.userRepository.findByEmail(principal.getUsername());

        if(user.getImageName() != null && user.getProfilePicture() != null){
            String filePath = "E:/Programming/blog/target/classes/static/image/" + user.getImageName();
            if(!(new File(filePath).isFile())){
                File profileImage = new File(filePath);
                if(profileImage.createNewFile()){
                    FileOutputStream fos = new FileOutputStream(filePath);
                    fos.write(user.getProfilePicture());
                    fos.close();
                }
            }
        }
        Set<Article> articles = user.getArticles();

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
    public String changeForgotPassword(@PathVariable Integer id, HttpServletRequest request, UserEditBindingModel userEditBindingModel){

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

        if(!userEditBindingModel.getPassword().equals(userEditBindingModel.getConfirmPassword()) || !userEditBindingModel.getConfirmCode().equals(user.getConfirmCode())){
            return "redirect:/user/forgot-password/" + user.getId();
        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        user.setPassword(bCryptPasswordEncoder.encode(userEditBindingModel.getPassword()));

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
    public String editUser(@PathVariable Integer id, UserEditBindingModel userEditBindingModel) throws IOException {

        if(!this.userRepository.existsById(id)){
            return "redirect:/profile";
        }

        User user = this.userRepository.getOne(id);

        if(!isMyProfile(user)){
            return "redirect:/profile";
        }

        if(!StringUtils.isEmpty(userEditBindingModel.getPassword())
                && !StringUtils.isEmpty(userEditBindingModel.getConfirmPassword())){
            if(userEditBindingModel.getPassword().equals(userEditBindingModel.getConfirmPassword())){
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

                user.setPassword(bCryptPasswordEncoder.encode(userEditBindingModel.getPassword()));
            }
        }

        if(!userEditBindingModel.getProfilePicture().isEmpty()){
            byte[] imageFile = userEditBindingModel.getProfilePicture().getBytes();
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + userEditBindingModel.getProfilePicture().getOriginalFilename();
            user.setImageName(resultFileName);
            user.setProfilePicture(imageFile);
        }

        user.setFullName(userEditBindingModel.getFullName());
        user.setEmail(userEditBindingModel.getEmail());


        this.userRepository.saveAndFlush(user);

        return "redirect:/profile";
    }

    private boolean isMyProfile(User user){
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return user.getEmail().equals(currentUser.getUsername());
    }
}
