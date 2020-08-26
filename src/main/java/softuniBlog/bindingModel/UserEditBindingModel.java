package softuniBlog.bindingModel;

import java.util.ArrayList;
import java.util.List;

public class UserEditBindingModel extends UserBindingModel{
    private List<Integer> roles;

    private String confirmCode;

    public UserEditBindingModel(){this.roles = new ArrayList<>();}

    public List<Integer> getRoles(){return roles;}

    public void setRoles(List<Integer>roles){this.roles = roles;}

    public String getConfirmCode() {
        return confirmCode;
    }

    public void setConfirmCode(String confirmCode) {
        this.confirmCode = confirmCode;
    }
}
