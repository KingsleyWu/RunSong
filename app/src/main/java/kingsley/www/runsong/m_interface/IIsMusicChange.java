package kingsley.www.runsong.m_interface;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/5/26 10:09
 * file change date : on 2017/5/26 10:09
 * version: 1.0
 */
//用于当歌曲变更时更新MusicLocalFragment的PlayBar视图
public interface IIsMusicChange {
        void isMusicChange(int position);
}
