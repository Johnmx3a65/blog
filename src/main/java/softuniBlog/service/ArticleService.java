package softuniBlog.service;

import org.springframework.web.bind.annotation.PathVariable;
import softuniBlog.bindingModel.ArticleBindingModel;
import softuniBlog.entity.Article;
import softuniBlog.entity.User;

import java.io.IOException;

public interface ArticleService {

    void createArticle(ArticleBindingModel articleBindingModel) throws IOException;

    Article loadArticleDetailsView(@PathVariable Integer id);

    User addUserEntityToDetailsView();

    Article editArticle(Integer id, ArticleBindingModel articleBindingModel) throws IOException;

}
