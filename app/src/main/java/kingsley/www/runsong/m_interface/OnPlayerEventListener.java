package kingsley.www.runsong.m_interface;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/5/26 11:49
 * file change date : on 2017/5/26 11:49
 * version: 1.0
 */
//用于当歌曲变更时更新PlayMusicActivity的歌曲信息视图
public interface OnPlayerEventListener {

    void setView(int position);

    void setSeekBar(int progress);

    void onPlayerCompletionPlay();
}