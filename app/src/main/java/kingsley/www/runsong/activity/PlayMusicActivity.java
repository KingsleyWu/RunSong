package kingsley.www.runsong.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;

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

import static kingsley.www.runsong.utils.Preferences.getPlayMode;

public class PlayMusicActivity extends BaseActivity implements View.OnClickListener, OnPlayerEventListener {

    private static final String TAG = "PlayMusicActivity";
    private Toolbar mPlayMusicToolbar;
    private ImageView mPlayMusicIvBack;
    private TextView mPlayMusicTvTitle;
    private ImageView mPlayMusicIvMore;
    private FrameLayout mPlayMusicFrameLayout;
    private TextView mPlayMusicTvCurrentPosition;
    private AppCompatSeekBar mPlayMusicSeekBar;
    private TextView mPlayMusicTvSongDuration;
    private ImageView mPlayMusicIvPlayMode;
    private ImageView mPlayMusicIvLastSong;
    private ImageView mPlayMusicIvPlayPause;
    private ImageView mPlayMusicIvNextSong;
    private ImageView mPlayMusicIvMoreList;
    private ImageView mPlayMusicIvCollect;
    private ImageView mPlayMusicIvDownload;
    private ImageView mPlayMusicIvMsg, mPlayMusicIvSongImage, mPlayMusicIvShare,mPlayMusicIvSongBg;
    private BlurringView mPlayMusicBlurringView;
    private CoverLoader coverLoader;
    private List<Music> mMusicList;
    private RelativeLayout mPlayMusicRotaView;
    private ImageView mPlayMusicIvPin;
    private PlayService playService;
    private int position;
    private boolean isPlaying;
    private boolean isInitView;
    private int playMode;
    private long songDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        //用于把musicChange引用还回给MusicLocalFragment
        //iIsMusicChange = CacheMusic.IsMusicChange;
        mMusicList = CacheMusic.getInstance().getMusicList();
        coverLoader = CoverLoader.getInstance();
        playService = getPlayService();
        position = 0;
        playMode = Preferences.getPlayMode();
        long id = Preferences.getCurrentSongId();
        for (int i = 0; i < mMusicList.size(); i++) {
            if (mMusicList.get(i).getId() == id) {
                position = i;
                break;
            }
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
        //toolbar
        mPlayMusicToolbar = (Toolbar) findViewById(R.id.play_music_toolbar);
        mPlayMusicIvBack = (ImageView) findViewById(R.id.play_music_iv_back);
        mPlayMusicTvTitle = (TextView) findViewById(R.id.play_music_tv_title);
        mPlayMusicIvMore = (ImageView) findViewById(R.id.play_music_iv_more);
        mPlayMusicFrameLayout = (FrameLayout) findViewById(R.id.play_music_frameLayout);
        //歌曲时间
        mPlayMusicTvCurrentPosition = (TextView) findViewById(R.id.play_music_tv_currentPosition);
        mPlayMusicSeekBar = (AppCompatSeekBar) findViewById(R.id.playMusicSeekBar);
        mPlayMusicTvSongDuration = (TextView) findViewById(R.id.play_music_tv_songDuration);
        //播放,模式,更多
        mPlayMusicIvPlayMode = (ImageView) findViewById(R.id.play_music_iv_playMode);
        mPlayMusicIvLastSong = (ImageView) findViewById(R.id.play_music_iv_lastSong);
        mPlayMusicIvPlayPause = (ImageView) findViewById(R.id.play_music_iv_play_pause);
        mPlayMusicIvNextSong = (ImageView) findViewById(R.id.play_music_iv_nextSong);
        mPlayMusicIvMoreList = (ImageView) findViewById(R.id.play_music_iv_moreList);
        //收藏,下载,分享,评论
        mPlayMusicIvCollect = (ImageView) findViewById(R.id.play_music_iv_collect);
        mPlayMusicIvDownload = (ImageView) findViewById(R.id.play_music_iv_download);
        mPlayMusicIvShare = (ImageView) findViewById(R.id.play_music_iv_share);
        mPlayMusicIvMsg = (ImageView) findViewById(R.id.play_music_iv_msg);
        //动画,歌曲图片
        mPlayMusicIvSongImage = (ImageView) findViewById(R.id.play_music_iv_songImage);
        mPlayMusicIvSongBg = (ImageView) findViewById(R.id.play_music_songBg);
        mPlayMusicRotaView = (RelativeLayout) findViewById(R.id.play_music_rotaView);
        mPlayMusicIvPin = (ImageView) findViewById(R.id.play_music_iv_pin);
        mPlayMusicBlurringView = (BlurringView) findViewById(R.id.playMusicBlurringView);
        setView(position);
        if (getPlayService().isPlaying()) {
            startRotaView();
            mPlayMusicIvPlayPause.setImageResource(R.mipmap.b_stop);
            isPlaying = true;
        } else {
            stopRotaPin(mPlayMusicIvPin,0);
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
        mPlayMusicIvDownload.setOnClickListener(this);
        mPlayMusicIvShare.setOnClickListener(this);
        mPlayMusicIvCollect.setOnClickListener(this);
        mPlayMusicIvMsg.setOnClickListener(this);
        mPlayMusicIvBack.setOnClickListener(this);
        mPlayMusicIvMore.setOnClickListener(this);
        mPlayMusicSeekBar.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
    }
    //设置控件显示的内容
    private void setBarView(int position) {
        if (isInitView){
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
        Music music = mMusicList.get(position);
        String path = FileUtil.getAlbumFilePath(music);
        Bitmap songImage;
        if (path == null) {
            songImage = BitmapFactory.decodeResource(getResources(), R.mipmap.i_love_my_music);
        } else {
            songImage = coverLoader.loadBitmapForPath(path, 180);
        }
        mPlayMusicIvSongImage.setImageBitmap(songImage);
        mPlayMusicIvSongBg.setImageBitmap(songImage);
        //ImageFastblurUtil.fastblur(songImage,20);
        mPlayMusicBlurringView.setBlurredView(mPlayMusicIvSongBg);
        mPlayMusicBlurringView.invalidate();

        mPlayMusicTvTitle.setText(music.getTitle());
        songDuration = music.getDuration();
        mPlayMusicTvSongDuration.setText(new SimpleDateFormat("mm:ss").format(songDuration));
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
                    stopRotaView();
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
            case R.id.play_music_iv_more:
                Toast.makeText(this, "敬请期待more...", Toast.LENGTH_SHORT).show();
                break;
            //收藏,下载,分享,评论
            case R.id.play_music_iv_collect:
                Toast.makeText(this, "敬请期待collect...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.play_music_iv_download:
                Toast.makeText(this, "敬请期待...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.play_music_iv_share:
                Toast.makeText(this, "敬请期待share...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.play_music_iv_msg:
                Toast.makeText(this, "敬请期待msg...", Toast.LENGTH_SHORT).show();
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
        startRotaView();
    }
    //停止动画
    private void stopRotaView() {
        mPlayMusicRotaView.clearAnimation();
        //rotaViewAnimator.pause();
        stopRotaPin(mPlayMusicIvPin,100);
        //mPlayMusicIvPin.clearAnimation();
    }
    //开启动画
    private void startRotaView() {
        //mPlayMusicRotaView.clearAnimation();
        //mPlayMusicIvPin.clearAnimation();
        rotaView(mPlayMusicRotaView, 0);
        rotaPin(mPlayMusicIvPin, 0);
    }
    //开启disk动画
    public void rotaView(View view, long startOffset) {
        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.play_music_anim);
        LinearInterpolator lin = new LinearInterpolator();
        rotateAnimation.setInterpolator(lin);
        rotateAnimation.setStartOffset(startOffset);
        rotateAnimation.setFillAfter(true);
        view.startAnimation(rotateAnimation);
        view.invalidate();
    }

    public static void rotaView1(View view, long startOffset) {
        ObjectAnimator rotaViewAnimator = ObjectAnimator.ofFloat(view, "rotation", 0, 359);
        rotaViewAnimator.setDuration(10000);
        rotaViewAnimator.setStartDelay(startOffset);
        rotaViewAnimator.start();
        rotaViewAnimator.setRepeatMode(ValueAnimator.RESTART);
        rotaViewAnimator.setRepeatCount(-1);
        view.setPivotX(view.getWidth()/2);
        view.setPivotY(view.getHeight()/2);
        view.invalidate();
    }
    //pin歌曲停止动画
    public static void stopRotaPin(View view, long startOffset) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"rotation",0,-25);
        animator.setDuration(1000);
        animator.setStartDelay(startOffset);
        animator.start();
        view.setPivotX(25);
        view.setPivotY(50);
        view.invalidate();
    }
    //pin歌曲播放时动画
    public static void rotaPin(View view, long startOffset) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"rotation",-25,0);
        animator.setDuration(1000);
        animator.setStartDelay(startOffset);
        animator.start();
        view.setPivotX(25);
        view.setPivotY(50);
        view.invalidate();
    }
    //自动下一曲时回调此方法
    @Override
    public void setView(int position) {
        setBarView(position);
    }
    //播放时更新seekBar时调用
    @Override
    public void setSeekBar(int progress) {
        songDuration = getPlayService().getDuration();
        int mProgress= (int) (progress*100/songDuration);
        mPlayMusicSeekBar.setProgress(mProgress);
        final String date = FormatDateUtil.formatTime("mm:ss", progress);
        setCurrentTimeView(date);
    }

    @Override
    public void onPlayerCompletionPlay() {

    }

    //设置当前播放的时间进度
    private void setCurrentTimeView(String date){
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
            int progress = seekBar.getProgress();
            playService.seekTo(progress);
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
