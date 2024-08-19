package blog.controller;

import blog.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HomeController {

    private final HomeService homeService;

    @Autowired
    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

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
