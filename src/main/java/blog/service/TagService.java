package blog.service;

import org.springframework.ui.Model;

public interface TagService {

    String loadArticlesWithTagView(Model model, String name);
}
