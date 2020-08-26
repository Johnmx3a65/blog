package softuniBlog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import softuniBlog.service.impl.HomeServiceImpl;

import java.io.IOException;


@Controller
public class HomeController extends HomeServiceImpl {

    @GetMapping("/")
    public String index(Model model) {
        return loadIndexView(model);
    }

    @GetMapping("/category/{id}")
    public String listArticles(Model model, @PathVariable Integer id) throws IOException {
        return loadListArticlesView(model, id);
    }

    @RequestMapping("/error/403")
    public String accessDenied(Model model){
        return loadError403View(model);
    }
}
