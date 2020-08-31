package softuniBlog.service.impl;

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
import softuniBlog.bindingModel.UserBindingModel;
import softuniBlog.bindingModel.UserEditBindingModel;
import softuniBlog.entity.Article;
import softuniBlog.entity.Role;
import softuniBlog.entity.User;
import softuniBlog.repository.RoleRepository;
import softuniBlog.repository.UserRepository;
import softuniBlog.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
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

    public RoleRepository getRoleRepository() {
        return roleRepository;
    }

    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public MailSenderImpl getMailSender() {
        return mailSender;
    }

    public void setMailSender(MailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void registerUser(UserBindingModel userBindingModel) throws IOException {

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        User user = new User(
                userBindingModel.getEmail(),
                userBindingModel.getFullName(),
                bCryptPasswordEncoder.encode(userBindingModel.getPassword())
        );

        if(userBindingModel.getProfilePicture() != null){
            byte[] imageFile = userBindingModel.getProfilePicture().getBytes();
            user.setProfilePicture(imageFile);
        }

        Role userRole = getRoleRepository().findByName("ROLE_USER");
        user.addRole(userRole);

        getUserRepository().saveAndFlush(user);
    }

    @Override
    public void sendMail(UserBindingModel userBindingModel){
        User user = this.userRepository.findByEmail(userBindingModel.getEmail());

        user.setConfirmCode(UUID.randomUUID().toString());

        String message = String.format("Hello, %s!\n" +
                "If you tried to change your password, please go to the next link: http://localhost:8080/user/forgot-password/%s . Your confirm code: %s\n" +
                "If it's not you, please ignore this message!", user.getFullName(), user.getId().toString(), user.getConfirmCode());

        mailSender.send(user.getEmail(), "Change Password", message);
        getUserRepository().saveAndFlush(user);
    }

    @Override
    public void logoutFromPage(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null){
            new SecurityContextLogoutHandler().logout(request,response,auth);
        }
    }

    @Override
    public User loadUserInfoForProfilePageView() {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        User user = getUserRepository().findByEmail(principal.getUsername());

        if (user.getProfilePicture() != null) {
            user.setProfilePictureBase64(Base64.getEncoder().encodeToString(user.getProfilePicture()));
        }

        return user;
    }

    @Override
    public Set<Article> loadArticlesInfoForProfilePageView(User user){
        Set<Article> articles = user.getArticles();

        for (Article article : articles){
            if(article.getArticlePicture() != null){
                article.setArticlePictureBase64(Base64.getEncoder().encodeToString(article.getArticlePicture()));
            }
        }
        return articles;
    }

    @Override
    public User changeForgotPassword(Integer id, HttpServletRequest request, UserEditBindingModel userEditBindingModel){
        User user = getUserRepository().getOne(id);

        if(request.getParameter("sendAgain") != null){
            user.setConfirmCode(UUID.randomUUID().toString());

            String message = String.format("Hello, %s!\n" +
                    "Your confirm code: %s\n" +
                    "If it's not you, please ignore this message!", user.getFullName(), user.getConfirmCode());

            mailSender.send(user.getEmail(), "Change Password", message);
            getUserRepository().saveAndFlush(user);
            return user;
        }

        if(!userEditBindingModel.getPassword().equals(userEditBindingModel.getConfirmPassword()) || !userEditBindingModel.getConfirmCode().equals(user.getConfirmCode()))
            return user;

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        user.setPassword(bCryptPasswordEncoder.encode(userEditBindingModel.getPassword()));

        user.setConfirmCode(null);

        getUserRepository().saveAndFlush(user);

        return null;
    }

    @Override
    public void editUser(@PathVariable Integer id, UserEditBindingModel userEditBindingModel) throws IOException {

        User user = this.userRepository.getOne(id);

        if(!StringUtils.isEmpty(userEditBindingModel.getPassword())
                && !StringUtils.isEmpty(userEditBindingModel.getConfirmPassword())){
            if(userEditBindingModel.getPassword().equals(userEditBindingModel.getConfirmPassword())){
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

                user.setPassword(bCryptPasswordEncoder.encode(userEditBindingModel.getPassword()));
            }
        }

        if(!userEditBindingModel.getProfilePicture().isEmpty()){
            byte[] imageFile = userEditBindingModel.getProfilePicture().getBytes();
            user.setProfilePicture(imageFile);
        }

        user.setFullName(userEditBindingModel.getFullName());
        user.setEmail(userEditBindingModel.getEmail());


        getUserRepository().saveAndFlush(user);
    }

    protected boolean isMyProfile(User user){
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return user.getEmail().equals(currentUser.getUsername());
    }
}
