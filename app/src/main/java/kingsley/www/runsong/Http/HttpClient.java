package kingsley.www.runsong.Http;

import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import kingsley.www.runsong.cache.AppCache;
import kingsley.www.runsong.entity.DownloadInfo;
import kingsley.www.runsong.entity.Music;
import kingsley.www.runsong.entity.OnLineMusic;
import kingsley.www.runsong.entity.OnLineMusicList;
import okhttp3.Call;

import static kingsley.www.runsong.m_interface.IConstant.BASE_URL;
import static kingsley.www.runsong.m_interface.IConstant.METHOD_DOWNLOAD_MUSIC;
import static kingsley.www.runsong.m_interface.IConstant.METHOD_GET_MUSIC_LIST;
import static kingsley.www.runsong.m_interface.IConstant.PARAM_METHOD;
import static kingsley.www.runsong.m_interface.IConstant.PARAM_OFFSET;
import static kingsley.www.runsong.m_interface.IConstant.PARAM_SIZE;
import static kingsley.www.runsong.m_interface.IConstant.PARAM_SONG_ID;
import static kingsley.www.runsong.m_interface.IConstant.PARAM_TYPE;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/6/5 20:50
 * file change date : on 2017/6/5 20:50
 * version: 1.0
 */

public class HttpClient {
    private static List<OnLineMusic> musicList;
    private static Music music;
    private static final String TAG = "HttpClient";

    public static void getSongListInfo(String type, int size, int offset, final HttpCallBack<OnLineMusicList> callBack) {
            musicList = new ArrayList<>();
            OkHttpUtils.get().url(BASE_URL)
                    .addParams(PARAM_METHOD, METHOD_GET_MUSIC_LIST)
                    .addParams(PARAM_TYPE, type)
                    .addParams(PARAM_SIZE, String.valueOf(size))
                    .addParams(PARAM_OFFSET, String.valueOf(offset))
                    .build()
                    .execute(new JsonCallBack<OnLineMusicList>(OnLineMusicList.class) {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(OnLineMusicList response, int id) {
                            callBack.onSuccess(response);
                        }
                    });
    }

    public static void getDownloadMusicInfo(String id){
        music = new Music();
        OkHttpUtils.get().url(BASE_URL)
                .addParams(PARAM_METHOD,METHOD_DOWNLOAD_MUSIC)
                .addParams(PARAM_SONG_ID,id)
                .build()
                .execute(new JsonCallBack<DownloadInfo>(DownloadInfo.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i(TAG, "onError: ");
                    }
                    @Override
                    public void onResponse(DownloadInfo response, int id) {
                        music.setPath(response.getBitrate().getFile_link());
                        //Log.i(TAG, "onResponse: response.getBitrate().getFile_link()="+response.getBitrate().getFile_link()+"  -- id="+id);
                        AppCache.getPlayService().play(music);
                    }
                });
    }
}
