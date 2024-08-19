package blog.service;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

public interface TagService {

    String loadArticlesWithTagView(Model model, @PathVariable String name);
}
