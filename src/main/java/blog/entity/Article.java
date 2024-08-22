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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "articles")
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    private Integer id;

    private String title;

    private String content;

    private User author;

    private Category category;

    private List<Tag> tags;

    private String picture;

    @ManyToMany()
    @JoinColumn(table = "articles_tags")
    public List<Tag> getTags() {
        return tags;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    @Column(nullable = false)
    public String getTitle() {
        return title;
    }

    @Column(columnDefinition = "text", nullable = false)
    public String getContent() {
        return content;
    }

    @ManyToOne()
    @JoinColumn(nullable = false, name = "authorId")
    public User getAuthor() {
        return author;
    }

    @ManyToOne()
    @JoinColumn(nullable = false, name = "categoryId")
    public Category getCategory() {
        return category;
    }

    @Column(name = "picture", columnDefinition = "text")
    public String getPicture() {
        return picture;
    }
}
