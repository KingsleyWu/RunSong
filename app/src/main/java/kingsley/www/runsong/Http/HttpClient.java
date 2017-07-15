package kingsley.www.runsong.Http;

import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import kingsley.www.runsong.entity.DownloadInfo;
import kingsley.www.runsong.entity.Lrc;
import kingsley.www.runsong.entity.OnLineMusicList;
import okhttp3.Call;

import static kingsley.www.runsong.m_interface.IConstant.BASE_URL;
import static kingsley.www.runsong.m_interface.IConstant.METHOD_DOWNLOAD_MUSIC;
import static kingsley.www.runsong.m_interface.IConstant.METHOD_GET_MUSIC_LIST;
import static kingsley.www.runsong.m_interface.IConstant.METHOD_LRC;
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
    private static final String TAG = "HttpClient";

    public static void getSongListInfo(final String type, final int size, final int offset, final HttpCallBack<OnLineMusicList> callBack) {

            OkHttpUtils.get().url(BASE_URL)
                    .addParams(PARAM_METHOD, METHOD_GET_MUSIC_LIST)
                    .addParams(PARAM_TYPE, type)
                    .addParams(PARAM_SIZE, String.valueOf(size))
                    .addParams(PARAM_OFFSET, String.valueOf(offset))
                    .build()
                    .execute(new JsonCallBack<OnLineMusicList>(OnLineMusicList.class) {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            getSongListInfo(type,size,offset,callBack);
                            Log.d(TAG, "getSongListInfo onError: id="+id+"  call="+call +"  e="+e);
                        }

                        @Override
                        public void onResponse(OnLineMusicList response, int id) {
                            callBack.onSuccess(response);
                        }
                    });
    }

    public static void getDownloadMusicInfo(final String songId, final HttpCallBack<DownloadInfo> callBack){
        OkHttpUtils.get().url(BASE_URL)
                .addParams(PARAM_METHOD,METHOD_DOWNLOAD_MUSIC)
                .addParams(PARAM_SONG_ID,songId)
                .build()
                .execute(new JsonCallBack<DownloadInfo>(DownloadInfo.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        getDownloadMusicInfo(songId,callBack);
                        Log.d(TAG, "getDownloadMusicInfo onError: id="+id+"  call="+call +"  e="+e);
                    }
                    @Override
                    public void onResponse(DownloadInfo response, int id) {
                        callBack.onSuccess(response);
                    }
                });
    }

    public static void downloadLrc(final String songId, final HttpCallBack<Lrc> callBack){
        OkHttpUtils.get().url(BASE_URL)
                .addParams(PARAM_METHOD,METHOD_LRC)
                .addParams(PARAM_SONG_ID,songId)
                .build()
                .execute(new JsonCallBack<Lrc>(Lrc.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        downloadLrc(songId,callBack);
                        Log.d(TAG, "downloadLrc onError: id="+id+"  call="+call +"  e="+e);
                    }

                    @Override
                    public void onResponse(Lrc response, int id) {
                        callBack.onSuccess(response);
                    }
                });
    }

    public static void downloadFile(final String url, final String destFileDir, final String destFileName, final HttpCallBack<File> callback) {
        OkHttpUtils.get().url(url).build()
                .execute(new FileCallBack(destFileDir, destFileName) {
                    @Override
                    public void inProgress(float progress, long total, int id) {
                    }

                    @Override
                    public void onResponse(File file, int id) {
                        callback.onSuccess(file);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        downloadFile(url,destFileDir, destFileName,callback);
                        Log.d(TAG, "downloadFile onError: id="+id+"  call="+call +"  e="+e);
                    }
                });
    }

}
