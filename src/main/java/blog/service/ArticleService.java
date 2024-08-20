package blog.service;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import blog.model.ArticleModel;

import java.io.IOException;

public interface ArticleService {

    String loadCreateArticleView(Model model);

    String createArticle(ArticleModel articleModel) throws IOException;

    String loadArticleDetailsView(Model model, Integer id);

    String loadArticleEditView(Integer id, Model model);

    String editArticle(Integer id, ArticleModel articleModel) throws IOException;

    String loadArticleDeleteView(Model model, Integer id);

    String deleteArticle(@PathVariable Integer id);
}
