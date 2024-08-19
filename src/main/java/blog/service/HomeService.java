package blog.service;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

public interface HomeService {

    String loadIndexView(Model model);

    String loadListArticlesView(Model model, @PathVariable Integer id);

    String loadError403View(Model model);
}
