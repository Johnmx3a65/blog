package blog.util;

public class StringUtils {

    private StringUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static final String ROLE_USER = "ROLE_USER";

    public static final String USER = "user";
    public static final String USERS = "users";
    public static final String ROLES = "roles";
    public static final String CATEGORY = "category";
    public static final String CATEGORIES = "categories";
    public static final String ARTICLE = "article";
    public static final String ARTICLES = "articles";
    public static final String TAG = "tag";
    public static final String TAGS = "tags";
    public static final String VIEW = "view";

    public static final String BASE_LAYOUT = "base-layout";
    public static final String REDIRECT_ADMIN_USERS = "redirect:/admin/users/";
    public static final String REDIRECT_HOME = "redirect:/";
    public static final String REDIRECT_ARTICLES_ID = "redirect:/article/{0}";
    public static final String REDIRECT_ADMIN_CATEGORIES_CREATE = "redirect:/admin/categories/create";
    public static final String REDIRECT_ADMIN_CATEGORIES = "redirect:/admin/categories/";
    public static final String REDIRECT_REGISTER = "redirect:/register";
    public static final String REDIRECT_LOGIN = "redirect:/login";
    public static final String REDIRECT_LOGIN_WITH_QUERY = "redirect:/login?{0}";
    public static final String REDIRECT_SEND_MAIL = "redirect:/send-mail";
    public static final String REDIRECT_USER_FORGOT_PASSWORD_ID = "redirect:/forgot-password/{0}";
    public static final String REDIRECT_PROFILE = "redirect:/profile";

    public static final String ADMIN_USERS_LIST = "admin/users/list";
    public static final String ADMIN_USERS_EDIT = "admin/users/edit";
    public static final String ADMIN_USERS_DELETE = "admin/users/delete";

    public static final String ADMIN_CATEGORIES_LIST = "admin/categories/list";
    public static final String ADMIN_CATEGORIES_CREATE = "admin/categories/create";
    public static final String ADMIN_CATEGORIES_EDIT = "admin/categories/edit";
    public static final String ADMIN_CATEGORIES_DELETE = "admin/categories/delete";

    public static final String ARTICLE_CREATE = "article/create";
    public static final String ARTICLE_DETAILS = "article/details";
    public static final String ARTICLE_EDIT = "article/edit";
    public static final String ARTICLE_DELETE = "article/delete";

    public static final String HOME_INDEX = "home/index";
    public static final String HOME_LIST_ARTICLES = "home/list-articles";

    public static final String TAG_ARTICLES = "tag/articles";

    public static final String USER_REGISTER = "user/register";
    public static final String USER_LOGIN = "user/login";
    public static final String USER_FORGOT_PASSWORD = "user/forgot-password";
    public static final String USER_FORGOT_PASSWORD_INPUT_EMAIL = "user/forgot-password-input-email";
    public static final String USER_PROFILE = "user/profile";
    public static final String USER_SEND_MAIL = "user/send-mail";
    public static final String USER_EDIT = "user/edit";

    public static final String ERROR_403 = "error/403";

    public static final String INVALID_CATEGORY_ID = "Invalid category id: {0}";
    public static final String INVALID_USERNAME = "Invalid username: {0}";

    public static final String CHANGE_PASSWORD = "Change Password";

    public static final String LOGOUT = "logout";
    public static final String SEND_AGAIN = "sendAgain";

    public static final String CONFIRMATION_CODE_MAIL = "Hello, {0}! If you tried to change your password, please go to the next link: http://localhost:8080/user/forgot-password/{1} . Your confirm code: {2} If its not you, please ignore this message!";
    public static final String NEW_CONFIRMATION_CODE_MAIL = "Hello, {0}! Your confirm code: {1}. If its not you, please ignore this message!";
}
