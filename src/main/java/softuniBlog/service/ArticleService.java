package softuniBlog.service;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import softuniBlog.bindingModel.ArticleBindingModel;
import softuniBlog.entity.Article;
import softuniBlog.entity.User;

import java.io.IOException;

public interface ArticleService {

    void createArticle(ArticleBindingModel articleBindingModel) throws IOException;

    Article loadArticleDetailsView(@PathVariable Integer id);

    User addUserEntityToDetailsView();

    String loadArticleEditView(@PathVariable Integer id, Model model);

    String editArticle(@PathVariable Integer id, ArticleBindingModel articleBindingModel) throws IOException;

    String loadArticleDeleteView(Model model, @PathVariable Integer id);

    String deleteArticle(@PathVariable Integer id);

}
