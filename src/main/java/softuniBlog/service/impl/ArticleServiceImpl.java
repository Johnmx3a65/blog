package softuniBlog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import softuniBlog.bindingModel.ArticleBindingModel;
import softuniBlog.entity.Article;
import softuniBlog.entity.Category;
import softuniBlog.entity.Tag;
import softuniBlog.entity.User;
import softuniBlog.repository.ArticleRepository;
import softuniBlog.repository.CategoryRepository;
import softuniBlog.repository.TagRepository;
import softuniBlog.repository.UserRepository;
import softuniBlog.service.ArticleService;

import java.io.IOException;
import java.util.Base64;
import java.util.HashSet;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TagRepository tagRepository;

    public ArticleRepository getArticleRepository() {
        return articleRepository;
    }

    public void setArticleRepository(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CategoryRepository getCategoryRepository() {
        return categoryRepository;
    }

    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public TagRepository getTagRepository() {
        return tagRepository;
    }

    public void setTagRepository(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public void createArticle(ArticleBindingModel articleBindingModel) throws IOException {

        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        User userEntity = getUserRepository().findByEmail(user.getUsername());
        Category category = getCategoryRepository().getOne(articleBindingModel
                .getCategoryId());
        HashSet<Tag> tags = this.findTagsFromString(articleBindingModel.getTagString());

        Article articleEntity = new Article(
                articleBindingModel.getTitle(),
                articleBindingModel.getContent(),
                userEntity,
                category,
                tags
        );

        if(articleBindingModel.getArticlePicture() != null){
            byte[] imageFile = articleBindingModel.getArticlePicture().getBytes();
            articleEntity.setArticlePicture(imageFile);
        }

        getArticleRepository().saveAndFlush(articleEntity);
    }

    @Override
    public Article loadArticleDetailsView(Integer id){

        Article article = getArticleRepository().getOne(id);

        if(article.getArticlePicture() != null){
            article.setArticlePictureBase64(Base64.getEncoder().encodeToString(article.getArticlePicture()));
        }
        return  article;

    }

    @Override
    public User addUserEntityToDetailsView(){
        if(!(SecurityContextHolder.getContext().getAuthentication()
                instanceof AnonymousAuthenticationToken)){

            UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();

            return getUserRepository().findByEmail(principal.getUsername());
        }
        return null;
    }

    @Override
    public Article editArticle(Integer id, ArticleBindingModel articleBindingModel) throws IOException {

        Article article = getArticleRepository().getOne(id);

        Category category = getCategoryRepository().getOne(articleBindingModel.getCategoryId());
        HashSet<Tag> tags = this.findTagsFromString(articleBindingModel.getTagString());

        if(articleBindingModel.getArticlePicture() != null){
            byte[] imageFile = articleBindingModel.getArticlePicture().getBytes();
            article.setArticlePicture(imageFile);
        }

        article.setTags(tags);
        article.setCategory(category);
        article.setContent(articleBindingModel.getContent());
        article.setTitle(articleBindingModel.getTitle());

        getArticleRepository().saveAndFlush(article);

        return  article;
    }

    private HashSet<Tag> findTagsFromString(String tagString){
        HashSet<Tag> tags = new HashSet<>();

        String[] tagNames = tagString.split(",\\s*");

        for (String tagName : tagNames){
            Tag currentTag = this.tagRepository.findByName(tagName);

            if(currentTag == null){
                currentTag = new Tag(tagName);
                this.tagRepository.saveAndFlush(currentTag);
            }

            tags.add(currentTag);
        }
        return tags;
    }

    protected boolean isUserAuthorOrAdmin(Article article){
        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User userEntity = this.userRepository.findByEmail(user.getUsername());

        return userEntity.isAdmin() || userEntity.isAuthor(article);
    }
}
