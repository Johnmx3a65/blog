package blog.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class ArticleModel {
    @NotNull
    private String title;

    @NotNull
    private String content;

    private Integer categoryId;

    private String tagString;

    private MultipartFile picture;
}
