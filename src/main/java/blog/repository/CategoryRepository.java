package blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import blog.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
