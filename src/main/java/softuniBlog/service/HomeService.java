package softuniBlog.service;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

public interface HomeService {

    String loadIndexView(Model model);

    String loadListArticlesView(Model model, @PathVariable Integer id) throws IOException;

    String loadError403View(Model model);
}
