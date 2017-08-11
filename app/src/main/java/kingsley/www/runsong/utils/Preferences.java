package kingsley.www.runsong.utils;

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
    private static final Boolean IS_FIRST = true;
    private static SharedPreferences sharedPreferences;

    //初始化SharePreferences
    public static void init(Context context){
        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
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
        return sharedPreferences.getLong(key, defValue);
    }

    private static void saveLong(String key, long value) {
        sharedPreferences.edit().putLong(key, value).apply();
    }

    private static int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    private static void saveInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public static String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    public static void saveString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    public static void saveBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public static Boolean getIsFirst() {
        return getBoolean("isFirst", IS_FIRST);
    }

    public static void setIsFirst(boolean value){
        saveBoolean("isFirst",value);
    }
}
