package kingsley.www.runsong.utils;

import kingsley.www.runsong.m_interface.IIsMusicChange;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/5/26 10:24
 * file change date : on 2017/5/26 10:24
 * version: 1.0
 */

public class MusicChange implements IIsMusicChange {
    private static int mPosition;
    @Override
    public void isMusicChange(int position) {
        mPosition = position;
    }
    private MusicChange(){}
}
