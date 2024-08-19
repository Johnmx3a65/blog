package blog.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class UserEditModel extends UserModel {
    private List<Integer> roles;

    private String confirmCode;

    public UserEditModel(){this.roles = new ArrayList<>();}
}
