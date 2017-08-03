package kingsley.www.runsong.application;

import android.app.Application;

import com.mob.MobSDK;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import kingsley.www.runsong.Http.HttpInterceptor;
import kingsley.www.runsong.utils.Preferences;
import okhttp3.OkHttpClient;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/6/5 20:44
 * file change date : on 2017/6/5 20:44
 * version: 1.0
 */

public class MusicApplication extends Application {
    public static MusicApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MobSDK.init(this, "1fe9e4f8dc55f", "bf5889c045df9df4cdb446a4fee7fa7f");
        initOkHttpUtils();
        initSharePreferences();
    }

    private void initOkHttpUtils() {
        //初始化OkHttpClient,并设置请求属性
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new HttpInterceptor())
                .build();
        //关联OkHttpClient
        OkHttpUtils.initClient(okHttpClient);
    }
    //初始化偏好设置context
    private void initSharePreferences(){
        Preferences.init(this);
    }
}
