package service;

import android.content.Context;

import com.aprz.user.UserManager;

/**
 * 获取ApplicationContext
 */
public class AppService {
    //为ApplicationContext
    private Context mContext;
    private boolean hasInit = false;

    private AppService() {
    }

    private static volatile AppService instance;

    public static AppService getInstance() {
        if (instance == null) {
            synchronized (AppService.class) {
                if (instance == null) {
                    instance = new AppService();
                }
            }
        }
        return instance;
    }

    public Context getContext() {
        if (!hasInit) {
            throw new IllegalStateException("you must init first");
        }
        return mContext;
    }

    public void initContext(Context context) {
        hasInit = true;
        mContext = context;
    }
}
