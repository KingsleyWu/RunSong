package kingsley.www.runsong.cache;

import java.util.ArrayList;
import java.util.List;

import kingsley.www.runsong.entity.Music;
import kingsley.www.runsong.m_interface.IIsMusicChange;
import kingsley.www.runsong.m_interface.OnPlayerEventListener;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/5/25 15:35
 * file change date : on 2017/5/25 15:35
 * version: 1.0
 */

public class CacheMusic {

    private static CacheMusic cacheMusic;

    public static IIsMusicChange isMusicChange;

    public static OnPlayerEventListener onPlayerEventListener;

    private List<Music> mMusicList = new ArrayList<>();

    private CacheMusic (){}

    public static CacheMusic getInstance(){
        if (cacheMusic == null){
            synchronized (CacheMusic.class) {
                cacheMusic = new CacheMusic();
            }
        }
        return cacheMusic;
    }

    public List<Music> getMusicList() {
        return mMusicList;
    }

    public void setMusicList(List<Music> mMusicList) {
        this.mMusicList = mMusicList;
    }

}
