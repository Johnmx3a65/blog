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
        model.addAttribute("view", "home/index");
        model.addAttribute("categories", getCategoryRepository().findAll());

        return "base-layout";
    }

    @GetMapping("/category/{id}")
    public String listArticles(Model model, @PathVariable Integer id) throws IOException {

        if(!getCategoryRepository().existsById(id)){
            return "redirect:/";
        }

        model.addAttribute("articles", articlesInCategory(id));
        model.addAttribute("category", getCategoryRepository().getOne(id));
        model.addAttribute("view", "home/list-articles");

        return "base-layout";
    }

    @RequestMapping("/error/403")
    public String accessDenied(Model model){
        return loadError403View(model);
    }
}
