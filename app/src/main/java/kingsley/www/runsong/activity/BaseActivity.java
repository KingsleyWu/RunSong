package kingsley.www.runsong.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

import kingsley.www.runsong.cache.AppCache;
import kingsley.www.runsong.service.PlayService;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/5/26 00:52
 * file change date : on 2017/5/26 00:52
 * version: 1.0
 */

public class BaseActivity extends AppCompatActivity {
    protected Handler mHandler = new Handler(Looper.getMainLooper());

    protected void mStartActivity(Class activity){
        Intent intent = new Intent(getApplicationContext(),activity);
        startActivity(intent);

    }
    public PlayService getPlayService() {
        PlayService playService = AppCache.getPlayService();
        if (playService == null) {
            throw new NullPointerException("play service is null");
        }
        return playService;
    }

    protected boolean checkServiceAlive() {
        return AppCache.getPlayService() != null;
    }
}
