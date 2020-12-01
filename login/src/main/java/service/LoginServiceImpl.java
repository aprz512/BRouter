package service;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.aprz.brouter.annotation.Route;
import com.example.module_login_export.service.LoginResponseBean;
import com.example.module_login_export.service.LoginService;

@Route(path = "login/loginServiceImpl")
public class LoginServiceImpl implements LoginService {
    Context mContext;

    @Override
    public LoginResponseBean login(String userName, String psw) {
        LoginResponseBean responseBean = new LoginResponseBean();
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(psw)) {
            responseBean.setErrorMsg("无效的用户名或密码");
            return responseBean;
        }

        if (userName.equals("admin") && psw.equals("admin")) {
            responseBean.setUserName(userName);
            responseBean.setSuccess(true);
            return responseBean;
        }
        responseBean.setErrorMsg("无效的用户名或密码");
        return responseBean;
    }


    @Override
    public boolean isUserNameValidate(String userName) {
        return userName.equals("admin");
    }

    @Override
    public void init(Context context) {
        this.mContext = context;
    }
}
