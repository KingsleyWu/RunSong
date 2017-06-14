package kingsley.www.runsong.Http;

import android.os.Build;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/6/5 20:48
 * file change date : on 2017/6/5 20:48
 * version: 1.0
 */

public class HttpInterceptor implements Interceptor {
    //一些网站常常通过判断 UA 来给不同的操作系统、不同的浏览器发送不同的页面，
    // 因此可能造成某些页面无法在某个浏览器中正常显示，
    // 但通过伪装 UA 可以绕过检测。
    private static final String UA = "User-Agent";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .addHeader(UA, makeUA())
                .build();
        return chain.proceed(request);
    }

    private String makeUA() {
        return Build.BRAND + "/" + Build.MODEL + "/" + Build.VERSION.RELEASE;
    }
}
