package kingsley.www.runsong.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import kingsley.www.runsong.R;
import kingsley.www.runsong.activity.PlayActivity;
import kingsley.www.runsong.adapter.LocalMusicAdapter;
import kingsley.www.runsong.cache.CacheMusic;
import kingsley.www.runsong.entity.Music;
import kingsley.www.runsong.m_interface.IConstant;
import kingsley.www.runsong.m_interface.IIsMusicChange;
import kingsley.www.runsong.service.PlayService;
import kingsley.www.runsong.utils.CoverLoader;
import kingsley.www.runsong.utils.FileUtil;
import kingsley.www.runsong.utils.Preferences;
import kingsley.www.runsong.view.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicLocalFragment extends BaseFragment implements LocalMusicAdapter.OnItemClickListener,
        View.OnClickListener, IIsMusicChange {
    private static final String TAG = "MusicLocalFragment";
    @BindView(R.id.localMusic_RecyclerView)
    RecyclerView mLocalMusicRecyclerView;
    @BindView(R.id.localMusic_Iv_songImage)
    CircleImageView mLocalMusicIvSongImage;
    @BindView(R.id.localMusic_Tv_title)
    TextView mLocalMusicTvTitle;
    @BindView(R.id.localMusic_Tv_artist)
    TextView mLocalMusicTvArtist;
    @BindView(R.id.localMusic_Iv_play)
    ImageView mLocalMusicIvPlay;
    @BindView(R.id.localMusic_Iv_moreList)
    ImageView mLocalMusicIvMoreList;
    @BindView(R.id.localMusic_Ll_playBar)
    LinearLayout mLocalMusicLlPlayBar;
    @BindView(R.id.localMusic_tv_isEmpty)
    TextView mLocalMusicTvIsEmpty;
    private LocalMusicAdapter adapter;
    private Context context;
    private int mPosition = -1;
    private Music music;
    private Animation rotateAnimation;
    private PlayService mPlayService;
    private long songId;
    protected List<Music> mMusicList;

    public MusicLocalFragment() {
        // Required empty public constructor
        mPlayService = getPlayService();
    }

    @Override
    public View createMyView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMusicList = new ArrayList<>();
        return inflater.inflate(R.layout.fragment_loacl_music, container, false);
    }

    @Override
    public void doBusiness() {
        initView();
        setListeners();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void initView() {
        mLocalMusicRecyclerView.setHasFixedSize(true);
        //瀑布流
        //RecyclerView.LayoutManager  layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mLocalMusicRecyclerView.setLayoutManager(layoutManager);
        adapter = new LocalMusicAdapter(getActivity(), mMusicList);
        mLocalMusicRecyclerView.setAdapter(adapter);
        mLocalMusicRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.HORIZONTAL));

        songId = Preferences.getCurrentSongId();
        //Log.i(TAG, "initView: songId=" + songId);
        if (songId == -1) setView(0);
        else setView(getSongPosition());
    }

    private int getSongPosition() {
        songId = Preferences.getCurrentSongId();
        int position = 0;
        for (int i = 0; i < mMusicList.size(); i++) {
            if (mMusicList.get(i).getId() == songId) {
                position = i;
                break;
            }
        }
        return position;
    }

    private void setListeners() {
        adapter.setItemClickListener(this);
        mLocalMusicIvPlay.setOnClickListener(this);
        mLocalMusicLlPlayBar.setOnClickListener(this);
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position) {
        mPlayService.playPause(position);
    }

    public void setView(int position) {
        // Log.i(TAG, "setView: mPosition =" + mPosition + "   position =" + position);
        //boolean isPlaying = mPlayService.isPlaying();
        if (mMusicList.size() > 0) {
            if (position == IConstant.ISONLINEMUSIC) {
                music = CacheMusic.getOnLineMusic();
                Glide.with(this).load(music.getCoverPath()).into(mLocalMusicIvSongImage);
            } else {
                music = mMusicList.get(position);
                String path = FileUtil.getAlbumFilePath(music);
                if (path != null)
                    mLocalMusicIvSongImage.setImageBitmap(CoverLoader.getInstance().loadBitmapForPath(path, 40));
                else {
                    mLocalMusicIvSongImage.setImageResource(R.mipmap.i_love_my_music);
                }
            }

            mLocalMusicTvTitle.setText(music.getTitle());
            mLocalMusicTvArtist.setText(music.getArtist());
            if (mPlayService.isPrepare) {
                Log.d(TAG, "setView: playing");
                mLocalMusicIvPlay.setImageResource(R.mipmap.stop);
                rotaView(mLocalMusicIvSongImage, 0);
            } else {
                Log.d(TAG, "setView: noPlaying");
                mLocalMusicIvPlay.setImageResource(R.mipmap.play);
                mLocalMusicIvSongImage.clearAnimation();
            }
        } else {
            mLocalMusicTvIsEmpty.setVisibility(View.VISIBLE);
        }
    }

    //歌曲图标旋转
    public void rotaView(View view, long startOffset) {
        rotateAnimation = AnimationUtils.loadAnimation(context.getApplicationContext(), R.anim.play_music_anim);
        LinearInterpolator lin = new LinearInterpolator();
        rotateAnimation.setInterpolator(lin);
        rotateAnimation.setStartOffset(startOffset);
        view.startAnimation(rotateAnimation);
        view.invalidate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.localMusic_Iv_play:
                mPosition = getSongPosition();
                mPlayService.playPause(mPosition);
                break;
            case R.id.localMusic_Ll_playBar:
                baseActivity.startActivity(new Intent(context, PlayActivity.class));
                break;
        }
    }

    //歌曲变更时调用
    @Override
    public void isMusicChange(int position) {
        setView(position);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach: close");
        context = null;
        mPlayService = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        if (mPlayService.isPlaying()) {
            mPosition = -1;
            setView(getSongPosition());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        if (CacheMusic.getInstance().getMusicList() != null) {
            adapter.addAll(CacheMusic.getInstance().getMusicList(), true);
            setView(getSongPosition());
        }
    }

}
