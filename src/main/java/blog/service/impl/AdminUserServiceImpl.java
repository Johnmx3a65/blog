package blog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import blog.model.UserEditModel;
import blog.entity.Role;
import blog.entity.User;
import blog.repository.ArticleRepository;
import blog.repository.RoleRepository;
import blog.repository.UserRepository;
import blog.service.AdminUserService;

import java.util.LinkedList;
import java.util.List;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;

    private final ArticleRepository articleRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public AdminUserServiceImpl(UserRepository userRepository, ArticleRepository articleRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public String loadlistUsersView(Model model){
        List<User> users = this.userRepository.findAll();

        model.addAttribute("users", users);
        model.addAttribute("view", "admin/users/list");

        return "base-layout";
    }

    @Override
    public String loadUserEditView(@PathVariable Integer id, Model model){
        if(!this.userRepository.existsById(id)){
            return "redirect:/admin/users/";
        }

        User user = this.userRepository.getOne(id);
        List<Role>roles = this.roleRepository.findAll();

        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        model.addAttribute("view", "admin/users/edit");

        return "base-layout";
    }

    @Override
    public String editUser(@PathVariable Integer id, UserEditModel userEditModel){
        if(!this.userRepository.existsById(id)){
            return "redirect:/admin/users/";
        }

        User user = this.userRepository.getOne(id);

        if(!StringUtils.isEmpty(userEditModel.getPassword())
                && !StringUtils.isEmpty(userEditModel.getConfirmPassword())){
            if(userEditModel.getPassword().equals(userEditModel.getConfirmPassword())){
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

                user.setPassword(bCryptPasswordEncoder.encode(userEditModel.getPassword()));
            }
        }

        user.setFullName(userEditModel.getFullName());
        user.setEmail(userEditModel.getEmail());

        List<Role> roles = new LinkedList<>();

        for (Integer roleId : userEditModel.getRoles()){
            roles.add(this.roleRepository.getOne(roleId));
        }

        user.setRoles(roles);

        this.userRepository.saveAndFlush(user);

        return "redirect:/admin/users/";
    }

    @Override
    public String loadUserDeleteView(@PathVariable Integer id, Model model){
        if(!this.userRepository.existsById(id)){
            return "redirect:/admin/users";
        }

        User user = this.userRepository.getOne(id);

        model.addAttribute("user", user);
        model.addAttribute("view", "admin/users/delete");

        return "base-layout";
    }

    @Override
    public String deleteUser(@PathVariable Integer id){
        if(!this.userRepository.existsById(id)){
            return "redirect:/admin/users/";
        }

        User user = this.userRepository.getOne(id);

        this.articleRepository.deleteAll(user.getArticles());

        this.userRepository.delete(user);

        return "redirect:/admin/users/";
    }
}
