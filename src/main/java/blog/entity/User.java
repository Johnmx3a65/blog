package blog.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Integer id;

    private String email;

    private String fullName;

    private String password;

    private List<Role> roles;

    private List<Article> articles;

    private byte[] profilePicture;

    private String profilePictureBase64;

    private String confirmCode;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    @Column(name = "email", unique = true, nullable = false)
    public String getEmail() {
        return email;
    }

    @Column(name = "fullName", nullable = false)
    public String getFullName() {
        return fullName;
    }

    @Column(name = "password", length = 60, nullable = false)
    public String getPassword() {
        return password;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles")
    public List<Role> getRoles() {
        return roles;
    }

    @OneToMany(mappedBy = "author")
    public List<Article> getArticles() {
        return articles;
    }

    @Column(name = "profilePicture")
    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public String getProfilePictureBase64() {
        return profilePictureBase64;
    }

    @Column(name = "confirmCode")
    public String getConfirmCode() {
        return confirmCode;
    }

    @Transient
    public boolean isAdmin(){
        return this.getRoles()
                .stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
    }

    @Transient
    public boolean isAuthor(Article article){
        return Objects.equals(this.getId(), article.getAuthor().getId());
    }
}