package blog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import blog.entity.Tag;
import blog.repository.TagRepository;
import blog.service.TagService;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public String loadArticlesWithTagView(Model model, @PathVariable String name){
        Tag tag = this.tagRepository.findByName(name);

        if(tag == null) {
            return "redirect:/";
        }

        model.addAttribute("view", "tag/articles");
        model.addAttribute("tag", tag);

        return "base-layout";
    }
}
