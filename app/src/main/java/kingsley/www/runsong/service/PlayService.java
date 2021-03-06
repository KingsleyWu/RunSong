package kingsley.www.runsong.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kingsley.www.runsong.application.MusicApplication;
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
    public int mPlayingPosition;
    public long mPlayingCurrentPosition;
    private OnPlayerEventListener onPlayerEventListener;
    public boolean isPrepare;
    private Handler handler;
    private IIsMusicChange musicChange;

    public PlayService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMusicList = new ArrayList<>();
        binder = new PlayBinder();
        initMusic();
        handler = new Handler(getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if ((onPlayerEventListener = CacheMusic.onPlayerEventListener) != null && mPlayer != null && isPlaying()) {
                    onPlayerEventListener.setSeekBar(mPlayer.getCurrentPosition());
                }
                handler.postDelayed(this, 1000);
            }
        }, 1000);

        if (mPlayer == null) mPlayer = getMediaPlayer(MusicApplication.instance);
        //设置异步加载监听
        mPlayer.setOnPreparedListener(this);
        //设置一首歌播放完毕后的监听
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnErrorListener(this);
    }

    public void initMusic() {
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
    }

    //绑定服务器时调用
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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
                isPrepare = false;
                pause();
            } else {
                isPrepare = true;
                play(mPlayingPosition);
                Log.i(TAG, "play: mPlayingPosition=" + mPlayingPosition);
            }
        } else {
            isPrepare = true;
            mPlayingPosition = mNextPlayPosition;
            Log.i(TAG, "playPause: mPlayingPosition=" + mPlayingPosition);
            play(mPlayingPosition);
        }
        updateView(mPlayingPosition);
    }

    //歌曲变更需更新View的回调方法
    private void updateView(int mNextPlayPosition) {
        if ((musicChange = CacheMusic.isMusicChange) != null)
            //播放位置变化
            musicChange.isMusicChange(mNextPlayPosition);
        if ((onPlayerEventListener = CacheMusic.onPlayerEventListener) != null && isPrepare) {
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
            Log.d(TAG, "play: 同一首歌");
        } else {
            //当前播放的音乐
            mPlayingMusic = music;
            if (music.getType() == Music.Type.ONLINE) {
                isPrepare = true;
                if ((musicChange = CacheMusic.isMusicChange) != null)
                    //播放位置变化
                    musicChange.isMusicChange(IConstant.ISONLINEMUSIC);
            }
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
        if (onPlayerEventListener != null)
            onPlayerEventListener.onPlayerCompletionPlay();
    }

    public long getCurrentPosition() {
        return mPlayer.getCurrentPosition();
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
        Log.d(TAG, "seekTo: progress=" + progress);
        if (mPlayer.isPlaying()) {
            progress = mPlayer.getDuration() * progress / 100;
            mPlayer.seekTo(progress);
            isPrepare = true;
        }
    }

    //当歌曲歌曲播放完成时,执行此方法
    @Override
    public void onCompletion(MediaPlayer mp) {
        switch (Preferences.getPlayMode()) {
            case IConstant.PLAY_MODE_LOOP:
                Log.i(TAG, "onCompletion: =" + mPlayingPosition);
                int lastPlayPosition = mPlayingPosition + 1;
                playPause(lastPlayPosition);
                Log.i(TAG, "onPrepared: PLAY_MODE_LOOP" + mPlayingPosition);
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
        Log.i(TAG, "onError: what=" + what);
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

    public long getDuration() {
        return mPlayer.getDuration();
    }

    public Music getPlayingMusic(){
        return mPlayingMusic;
    }

    @Override
    public void onDestroy() {
        stop();
    }

    private MediaPlayer getMediaPlayer(Context context) {
        MediaPlayer mediaplayer = new MediaPlayer();
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            return mediaplayer;
        }
        try {
            Class<?> cMediaTimeProvider = Class.forName("android.media.MediaTimeProvider");
            Class<?> cSubtitleController = Class.forName("android.media.SubtitleController");
            Class<?> iSubtitleControllerAnchor = Class.forName("android.media.SubtitleController$Anchor");
            Class<?> iSubtitleControllerListener = Class.forName("android.media.SubtitleController$Listener");
            Constructor constructor = cSubtitleController.getConstructor(
                    new Class[]{Context.class, cMediaTimeProvider, iSubtitleControllerListener});
            Object subtitleInstance = constructor.newInstance(context, null, null);
            Field f = cSubtitleController.getDeclaredField("mHandler");
            f.setAccessible(true);
            try {
                f.set(subtitleInstance, new Handler());
            } catch (IllegalAccessException e) {
                return mediaplayer;
            } finally {
                f.setAccessible(false);
            }
            Method setSubtitleAnchor = mediaplayer.getClass().getMethod("setSubtitleAnchor",
                    cSubtitleController, iSubtitleControllerAnchor);
            setSubtitleAnchor.invoke(mediaplayer, subtitleInstance, null);
        } catch (Exception e) {
            Log.d(TAG, "getMediaPlayer crash ,exception = " + e);
        }
        return mediaplayer;
    }

    public void updatePlayingPosition() {
        int position = 0;
        long id = Preferences.getCurrentSongId();
        for (int i = 0; i < mMusicList.size(); i++) {
            if (mMusicList.get(i).getId() == id) {
                position = i;
                break;
            }
        }
        mPlayingPosition = position;
        Preferences.saveCurrentSongId(mMusicList.get(mPlayingPosition).getId());
    }
}
