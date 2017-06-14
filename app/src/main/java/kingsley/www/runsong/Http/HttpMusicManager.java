package kingsley.www.runsong.Http;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import kingsley.www.runsong.entity.OnLineMusic;
import kingsley.www.runsong.utils.StreamUtil;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/6/5 09:15
 * file change date : on 2017/6/5 09:15
 * version: 1.0
 */

public class HttpMusicManager {
    //获取网络数据
    private static List<OnLineMusic> loadMusic(){
        //初始化返回类型
        List<OnLineMusic> onLineMusics = null;
        try {
            onLineMusics = new ArrayList<>();
            //初始化url
            URL url = new URL("IConstant.MUSICLIST_URL");
            //获得connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //设置请求头及请求参数
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(500);
            connection.setDoInput(true);
            //连接服务器
            connection.connect();
            //获取返回码
            int statusCode = connection.getResponseCode();
            if (statusCode == 200){
                //获取服务端的输入流
                InputStream is = connection.getInputStream();
                String jsonStr = StreamUtil.createStr(is);
                JSONObject jsonObjects = new JSONObject(jsonStr);
                String result = jsonObjects.getString("result");
                if (result.equals("ok")){
                    JSONArray array = jsonObjects.getJSONArray("data");
                    if (array != null)
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonMusic = array.getJSONObject(i);
                        int id = jsonMusic.getInt("id");
                        String album = jsonMusic.getString("album");
                        String albumpic=jsonMusic.getString("albumpic");
                        String author=jsonMusic.getString("author");
                        String composer=jsonMusic.getString("composer");
                        String downcount=jsonMusic.getString("downcount");
                        String durationtime=jsonMusic.getString("durationtime");
                        String favcount=jsonMusic.getString("favcount");
                        String musicpath=jsonMusic.getString("musicpath");
                        String name=jsonMusic.getString("name");
                        String singer=jsonMusic.getString("singer");
                        OnLineMusic music=new OnLineMusic();
                        onLineMusics.add(music);
                    }
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return onLineMusics;
    }

    public static LoadMusicListener listener;
    public static void asyncLoadMusic(LoadMusicListener musicListener){
        if (listener == null){
            listener = musicListener;
        }
        LoadMusicAsyncTask task = new LoadMusicAsyncTask();
        task.execute();
    }
    public interface LoadMusicListener{
        void onMusicsLoadEnd(List<OnLineMusic> musics);
    }
    public static class LoadMusicAsyncTask extends AsyncTask<Void,Integer,List<OnLineMusic>>{
        @Override
        protected List<OnLineMusic> doInBackground(Void... params) {
            //执行歌曲加载
            return loadMusic();
        }

        @Override
        protected void onPostExecute(List<OnLineMusic> onLineMusics) {
            super.onPostExecute(onLineMusics);
            //返回歌曲
            listener.onMusicsLoadEnd(onLineMusics);
        }
    }
}
