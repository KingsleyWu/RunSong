package kingsley.www.runsong.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kingsley.www.runsong.cache.CacheMusic;
import kingsley.www.runsong.entity.Music;
import kingsley.www.runsong.m_interface.IConstant;
import kingsley.www.runsong.m_interface.IIsMusicChange;
import kingsley.www.runsong.m_interface.OnPlayerEventListener;
import kingsley.www.runsong.utils.MusicUtils;
import kingsley.www.runsong.utils.Preferences;

public class PlayService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private static final String TAG = "PlayService";
    //本地歌曲
    private List<Music> mMusicList;
    private PlayBinder binder;
    private static MediaPlayer mPlayer;
    // 正在播放的歌曲[本地|网络]
    private Music mPlayingMusic;
    // 正在播放的本地歌曲的序号
    private int mPlayingPosition;
    private long mPlayingCurrentPosition;
    private OnPlayerEventListener onPlayerEventListener;

    public PlayService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMusicList = new ArrayList<>();
        binder = new PlayBinder();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //加载本地歌曲数据
                MusicUtils.scanMusic(getApplicationContext(), mMusicList);
                //把本地歌曲数据给到CacheMusic
                CacheMusic.getInstance().setMusicList(mMusicList);
                //Log.i(TAG, "onCreate: mMusicList="+mMusicList);
            }
        }).start();
        final Handler handler = new Handler(getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if ((onPlayerEventListener = CacheMusic.onPlayerEventListener) != null && mPlayer != null && isPlaying()) {
                    onPlayerEventListener.setSeekBar(mPlayer.getCurrentPosition());
                }
                handler.postDelayed(this,1000);
            }
        }, 1000);

        if (mPlayer == null) mPlayer = new MediaPlayer();
        //设置异步加载监听
        mPlayer.setOnPreparedListener(this);
        //设置一首歌播放完毕后的监听
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnErrorListener(this);
    }

    //绑定服务器时调用
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*if (intent != null && intent.getAction() != null) {
            Log.i(TAG, "onStartCommand: "+intent.getClass());
            mNextPlayPosition = intent.getIntExtra(POSITION,mPlayingPosition);
            Log.i(TAG, "onStartCommand: mNextPlayPosition="+mNextPlayPosition);
            switch (intent.getAction()) {
                case PLAY:
                    Log.i(TAG, "onStartCommand: PLAY");
                    flag = false;
                    playPause();
                    break;
                case NEXT:
                    next();
                    break;
                case PREV:
                    prev();
                    break;
            }
        }*/
        return super.onStartCommand(intent, flags, startId);
    }

    //歌曲播放
    public void playPause(int mNextPlayPosition) {
        if (mMusicList.isEmpty()) {
            Toast.makeText(this, "列表为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mNextPlayPosition < 0) {
            mNextPlayPosition = mMusicList.size() - 1;
        } else if (mNextPlayPosition >= mMusicList.size()) {
            mNextPlayPosition = 0;
        }
        if (mPlayingPosition == mNextPlayPosition) {
            //Log.i(TAG, "playPause: mPlayingPosition="+mPlayingPosition);
            if (mPlayer.isPlaying()) {
                //Log.i(TAG, "playPause: mPlayer.pause()");
                pause();
            } else {
                play(mPlayingPosition);
                Log.i(TAG, "play: mPlayingPosition="+mPlayingPosition);
            }
        } else {
            mPlayingPosition = mNextPlayPosition;
            Log.i(TAG, "playPause: mPlayingPosition="+mPlayingPosition);
            play(mPlayingPosition);
        }
        updateView(mNextPlayPosition);
    }

    //歌曲变更需更新View的回调方法
    private void updateView(int mNextPlayPosition) {
        IIsMusicChange musicChange;
        if ((musicChange = CacheMusic.isMusicChange) != null)
            //播放位置变化
            musicChange.isMusicChange(mNextPlayPosition);
        if ((onPlayerEventListener = CacheMusic.onPlayerEventListener) != null) {
            onPlayerEventListener.setView(mNextPlayPosition);
        }
    }

    //歌曲播放
    public void play(int position) {
        if (mMusicList.isEmpty()) {
            return;
        }
        Music music = mMusicList.get(position);
        //Log.i(TAG, "play: Preferences.saveCurrentSongId =" + Preferences.getCurrentSongId());
        play(music);
        Preferences.saveCurrentSongId(music.getId());
        Preferences.saveCurrentPlaySongPosition(position);
    }

    //歌曲播放
    public void play(Music music) {
        //判断播放的是否为同一首歌
        if (music.equals(mPlayingMusic)) {
            mPlayer.seekTo((int) mPlayingCurrentPosition);
            mPlayer.start();
        } else {
            //当前播放的音乐
            mPlayingMusic = music;
            try {
                //让mediaPlayer处于空闲状态
                mPlayer.reset();
                mPlayer.setDataSource(music.getPath());
                //异步加载
                mPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //歌曲播放
    private void start() {
        mPlayer.start();
    }

    //歌曲播放
    private void pause() {
        if (!mPlayer.isPlaying()) return;
        mPlayer.pause();
        Log.i(TAG, "mPlayer.pause();");
        mPlayingCurrentPosition = mPlayer.getCurrentPosition();
    }

    //服务停止时调用,用于释放资源
    public void stop() {
        pause();
        mPlayer.reset();
        mPlayer.release();
        mPlayer = null;
        stopSelf();
    }

    //更改播放进度
    public void seekTo(int progress) {
        progress = mPlayer.getDuration() * progress / 100;
        mPlayer.seekTo(progress);
    }

    //当歌曲歌曲播放完成时,执行此方法
    @Override
    public void onCompletion(MediaPlayer mp) {
        switch (Preferences.getPlayMode()) {
            case IConstant.PLAY_MODE_LOOP:
                Log.i(TAG, "onCompletion: ="+mPlayingPosition);
                int lastPlayPosition = mPlayingPosition+1;
                playPause(lastPlayPosition);
                Log.i(TAG, "onPrepared: PLAY_MODE_LOOP"+mPlayingPosition);
                break;
            case IConstant.PLAY_MODE_SINGLE_LOOP:
                playPause(mPlayingPosition);
                Log.i(TAG, "onPrepared: PLAY_MODE_SINGLE_LOOP");
                break;
            case IConstant.PLAY_MODE_RANDOM:
                int randomPosition = new Random().nextInt(mMusicList.size());
                playPause(randomPosition);
                Log.i(TAG, "onPrepared: PLAY_MODE_RANDOM");
                break;
        }
    }

    //当歌曲异步加载时,执行此方法
    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.i(TAG, "onPrepared: mp.start()");
        if (mp != null) start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.i(TAG, "onError: what="+what);
        return true;
    }

    public class PlayBinder extends Binder {
        public PlayService getService() {
            return PlayService.this;
        }
    }

    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    public long getDuration(){
        return mPlayer.getDuration();
    }

    @Override
    public void onDestroy() {
        stop();
    }

}
