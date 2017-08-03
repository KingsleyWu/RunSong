package kingsley.www.runsong.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import kingsley.www.runsong.activity.BaseActivity;
import kingsley.www.runsong.cache.AppCache;
import kingsley.www.runsong.service.PlayService;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/5/25 18:36
 * file change date : on 2017/5/25 18:36
 * version: 1.0
 */

public abstract class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";
    protected BaseActivity baseActivity;
    protected PlayService getPlayService() {
        PlayService playService = AppCache.getPlayService();
        if (playService == null) {
            throw new NullPointerException("play service is null");
        }
        return playService;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = (BaseActivity)getActivity();
    }
}
