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
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "tags")
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    private Integer id;

    private String name;

    private List<Article> articles;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    @Column(unique = true, nullable = false)
    public String getName() {
        return name;
    }

    @ManyToMany(mappedBy = "tags")
    public List<Article> getArticles() {
        return articles;
    }
}
