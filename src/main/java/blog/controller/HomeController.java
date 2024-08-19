package blog.controller;

import blog.service.HomeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@AllArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/")
    public String index(Model model) {
        return this.homeService.loadIndexView(model);
    }

    @GetMapping("/category/{id}")
    public String listArticles(Model model, @PathVariable Integer id) {
        return this.homeService.loadListArticlesView(model, id);
    }

    @RequestMapping("/error/403")
    public String accessDenied(Model model){
        return this.homeService.loadError403View(model);
    }
}
