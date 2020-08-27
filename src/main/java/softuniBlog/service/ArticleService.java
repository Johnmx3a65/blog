package softuniBlog.service;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import softuniBlog.bindingModel.ArticleBindingModel;

import java.io.IOException;

public interface ArticleService {

    String loadCreateArticleView(Model model);

    String createArticle(ArticleBindingModel articleBindingModel) throws IOException;

    String loadArticleDetailsView(Model model, @PathVariable Integer id);

    String loadArticleEditView(@PathVariable Integer id, Model model);

    String editArticle(@PathVariable Integer id, ArticleBindingModel articleBindingModel) throws IOException;

    String loadArticleDeleteView(Model model, @PathVariable Integer id);

    String deleteArticle(@PathVariable Integer id);

}
