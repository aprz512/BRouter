package service;

import android.content.Context;

import com.aprz.brouter.annotation.Route;
import com.example.module_login_export.service.LoginResponseBean;
import com.example.module_login_export.service.LoginService;

@Route(path = "login/loginServiceImpl")
public class LoginServiceImpl implements LoginService {
    Context mContext;

    @Override
    public LoginResponseBean login(String userName, String psw) {
        if (userName.equals("peng")) {
            return new LoginResponseBean(userName);
        }
        return null;
    }

    @Override
    public boolean isUserNameValidate(String userName) {
        return userName.equals("peng");
    }

    @Override
    public void init(Context context) {
        this.mContext = context;
    }
}
