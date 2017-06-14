package kingsley.www.runsong.fragment;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kingsley.www.runsong.R;
import kingsley.www.runsong.adapter.PlayMusicPagerAdapter;
import kingsley.www.runsong.service.PlayService;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayMusicFragment extends BaseFragment {

    private LinearLayout mPlayMusicLlContent;
    private ImageView playmusicsongBg;
    private ImageView playmusicivback;
    private TextView playmusictvtitle;
    private ImageView playmusicivmore;
    private Toolbar playmusictoolbar;
    private ImageView playmusicivdisc;
    private RelativeLayout playmusicrotaView;
    private ImageView playmusicivpin;
    private ViewPager playMusicViewPager;
    private TextView playmusictvcurrentPosition;
    private TextView playmusictvsongDuration;
    private ImageView playmusicivplayMode;
    private ImageView playmusicivlastSong;
    private ImageView playmusicivplaypause;
    private ImageView playmusicivnextSong;
    private ImageView playmusicivmoreList;
    private ImageView playmusicivcollect;
    private ImageView playmusicivdownload;
    private ImageView playmusicivshare;
    private ImageView playmusicivmsg;
    private Context mContext;
    private PlayService playService;
    public PlayMusicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_music, container, false);
        initView(view);
        setListeners();
        return view;
    }

    private void setListeners() {


    }

    private void initView(View view) {
        mPlayMusicLlContent = (LinearLayout) view.findViewById(R.id.play_music_ll_content);
        this.playmusicivmsg = (ImageView) view.findViewById(R.id.play_music_iv_msg);
        this.playmusicivshare = (ImageView) view.findViewById(R.id.play_music_iv_share);
        this.playmusicivdownload = (ImageView) view.findViewById(R.id.play_music_iv_download);
        this.playmusicivcollect = (ImageView) view.findViewById(R.id.play_music_iv_collect);
        this.playmusicivmoreList = (ImageView) view.findViewById(R.id.play_music_iv_moreList);
        this.playmusicivnextSong = (ImageView) view.findViewById(R.id.play_music_iv_nextSong);
        this.playmusicivplaypause = (ImageView) view.findViewById(R.id.play_music_iv_play_pause);
        this.playmusicivlastSong = (ImageView) view.findViewById(R.id.play_music_iv_lastSong);
        this.playmusicivplayMode = (ImageView) view.findViewById(R.id.play_music_iv_playMode);
        this.playmusictvsongDuration = (TextView) view.findViewById(R.id.play_music_tv_songDuration);
        this.playmusictvcurrentPosition = (TextView) view.findViewById(R.id.play_music_tv_currentPosition);
        playMusicViewPager = (ViewPager) view.findViewById(R.id.play_music_ViewPager);
        playMusicViewPager.setAdapter(new PlayMusicPagerAdapter(getActivity()));
        this.playmusictoolbar = (Toolbar) view.findViewById(R.id.play_music_toolbar);
        this.playmusicivmore = (ImageView) view.findViewById(R.id.play_music_iv_more);
        this.playmusictvtitle = (TextView) view.findViewById(R.id.play_music_tv_title);
        this.playmusicivback = (ImageView) view.findViewById(R.id.play_music_iv_back);
        this.playmusicsongBg = (ImageView) view.findViewById(R.id.play_music_songBg);

        //playMusicViewPager.addView(coverView);
        initSystemBar();
    }

    @Override
    protected void init() {
        playService = getPlayService();
    }
    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            int top = getResources().getDimensionPixelOffset(resourceId);
            mPlayMusicLlContent.setPadding(0, top, 0, 0);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
