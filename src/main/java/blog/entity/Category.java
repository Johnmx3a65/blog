package blog.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "categories")
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private Integer id;

    private String name;

    private List<Article> articles;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    @Column(nullable = false, unique = true)
    public String getName() {
        return name;
    }

    @OneToMany(mappedBy = "category")
    public List<Article> getArticles() {
        return articles;
    }
}
