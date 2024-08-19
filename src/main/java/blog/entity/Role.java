package blog.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Entity
@Table(name = "roles")
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    private Integer id;

    private String name;

    private List<User> users;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    @ManyToMany(mappedBy = "roles")
    public List<User> getUsers() {
        return users;
    }

    @Transient
    public String getSimpleName(){
        return StringUtils.capitalize(this.getName().substring(5).toLowerCase());
    }
}