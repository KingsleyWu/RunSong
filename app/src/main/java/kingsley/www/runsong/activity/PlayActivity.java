package kingsley.www.runsong.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import kingsley.www.runsong.R;
import kingsley.www.runsong.cache.CacheMusic;
import kingsley.www.runsong.entity.Music;
import kingsley.www.runsong.m_interface.IConstant;
import kingsley.www.runsong.m_interface.OnPlayerEventListener;
import kingsley.www.runsong.service.PlayService;
import kingsley.www.runsong.utils.CoverLoader;
import kingsley.www.runsong.utils.FileUtil;
import kingsley.www.runsong.utils.FormatDateUtil;
import kingsley.www.runsong.utils.Preferences;
import kingsley.www.runsong.view.BlurringView;
import kingsley.www.runsong.view.DiskCoverView;
import kingsley.www.runsong.view.lrcView.ILrcBuilder;
import kingsley.www.runsong.view.lrcView.ILrcViewListener;
import kingsley.www.runsong.view.lrcView.LrcBuilder;
import kingsley.www.runsong.view.lrcView.LrcRow;
import kingsley.www.runsong.view.lrcView.LrcView;

import static kingsley.www.runsong.utils.Preferences.getPlayMode;

public class PlayActivity extends BaseActivity implements View.OnClickListener, OnPlayerEventListener {

    private static final String TAG = "PlayMusicActivity";
    //toolbar
    @BindView(R.id.play_music_toolbar)
    public Toolbar mPlayMusicToolbar;
    @BindView(R.id.play_music_iv_back)
    public ImageView mPlayMusicIvBack;
    @BindView(R.id.play_music_tv_title)
    public TextView mPlayMusicTvTitle;
    @BindView(R.id.play_music_iv_more)
    public ImageView mPlayMusicIvMore;
    @BindView(R.id.play_music_frameLayout)
    public FrameLayout mPlayMusicFrameLayout;
    //歌曲时间
    @BindView(R.id.play_music_tv_currentPosition)
    public TextView mPlayMusicTvCurrentPosition;
    @BindView(R.id.playMusicSeekBar)
    public AppCompatSeekBar mPlayMusicSeekBar;
    @BindView(R.id.play_music_tv_songDuration)
    public TextView mPlayMusicTvSongDuration;
    //播放,模式,更多
    @BindView(R.id.play_music_iv_playMode)
    public ImageView mPlayMusicIvPlayMode;
    @BindView(R.id.play_music_iv_lastSong)
    public ImageView mPlayMusicIvLastSong;
    @BindView(R.id.play_music_iv_play_pause)
    public ImageView mPlayMusicIvPlayPause;
    @BindView(R.id.play_music_iv_nextSong)
    public ImageView mPlayMusicIvNextSong;
    @BindView(R.id.play_music_iv_moreList)
    public ImageView mPlayMusicIvMoreList;

    @BindView(R.id.play_music_songBg)
    public ImageView mPlayMusicIvSongBg;
    @BindView(R.id.playMusicBlurringView)
    public BlurringView mPlayMusicBlurringView;
    @BindView(R.id.play_music_DiskCoverView)
    public DiskCoverView diskCoverView;

    //动画,歌曲图片
    @BindView(R.id.play_music_LrcView)
    public LrcView lrcView;

