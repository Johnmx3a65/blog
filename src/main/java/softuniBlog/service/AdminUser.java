package softuniBlog.service;

import softuniBlog.bindingModel.UserEditBindingModel;

public interface AdminUser {

    void editUser(Integer id, UserEditBindingModel userEditBindingModel);

    void deleteUser(Integer id);
}
