package kingsley.www.runsong.Http;

import java.util.HashMap;
import java.util.Map;

import kingsley.www.runsong.entity.OnLineMusicList;
import kingsley.www.runsong.m_interface.IConstant;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static kingsley.www.runsong.m_interface.IConstant.METHOD_GET_MUSIC_LIST;
import static kingsley.www.runsong.m_interface.IConstant.PARAM_METHOD;
import static kingsley.www.runsong.m_interface.IConstant.PARAM_OFFSET;
import static kingsley.www.runsong.m_interface.IConstant.PARAM_SIZE;
import static kingsley.www.runsong.m_interface.IConstant.PARAM_TYPE;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/6/26 20:51
 * file change date : on 2017/6/26 20:51
 * version: 1.0
 */

public class RetrofitClient {
    private static RetrofitClient INSTANCE;
    private RetrofitService retrofitService;

    public static RetrofitClient getInstance(){
        if (INSTANCE ==null){
            synchronized (RetrofitClient.class){
                if (INSTANCE == null){
                    INSTANCE = new RetrofitClient();
                }
            }
        }
        return INSTANCE;
    }

    private RetrofitClient(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new HttpInterceptor()).build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(IConstant.BASE_URL).client(okHttpClient).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitService = retrofit.create(RetrofitService.class);
    }

    public void getSongListInfo(String type, int size, int offset , Callback<OnLineMusicList> callback){
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_METHOD,METHOD_GET_MUSIC_LIST);
        params.put(PARAM_TYPE,type);
        params.put(PARAM_SIZE, String.valueOf(size));
        params.put(PARAM_OFFSET,String.valueOf(offset));
        Call<OnLineMusicList> onLineMusicListCall = retrofitService.getSongListInfo(params);
        onLineMusicListCall.enqueue(callback);
    }
}
