package softuniBlog.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import softuniBlog.bindingModel.ArticleBindingModel;
import softuniBlog.entity.Article;
import softuniBlog.entity.Tag;
import softuniBlog.entity.User;
import softuniBlog.service.impl.ArticleServiceImpl;

import java.io.IOException;
import java.util.stream.Collectors;

@Controller
public class ArticleController extends ArticleServiceImpl {

    @GetMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    public String create(Model model){

        model.addAttribute("categories", getCategoryRepository().findAll());
        model.addAttribute("view", "article/create");

        return "base-layout";
    }

    @PostMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    public String createProcess(ArticleBindingModel articleBindingModel) throws IOException {

        createArticle(articleBindingModel);

        return "redirect:/";
    }

    @GetMapping("/article/{id}")
    public String details(Model model, @PathVariable Integer id){

        if(!this.getArticleRepository().existsById(id)){
            return "redirect:/";
        }
        User user = addUserEntityToDetailsView();
        if (user != null){
            model.addAttribute("user", user);
        }

        model.addAttribute("article", loadArticleDetailsView(id));
        model.addAttribute("view", "article/details");

        return "base-layout";
    }

    @GetMapping("article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(@PathVariable Integer id, Model model){
        if(!getArticleRepository().existsById(id)){
            return "redirect:/";
        }
        Article article = getArticleRepository().getOne(id);
        if (!isUserAuthorOrAdmin(article)){
            return "redirect:/article/" + id;
        }

        model.addAttribute("view", "article/edit");
        model.addAttribute("article", article);
        model.addAttribute("categories", getCategoryRepository().findAll());
        model.addAttribute("tags", article.getTags().stream().map(Tag::getName).collect(Collectors.joining(", ")));

        return "base-layout";
    }

    @PostMapping("/article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editProcess(@PathVariable Integer id, ArticleBindingModel articleBindingModel) throws IOException {
        if(!getArticleRepository().existsById(id)){
            return "redirect:/";
        }
        if (!isUserAuthorOrAdmin(getArticleRepository().getOne(id))){
            return "redirect:/article/" + id;
        }
        return "redirect:/article/" + editArticle(id, articleBindingModel).getId().toString();
    }

    @GetMapping("/article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(Model model, @PathVariable Integer id){
        if(!getArticleRepository().existsById(id)){
            return "redirect:/";
        }
        Article article = getArticleRepository().getOne(id);
        if (!isUserAuthorOrAdmin(article)){
            return "redirect:/article/" + id;
        }
        model.addAttribute("article", article);
        model.addAttribute("view", "article/delete");

        return "base-layout";
    }

    @PostMapping("/article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteProcess(@PathVariable Integer id){
        if(!getArticleRepository().existsById(id)){
            return "redirect:/";
        }
        Article article = getArticleRepository().getOne(id);
        if (!isUserAuthorOrAdmin(article)){
            return "redirect:/article/" + id;
        }
        getArticleRepository().delete(article);
        return "redirect:/";
    }
}
