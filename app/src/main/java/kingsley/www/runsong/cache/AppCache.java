package kingsley.www.runsong.cache;

import android.util.LongSparseArray;

import java.util.ArrayList;
import java.util.List;

import kingsley.www.runsong.entity.Music;
import kingsley.www.runsong.entity.SongListInfo;
import kingsley.www.runsong.service.PlayService;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/6/5 21:29
 * file change date : on 2017/6/5 21:29
 * version: 1.0
 */

public class AppCache {
    private PlayService mPlayService;
    // 本地歌曲列表
    private final List<Music> mMusicList = new ArrayList<>();
    // 歌单列表
    private final List<SongListInfo> mSongListInfos = new ArrayList<>();
    // 下载歌单
    public final LongSparseArray<String> mDownloadList = new LongSparseArray<>();

    private AppCache() {
    }

    private static AppCache INSTANCE;

    private static AppCache getInstance() {
        if (INSTANCE == null){
            synchronized (AppCache.class){
                if (INSTANCE == null){
                    INSTANCE = new AppCache();
                }
            }
        }
        return INSTANCE;
    }

    public static PlayService getPlayService() {
        return getInstance().mPlayService;
    }

    public static void setPlayService(PlayService service) {
        getInstance().mPlayService = service;
    }

    public static List<SongListInfo> getSongListInfos() {
        return getInstance().mSongListInfos;
    }


    public static LongSparseArray<String> getmDownloadList() {
        return getInstance().mDownloadList;
    }

    public static List<Music> getmMusicList() {
        return getInstance().mMusicList;
    }
}
