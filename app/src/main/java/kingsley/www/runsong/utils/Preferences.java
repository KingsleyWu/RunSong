package kingsley.www.runsong.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * SharePreferences工具
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/6/6 16:13
 * file change date : on 2017/6/6 16:13
 * version: 1.0
 */

public class Preferences {
    //定义需保存的内容
    private static final String MUSIC_ID = "music_id";
    private static final String PLAY_MODE = "play_mode";
    private static final String CURRENT_PLAY_POSITION = "current_play_position";
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    public static void init(Context context){
        mContext = context.getApplicationContext();
    }
    //初始化SharePreferences
    private static SharedPreferences getPreferences(){
        return PreferenceManager.getDefaultSharedPreferences(mContext);
    }
    //从偏好设置中取出播放的songId
    public static long getCurrentSongId() {
        return getLong(MUSIC_ID, -1);
    }
    //把当前播放的songId保存到偏好设置
    public static void saveCurrentSongId(long id) {
        saveLong(MUSIC_ID, id);
    }
    //从偏好设置中取出播放模式
    public static int getPlayMode() {
        return getInt(PLAY_MODE, 0);
    }
    //把当前的播放模式保存到偏好设置
    public static void savePlayMode(int mode) {
        saveInt(PLAY_MODE, mode);
    }

    public static void saveCurrentPlaySongPosition(int position){
        saveInt(CURRENT_PLAY_POSITION, position);
    }
    public static int getCurrentPlaySongPosition(){
        return getInt(CURRENT_PLAY_POSITION, 0);
    }

    private static long getLong(String key, long defValue) {
        return getPreferences().getLong(key, defValue);
    }

    private static void saveLong(String key, long value) {
        getPreferences().edit().putLong(key, value).apply();
    }

    private static int getInt(String key, int defValue) {
        return getPreferences().getInt(key, defValue);
    }

    private static void saveInt(String key, int value) {
        getPreferences().edit().putInt(key, value).apply();
    }
}
