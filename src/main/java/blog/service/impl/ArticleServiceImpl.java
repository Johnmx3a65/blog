package blog.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import blog.model.ArticleModel;
import blog.entity.Article;
import blog.entity.Category;
import blog.entity.Tag;
import blog.entity.User;
import blog.repository.ArticleRepository;
import blog.repository.CategoryRepository;
import blog.repository.TagRepository;
import blog.repository.UserRepository;
import blog.service.ArticleService;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final TagRepository tagRepository;

    @Override
    public String loadCreateArticleView(Model model){
        List<Category> categories = this.categoryRepository.findAll();

        model.addAttribute("categories", categories);
        model.addAttribute("view", "article/create");

        return "base-layout";
    }

    @Override
    public String createArticle(ArticleModel articleModel) throws IOException {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        User userEntity = this.userRepository.findByEmail(user.getUsername());
        Category category = this.categoryRepository.getOne(articleModel
                .getCategoryId());
        List<Tag> tags = this.findTagsFromString(articleModel.getTagString());

        Article articleEntity = Article
                .builder()
                .title(articleModel.getTitle())
                .content(articleModel.getContent())
                .author(userEntity)
                .category(category)
                .tags(tags)
                .build();

        if(articleModel.getArticlePicture() != null){
            byte[] imageFile = articleModel.getArticlePicture().getBytes();
            articleEntity.setArticlePicture(imageFile);
        }

        this.articleRepository.saveAndFlush(articleEntity);

        return "redirect:/";
    }

    @Override
    public String loadArticleDetailsView(Model model, @PathVariable Integer id){
        if(!this.articleRepository.existsById(id)){
            return "redirect:/";
        }

        if(!(SecurityContextHolder.getContext().getAuthentication()
                instanceof AnonymousAuthenticationToken)){
            UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();

            User entityUser = this.userRepository.findByEmail(principal.getUsername());

            model.addAttribute("user", entityUser);
        }

        Article article = this.articleRepository.getReferenceById(id);

        model.addAttribute("article", article);
        model.addAttribute("view", "article/details");

        return "base-layout";
    }

    @Override
    public String loadArticleEditView(@PathVariable Integer id, Model model){
        if(!this.articleRepository.existsById(id)){
            return "redirect:/";
        }

        Article article = this.articleRepository.getReferenceById(id);

        if (!isUserAuthorOrAdmin(article)){
            return "redirect:/article/" + id;
        }

        List<Category> categories = this.categoryRepository.findAll();

        String tagString = article.getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.joining(", "));

        model.addAttribute("view", "article/edit");
        model.addAttribute("article", article);
        model.addAttribute("categories", categories);
        model.addAttribute("tags", tagString);

        return "base-layout";
    }

    @Override
    public String editArticle(@PathVariable Integer id, ArticleModel articleModel) throws IOException {
        if(!this.articleRepository.existsById(id)){
            return "redirect:/";
        }

        Article article = this.articleRepository.getReferenceById(id);

        if (!isUserAuthorOrAdmin(article)){
            return "redirect:/article/" + id;
        }

        Category category = this.categoryRepository.getOne(articleModel.getCategoryId());
        List<Tag> tags = this.findTagsFromString(articleModel.getTagString());

        if(articleModel.getArticlePicture() != null){
            byte[] imageFile = articleModel.getArticlePicture().getBytes();
            article.setArticlePicture(imageFile);
        }

        article.setTags(tags);
        article.setCategory(category);
        article.setContent(articleModel.getContent());
        article.setTitle(articleModel.getTitle());

        this.articleRepository.saveAndFlush(article);

        return "redirect:/article/" + article.getId().toString();
    }

    @Override
    public String loadArticleDeleteView(Model model, @PathVariable Integer id){
        if(!this.articleRepository.existsById(id)){
            return "redirect:/";
        }

        Article article = this.articleRepository.getReferenceById(id);

        if (!isUserAuthorOrAdmin(article)){
            return "redirect:/article/" + id;
        }

        model.addAttribute("article", article);
        model.addAttribute("view", "article/delete");

        return "base-layout";
    }

    @Override
    public String deleteArticle(@PathVariable Integer id){
        if(!this.articleRepository.existsById(id)){
            return "redirect:/";
        }

        Article article = this.articleRepository.getReferenceById(id);
        if (!isUserAuthorOrAdmin(article)){
            return "redirect:/article/" + id;
        }
        this.articleRepository.delete(article);

        return "redirect:/";
    }

    private List<Tag> findTagsFromString(String tagString){
        List<Tag> tags = new LinkedList<>();

        String[] tagNames = tagString.split(",\\s*");

        for (String tagName : tagNames){
            Tag currentTag = this.tagRepository.findByName(tagName);

            if(currentTag == null){
                currentTag = Tag.builder().name(tagName).build();
                this.tagRepository.saveAndFlush(currentTag);
            }

            tags.add(currentTag);
        }
        return tags;
    }

    private boolean isUserAuthorOrAdmin(Article article){
        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User userEntity = this.userRepository.findByEmail(user.getUsername());

        return userEntity.isAdmin() || userEntity.isAuthor(article);
    }
}
