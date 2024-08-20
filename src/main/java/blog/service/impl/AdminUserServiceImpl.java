package blog.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import blog.model.UserEditModel;
import blog.entity.Role;
import blog.entity.User;
import blog.repository.ArticleRepository;
import blog.repository.RoleRepository;
import blog.repository.UserRepository;
import blog.service.AdminUserService;

import java.util.LinkedList;
import java.util.List;

import static blog.util.StringUtils.ADMIN_USERS_DELETE;
import static blog.util.StringUtils.ADMIN_USERS_EDIT;
import static blog.util.StringUtils.ADMIN_USERS_LIST;
import static blog.util.StringUtils.BASE_LAYOUT;
import static blog.util.StringUtils.REDIRECT_ADMIN_USERS;
import static blog.util.StringUtils.ROLES;
import static blog.util.StringUtils.USER;
import static blog.util.StringUtils.USERS;
import static blog.util.StringUtils.VIEW;

@Service
@AllArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;

    private final ArticleRepository articleRepository;

    private final RoleRepository roleRepository;

    @Override
    public String loadListUsersView(Model model){
        List<User> users = this.userRepository.findAll();

        model.addAttribute(USERS, users);
        model.addAttribute(VIEW, ADMIN_USERS_LIST);

        return BASE_LAYOUT;
    }

    @Override
    public String loadUserEditView(Integer id, Model model){
        if(!this.userRepository.existsById(id)){
            return REDIRECT_ADMIN_USERS;
        }

        User user = this.userRepository.getReferenceById(id);
        List<Role>roles = this.roleRepository.findAll();

        model.addAttribute(USER, user);
        model.addAttribute(ROLES, roles);
        model.addAttribute(VIEW, ADMIN_USERS_EDIT);

        return BASE_LAYOUT;
    }

    @Override
    public String editUser(Integer id, UserEditModel userEditModel){
        if(!this.userRepository.existsById(id)){
            return REDIRECT_ADMIN_USERS;
        }

        User user = this.userRepository.getReferenceById(id);

        String password = userEditModel.getPassword();
        String confirmPassword = userEditModel.getConfirmPassword();

        if(!(password.isEmpty() || confirmPassword.isEmpty()) && password.equals(confirmPassword)){
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                user.setPassword(bCryptPasswordEncoder.encode(userEditModel.getPassword()));
        }

        user.setFullName(userEditModel.getFullName());
        user.setEmail(userEditModel.getEmail());

        List<Role> roles = new LinkedList<>();

        for (Integer roleId : userEditModel.getRoles()){
            roles.add(this.roleRepository.getReferenceById(roleId));
        }

        user.setRoles(roles);

        this.userRepository.saveAndFlush(user);

        return REDIRECT_ADMIN_USERS;
    }

    @Override
    public String loadUserDeleteView(Integer id, Model model){
        if(!this.userRepository.existsById(id)){
            return REDIRECT_ADMIN_USERS;
        }

        User user = this.userRepository.getReferenceById(id);

        model.addAttribute(USER, user);
        model.addAttribute(VIEW, ADMIN_USERS_DELETE);

        return BASE_LAYOUT;
    }

    @Override
    public String deleteUser(Integer id){
        if(!this.userRepository.existsById(id)){
            return REDIRECT_ADMIN_USERS;
        }

        User user = this.userRepository.getReferenceById(id);

        this.articleRepository.deleteAll(user.getArticles());

        this.userRepository.delete(user);

        return REDIRECT_ADMIN_USERS;
    }
}
