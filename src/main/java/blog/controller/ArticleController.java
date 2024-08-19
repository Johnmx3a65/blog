package blog.controller;

import blog.service.ArticleService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import blog.model.ArticleModel;

import java.io.IOException;

@Controller
@AllArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    public String create(Model model){
        return this.articleService.loadCreateArticleView(model);
    }

    @PostMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    public String createProcess(ArticleModel articleModel) throws IOException {
        return this.articleService.createArticle(articleModel);
    }

    @GetMapping("/article/{id}")
    public String details(Model model, @PathVariable Integer id){
        return this.articleService.loadArticleDetailsView(model, id);
    }

    @GetMapping("article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(@PathVariable Integer id, Model model){
        return this.articleService.loadArticleEditView(id, model);
    }

    @PostMapping("/article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editProcess(@PathVariable Integer id, ArticleModel articleModel) throws IOException {
        return this.articleService.editArticle(id, articleModel);
    }

    @GetMapping("/article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(Model model, @PathVariable Integer id){
        return this.articleService.loadArticleDeleteView(model, id);
    }

    @PostMapping("/article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteProcess(@PathVariable Integer id){
        return this.articleService.deleteArticle(id);
    }
}
