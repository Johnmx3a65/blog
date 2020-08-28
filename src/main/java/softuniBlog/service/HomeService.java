package softuniBlog.service;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import softuniBlog.entity.Article;
import softuniBlog.entity.Category;
import softuniBlog.repository.CategoryRepository;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface HomeService {

    Set<Article> articlesInCategory(Integer id) throws IOException;

    String loadError403View(Model model);
}
