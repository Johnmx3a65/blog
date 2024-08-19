package blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import blog.entity.Article;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

}
