package softuniBlog.service;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

public interface TagService {
    void loadArticlesPictureFromDB(String name);
}
