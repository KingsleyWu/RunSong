package kingsley.www.runsong.Http;

import java.util.Map;

import kingsley.www.runsong.entity.OnLineMusicList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/6/26 21:14
 * file change date : on 2017/6/26 21:14
 * version: 1.0
 */

public interface RetrofitService {
    @GET()
    Call<OnLineMusicList> getSongListInfo(@QueryMap Map<String,String> params);
}
