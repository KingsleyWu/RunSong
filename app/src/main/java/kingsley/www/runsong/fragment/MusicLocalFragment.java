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

import kingsley.www.runsong.R;
import kingsley.www.runsong.activity.PlayMusicActivity;
import kingsley.www.runsong.adapter.LocalMusicAdapter;
import kingsley.www.runsong.entity.Music;
import kingsley.www.runsong.m_interface.IIsMusicChange;
import kingsley.www.runsong.service.PlayService;
import kingsley.www.runsong.utils.CoverLoader;
import kingsley.www.runsong.utils.Preferences;
import kingsley.www.runsong.view.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicLocalFragment extends BaseFragment
        implements LocalMusicAdapter.OnItemClickListener, View.OnClickListener, IIsMusicChange {
    private static final String TAG = "MusicLocalFragment";
    private CircleImageView mLocalMusicIvSongImage;
    private LinearLayout mLocalMusicLlPlayBar;
    private TextView mLocalMusicTvTitle;
    private TextView mLocalMusicTvArtist;
    private ImageView mLocalMusicIvPlay;
    private ImageView mLocalMusicIvMoreList;
    private LocalMusicAdapter adapter;
    private Context context;
    private int mPosition = -1;
    private Music music;
    private Animation rotateAnimation;
    private PlayService mPlayService;
    public boolean isResumeInit;
    private boolean isFirstLoad = true;
    private long songId;
    private boolean isPause;

    public MusicLocalFragment() {
        // Required empty public constructor
    }

    @Override
    protected void init() {
        mPlayService = getPlayService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loacl_music, container, false);
        initView(view);
        setListeners();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void initView(View view) {
        RecyclerView mLocalMusicRecyclerView = (RecyclerView) view.findViewById(R.id.localMusic_RecyclerView);
        mLocalMusicRecyclerView.setHasFixedSize(true);
        //瀑布流
        //RecyclerView.LayoutManager  layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mLocalMusicRecyclerView.setLayoutManager(layoutManager);
        adapter = new LocalMusicAdapter(getActivity(), mMusicList);
        mLocalMusicRecyclerView.setAdapter(adapter);

        mLocalMusicRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.HORIZONTAL));

        mLocalMusicLlPlayBar = (LinearLayout) view.findViewById(R.id.localMusic_Ll_playBar);
        mLocalMusicIvSongImage = (CircleImageView) view.findViewById(R.id.localMusic_Iv_songImage);
        mLocalMusicTvTitle = (TextView) view.findViewById(R.id.localMusic_Tv_title);
        mLocalMusicTvArtist = (TextView) view.findViewById(R.id.localMusic_Tv_artist);
        mLocalMusicIvPlay = (ImageView) view.findViewById(R.id.localMusic_Iv_play);
        mLocalMusicIvMoreList = (ImageView) view.findViewById(R.id.localMusic_Iv_moreList);
        songId = Preferences.getCurrentSongId();
        //Log.i(TAG, "initView: songId=" + songId);
        if (songId == -1) setView(0);
        else setView(getSongPosition());
    }

    private int getSongPosition(){
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
        //setView(position);
    }

    public void setView(int position) {
       // Log.i(TAG, "setView: mPosition =" + mPosition + "   position =" + position);
        //boolean isPlaying = mPlayService.isPlaying();
        music = mMusicList.get(position);
        String path = music.getCoverPath();
        if (path != null)
            mLocalMusicIvSongImage.setImageBitmap(CoverLoader.getInstance().loadBitmapForPath(path, 40));
        else {
            mLocalMusicIvSongImage.setImageResource(R.mipmap.i_love_my_music);
        }
        mLocalMusicTvTitle.setText(music.getTitle());
        mLocalMusicTvArtist.setText(music.getArtist());
        if (isFirstLoad) {
            isFirstLoad = false;
            mLocalMusicIvPlay.setImageResource(R.mipmap.play);
            Log.i(TAG, "setView: 刚进入界面,暂停");
        } else {
            if (mPosition == position) {
                if (isPause) {
                    Log.i(TAG, "setView: 同一首歌暂停");
                    mLocalMusicIvPlay.setImageResource(R.mipmap.play);
                    mLocalMusicIvSongImage.clearAnimation();
                    isPause = false;
                }else {
                    Log.i(TAG, "setView: 同一首歌播放");
                    mLocalMusicIvPlay.setImageResource(R.mipmap.stop);
                    rotaView(mLocalMusicIvSongImage, 0);
                    isPause = true;
                }
            } else {
                mPosition = position;
                mLocalMusicIvPlay.setImageResource(R.mipmap.stop);
                rotaView(mLocalMusicIvSongImage, 0);
                Log.i(TAG, "setView: 不同歌曲播放");
                isPause = true;
            }
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
                //setView(mPosition);
                //Log.i(TAG, "onClick: localMusic_Iv_play mPosition="+mPosition);
                break;
            case R.id.localMusic_Ll_playBar:
                /*MainActivity activity = (MainActivity)context;
                activity.showPlayMusicFragment();*/
                context.startActivity(new Intent(context, PlayMusicActivity.class));
                //Log.i(TAG, "onClick: localMusic_Ll_playBar mPosition="+mPosition);
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
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDetach: onDestroyView");
    }

}
