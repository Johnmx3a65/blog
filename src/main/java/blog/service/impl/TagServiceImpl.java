package blog.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import blog.entity.Tag;
import blog.repository.TagRepository;
import blog.service.TagService;

import static blog.util.StringUtils.BASE_LAYOUT;
import static blog.util.StringUtils.REDIRECT_HOME;
import static blog.util.StringUtils.TAG;
import static blog.util.StringUtils.TAG_ARTICLES;
import static blog.util.StringUtils.VIEW;

@Service
@AllArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public String loadArticlesWithTagView(Model model, String name){
        Tag tag = this.tagRepository.findByName(name);

        if(tag == null) {
            return REDIRECT_HOME;
        }

        model.addAttribute(VIEW, TAG_ARTICLES);
        model.addAttribute(TAG, tag);

        return BASE_LAYOUT;
    }
}
