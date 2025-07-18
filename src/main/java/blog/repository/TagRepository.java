package blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import blog.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    Tag findByName(String name);
}
