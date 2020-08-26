package softuniBlog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import softuniBlog.service.impl.TagServiceImpl;

@Controller
public class TagController extends TagServiceImpl {

    @GetMapping("/tag/{name}")
    public String articlesWithTag(Model model, @PathVariable String name){
        return loadArticlesWithTagView(model, name);
    }
}
