package kingsley.www.runsong.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kingsley.www.runsong.Http.HttpCallBack;
import kingsley.www.runsong.Http.HttpClient;
import kingsley.www.runsong.R;
import kingsley.www.runsong.adapter.OnLineMusicAdapter;
import kingsley.www.runsong.cache.AppCache;
import kingsley.www.runsong.cache.CacheMusic;
import kingsley.www.runsong.entity.DownloadInfo;
import kingsley.www.runsong.entity.Music;
import kingsley.www.runsong.entity.OnLineMusic;
import kingsley.www.runsong.entity.OnLineMusicList;
import kingsley.www.runsong.entity.SongListInfo;
import kingsley.www.runsong.service.PlayService;
import kingsley.www.runsong.utils.FileUtil;
import kingsley.www.runsong.utils.MusicUtils;
import kingsley.www.runsong.view.AutoLoadListView;

public class OnlineMusicActivity extends BaseActivity implements AdapterView.OnItemClickListener, AutoLoadListView.OnLoadListener {

    @BindView(R.id.online_toolbar)
    public Toolbar mToolbar;
    @BindView(R.id.online_lv)
    public AutoLoadListView mOnlineLv;
    public ImageView mIvHeaderBg;
    public ImageView mIvCover;
    public TextView mTvTitle;
    public TextView mTvUpdateDate;
    public TextView mTvComment;
    public View headerView;
    private SongListInfo songListInfo;
    private List<OnLineMusic> data;
    private int musicListSize = 20;
    private OnLineMusicList onLineMusicLists;
    private OnLineMusicAdapter adapter;
    private PlayService playService;
    private int mOffset = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_music);
        ButterKnife.bind(this);
        playService = getPlayService();
        songListInfo = (SongListInfo) getIntent().getSerializableExtra("songListInfo");
        setTitle(songListInfo.getTitle());
        initView();
    }

    private void initView() {
        if (mToolbar == null) {
            throw new IllegalStateException("Layout is required to include a Toolbar with id 'toolbar'");
        }
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        addHeaderView();
        data = new ArrayList<>();
        adapter = new OnLineMusicAdapter(this,data);
        mOnlineLv.setAdapter(adapter);
        mOnlineLv.setOnItemClickListener(this);
        mOnlineLv.setOnLoadListener(this);
    }

    private void addHeaderView() {
        headerView = LayoutInflater.from(this).inflate(R.layout.activity_online_music_list_header,mOnlineLv,false);
        mOnlineLv.addHeaderView(headerView);
    }

    private void initHeaderView(View view) {
        mIvHeaderBg = (ImageView) view.findViewById(R.id.iv_header_bg);
        mIvCover = (ImageView) view.findViewById(R.id.iv_cover);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mTvUpdateDate = (TextView) view.findViewById(R.id.tv_update_date);
        mTvComment = (TextView) view.findViewById(R.id.tv_comment);
        mTvTitle.setText(onLineMusicLists.getBillboard().getName());
        mTvUpdateDate.setText(""+"最近更新："+onLineMusicLists.getBillboard().getUpdate_date());
        mTvComment.setText(onLineMusicLists.getBillboard().getComment());
        Glide.with(this).load(onLineMusicLists.getBillboard().getPic_s640()).into(mIvCover);
        Glide.with(this).load(onLineMusicLists.getBillboard().getPic_s640()).into(mIvHeaderBg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDatas();
    }

    private void initDatas(){
        HttpClient.getSongListInfo(songListInfo.getType(), musicListSize, mOffset,
                new HttpCallBack<OnLineMusicList>() {
            @Override
            public void onSuccess(OnLineMusicList onLineMusicList) {
                mOnlineLv.onLoadComplete();
                onLineMusicLists = onLineMusicList;
                initHeaderView(headerView);
                mOffset += musicListSize;
                adapter.addAll(onLineMusicList.getSong_list(),false);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Music music = new Music();
        final OnLineMusic onLineMusic = adapter.getItem(position-1);
        log(onLineMusic.getTitle());
        HttpClient.getDownloadMusicInfo(onLineMusic.getSong_id(), new HttpCallBack<DownloadInfo>() {
            @Override
            public void onSuccess(DownloadInfo response) {
                music.setPath(response.getBitrate().getFile_link());
                music.setType(Music.Type.ONLINE);
                music.setAlbum(onLineMusic.getPic_big());
                music.setArtist(onLineMusic.getArtist_name());
                music.setCoverPath(onLineMusic.getPic_small());
                music.setTitle(onLineMusic.getTitle());
                //下载歌词
                String lrcFileName = FileUtil.getLrcFileName(onLineMusic.getArtist_name(), onLineMusic.getTitle());
                File lrcFile = new File(FileUtil.getLrcDir() + lrcFileName);
                if (!TextUtils.isEmpty(onLineMusic.getLrclink()) && !lrcFile.exists()){
                    MusicUtils.downloadMusicLrc(onLineMusic.getLrclink(),lrcFileName);
                }
                //Log.i(TAG, "onResponse: response.getBitrate().getFile_link()="+response.getBitrate().getFile_link()+"  -- id="+id);
                CacheMusic.setOnLineMusic(music);
                AppCache.getPlayService().play(music);
            }
        });
    }

    @Override
    public void onLoad() {
        initDatas();
    }
}
