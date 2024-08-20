package blog.service;

import org.springframework.ui.Model;

public interface HomeService {

    String loadIndexView(Model model);

    String loadListArticlesView(Model model, Integer id);

    String loadError403View(Model model);
}
