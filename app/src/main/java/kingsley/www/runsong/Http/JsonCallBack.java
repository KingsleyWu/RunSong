package kingsley.www.runsong.Http;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/6/6 09:00
 * file change date : on 2017/6/6 09:00
 * version: 1.0
 */

public abstract class JsonCallBack<T> extends Callback<T> {
    private static final String TAG = "JsonCallBack";
    private Class<T> mClass;

    public JsonCallBack(Class<T> tClass){
        mClass = tClass;
    }
    @Override
    public T parseNetworkResponse(Response response, int id) throws Exception {
        String jsonStr = response.body().string();
        //返回结果
        //Log.i(TAG, "parseNetworkResponse: jsonStr"+jsonStr);
        return new Gson().fromJson(jsonStr,mClass);
    }
}