    private CoverLoader coverLoader;
    private List<Music> mMusicList;
    private PlayService playService;
    private int position;
    private boolean isPlaying;
    private boolean isInitView;
    private int playMode;
    private long songDuration;
    private Music playingMusic;
    //更新歌词的频率，每秒更新一次
    private int mPlayTimerDuration = 1000;
    //更新歌词的定时器
    private Timer mTimer;
    //更新歌词的定时任务
    private TimerTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music_layout);
        ButterKnife.bind(this);
        //用于把musicChange引用还回给MusicLocalFragment
        //iIsMusicChange = CacheMusic.IsMusicChange;
        mMusicList = CacheMusic.getInstance().getMusicList();
        coverLoader = CoverLoader.getInstance();
        playService = getPlayService();
        playingMusic = playService.getPlayingMusic();
        if (playingMusic.getType() == Music.Type.LOCAL) {
            position = 0;
            playMode = Preferences.getPlayMode();
            long id = Preferences.getCurrentSongId();
            for (int i = 0; i < mMusicList.size(); i++) {
                if (mMusicList.get(i).getId() == id) {
                    position = i;
                    break;
                }
            }
        }else {
            position = IConstant.ISONLINEMUSIC;
        }
        //Log.i(TAG, "onCreate: position = " + position);
        isInitView = true;
        initView(position);
        setListeners();
        //把musicChange引用替换为PlayMusicActivity
        //CacheMusic.IsMusicChange = this;
        //Log.i(TAG, "onCreate: "+playMode);
    }

    //初始化控件
    private void initView(int position) {
        diskCoverView.initNeedle(getPlayService().isPlaying());
        setBarView(position);
        if (getPlayService().isPlaying()) {
            diskCoverView.start();
            mPlayMusicIvPlayPause.setImageResource(R.mipmap.b_stop);
            isPlaying = true;
            lrcView.setPlaying(true);
            beginLrcPlay();
        } else {
            diskCoverView.pause();
            mPlayMusicIvPlayPause.setImageResource(R.mipmap.b_play);
        }
        //设置监听
        CacheMusic.onPlayerEventListener = this;
    }

    //设置监听器
    private void setListeners() {
        mPlayMusicIvPlayMode.setOnClickListener(this);
        mPlayMusicIvLastSong.setOnClickListener(this);
        mPlayMusicIvPlayPause.setOnClickListener(this);
        mPlayMusicIvNextSong.setOnClickListener(this);
        mPlayMusicIvMoreList.setOnClickListener(this);
        mPlayMusicIvBack.setOnClickListener(this);
        mPlayMusicIvMore.setOnClickListener(this);
        mPlayMusicSeekBar.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
        //设置自定义的LrcView上下拖动歌词时监听
        lrcView.setListener(new ILrcViewListener() {
            //当歌词被用户上下拖动的时候回调该方法,从高亮的那一句歌词开始播放
            public void onLrcSeeked(int newPosition, LrcRow row) {
                if (getPlayService() != null && playService.isPrepare) {
                    Log.d(TAG, "onLrcSeeked:" + row.time);
                    int mProgress = (int) (row.time * 100 / songDuration);
                    Log.d(TAG, "onLrcSeeked: progress="+mProgress);
                    playService.seekTo(mProgress+1);
                    beginLrcPlay();
                }
            }
        });
    }

    //设置控件显示的内容
    private void setBarView(int position) {
        if (isInitView) {
            switch (playMode) {
                case IConstant.PLAY_MODE_LOOP:
                    mPlayMusicIvPlayMode.setImageResource(R.mipmap.loop);
                    break;
                case IConstant.PLAY_MODE_SINGLE_LOOP:
                    mPlayMusicIvPlayMode.setImageResource(R.mipmap.single_loop);
                    break;
                case IConstant.PLAY_MODE_RANDOM:
                    mPlayMusicIvPlayMode.setImageResource(R.mipmap.random);
                    break;
            }
            isInitView = false;
        }

        Bitmap songImage;
        if (position == IConstant.ISONLINEMUSIC){
            playingMusic = mMusicList.get(position);
            Glide.with(this).load(playingMusic.getCoverPath()).placeholder(R.mipmap.i_love_my_music).into(mPlayMusicIvSongBg);
            diskCoverView.setCoverBitmap(mPlayMusicIvSongBg.getDrawingCache());
        }else {
            String path = FileUtil.getAlbumFilePath(playingMusic);
            if (path == null) {
                songImage = BitmapFactory.decodeResource(getResources(), R.mipmap.i_love_my_music);
            } else {
                songImage = coverLoader.loadBitmapForPath(path, 180);
            }
            mPlayMusicIvSongBg.setImageBitmap(songImage);
            songImage = coverLoader.resizeImage(songImage, 300, 300);
            songImage = coverLoader.createCircleImage(songImage);
            diskCoverView.setCoverBitmap(songImage);
        }
        String lrc = parseLrc(playingMusic);
        ILrcBuilder builder = new LrcBuilder();
        //解析歌词返回LrcRow集合
        List<LrcRow> rows = builder.getLrcRows(lrc);
        //将得到的歌词集合传给mLrcView用来展示
        lrcView.setLrc(rows);
        //ImageFastblurUtil.fastblur(songImage,20);
        mPlayMusicBlurringView.setBlurredView(mPlayMusicIvSongBg);
        mPlayMusicBlurringView.invalidate();
        mPlayMusicTvTitle.setText(playingMusic.getTitle());
        songDuration = playingMusic.getDuration();
        mPlayMusicTvSongDuration.setText(new SimpleDateFormat("mm:ss").format(songDuration));
    }

    //开始播放歌曲并同步展示歌词
    private void beginLrcPlay() {
        if (mTimer == null) {
            mTimer = new Timer();
            mTask = new LrcTask();
            mTimer.scheduleAtFixedRate(mTask, 0, mPlayTimerDuration);
        }
    }

    /**
     * 展示歌曲的定时任务
     */
    private class LrcTask extends TimerTask {
        @Override
        public void run() {
            if (playService != null) {
                //获取歌曲播放的位置
                final long timePassed = playService.getCurrentPosition();
                PlayActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        //滚动歌词
                        lrcView.seekLrcToTime(timePassed);
                    }
                });
            }
        }
    }

    private String parseLrc(Music music) {
        if (music.getType() == Music.Type.ONLINE){

        }
        try {
            String lrcPath = FileUtil.getLrcFilePath(music);
            if (lrcPath != null) {
                File file = new File(lrcPath);
                BufferedReader bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
                String line;
                StringBuilder builder = new StringBuilder();
                //读取一行数据
                while ((line = bufReader.readLine()) != null) {
                    if (line.trim().equals(""))
                        continue;
                    //不为空则拼接字符
                    builder.append(line).append("\r\n");
                }
                log(builder.toString());
                return builder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    //点击处理
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //music
            case R.id.play_music_iv_lastSong:
                position--;
                setViewAndPlayMusic();
                break;
            case R.id.play_music_iv_play_pause:
                if (isPlaying) {
                    isPlaying = false;
                    mPlayMusicIvPlayPause.setImageResource(R.mipmap.b_play);
                    playService.playPause(position);
                    diskCoverView.pause();
                    lrcView.setPlaying(false);
                } else {
                    setViewAndPlayMusic();
                }
                break;
            case R.id.play_music_iv_nextSong:
                position++;
                setViewAndPlayMusic();
                break;
            case R.id.play_music_iv_playMode:
                switch (getPlayMode()) {
                    case IConstant.PLAY_MODE_LOOP:
                        mPlayMusicIvPlayMode.setImageResource(R.mipmap.single_loop);
                        Preferences.savePlayMode(IConstant.PLAY_MODE_SINGLE_LOOP);
                        break;
                    case IConstant.PLAY_MODE_SINGLE_LOOP:
                        mPlayMusicIvPlayMode.setImageResource(R.mipmap.random);
                        Preferences.savePlayMode(IConstant.PLAY_MODE_RANDOM);
                        break;
                    case IConstant.PLAY_MODE_RANDOM:
                        mPlayMusicIvPlayMode.setImageResource(R.mipmap.loop);
                        Preferences.savePlayMode(IConstant.PLAY_MODE_LOOP);
                        break;
                }
                break;
            case R.id.play_music_iv_moreList:
                Toast.makeText(this, "敬请期待moreList...", Toast.LENGTH_SHORT).show();
                break;
            //toolbar
            case R.id.play_music_iv_back:
                finish();
                break;
        }
    }

    //点击事件调用,用于播放及更改控件显示内容
    private void setViewAndPlayMusic() {
        //为了防止下一首和上一首出现下标值溢出的问题
        if (position < 0) {
            position = mMusicList.size() - 1;
        } else if (position >= mMusicList.size()) {
            position = 0;
        }
        playService.playPause(position);
        isPlaying = true;
        mPlayMusicIvPlayPause.setImageResource(R.mipmap.b_stop);
        //setView(position, playMode);
        lrcView.setPlaying(true);
        diskCoverView.start();
    }

    //自动下一曲时回调此方法
    @Override
    public void setView(int position) {
        setBarView(position);
        beginLrcPlay();
    }

    //播放时更新seekBar时调用
    @Override
    public void setSeekBar(int progress) {
        songDuration = getPlayService().getDuration();
        int mProgress = (int) (progress * 100 / songDuration);
        mPlayMusicSeekBar.setProgress(mProgress);
        final String date = FormatDateUtil.formatTime("mm:ss", progress);
        setCurrentTimeView(date);
    }

    @Override
    public void onPlayerCompletionPlay() {
        stopLrcPlay();
    }

    /**
     * 停止展示歌曲
     */
    public void stopLrcPlay() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    //设置当前播放的时间进度
    private void setCurrentTimeView(String date) {
        mPlayMusicTvCurrentPosition.setText(date);
    }

    private class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (playService.isPrepare) {
                int progress = seekBar.getProgress();
                playService.seekTo(progress);
            }else {
                seekBar.setProgress(0);
            }
        }
    }

    //当Activity处于不可见时调用
    @Override
    protected void onPause() {
        super.onPause();
        CacheMusic.onPlayerEventListener = null;
        //getPlayService().iGetChangeMusicPosition = null;
        //把musicChange引用还回给MusicLocalFragment
        //CacheMusic.IsMusicChange = iIsMusicChange;
        //Log.i(TAG, "onPause: "+CacheMusic.IsMusicChange);
    }

    //当Activity销毁时调用
    @Override
    protected void onDestroy() {
        super.onDestroy();
        playService = null;
        mMusicList = null;
        //Log.i(TAG, "onDestroy: "+iIsMusicChange);
    }

}
