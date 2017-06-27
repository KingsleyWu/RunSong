package kingsley.www.runsong.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import kingsley.www.runsong.Http.HttpCallBack;
import kingsley.www.runsong.Http.HttpClient;
import kingsley.www.runsong.R;
import kingsley.www.runsong.adapter.OnLineMusicAdapter;
import kingsley.www.runsong.entity.OnLineMusic;
import kingsley.www.runsong.entity.OnLineMusicList;
import kingsley.www.runsong.entity.SongListInfo;
import kingsley.www.runsong.service.PlayService;

public class OnlineMusicActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private Toolbar mToolbar;
    private ListView mOnlineLv;
    private SongListInfo songListInfo;
    private List<OnLineMusic> data;
    private int musicListSize = 20;
    private OnLineMusicList onLineMusicLists;
    private OnLineMusicAdapter adapter;
    private PlayService playService;
    private ImageView mIvHeaderBg;
    private ImageView mIvCover;
    private TextView mTvTitle;
    private TextView mTvUpdateDate;
    private TextView mTvComment;
    private View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_music);
        playService = getPlayService();
        songListInfo = (SongListInfo) getIntent().getSerializableExtra("songListInfo");
        setTitle(songListInfo.getTitle());
        initView();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.online_toolbar);
        if (mToolbar == null) {
            throw new IllegalStateException("Layout is required to include a Toolbar with id 'toolbar'");
        }
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mOnlineLv = (ListView) findViewById(R.id.online_lv);
        addHeaderView();
        data = new ArrayList<>();
        adapter = new OnLineMusicAdapter(this,data);
        mOnlineLv.setAdapter(adapter);
        mOnlineLv.setOnItemClickListener(this);
    }

    private void addHeaderView() {
        headerView = LayoutInflater.from(this).inflate(R.layout.activity_online_music_list_header,null);
        mOnlineLv.addHeaderView(headerView);
    }

    private void initHeaderView(View view) {
        mIvHeaderBg = (ImageView) view.findViewById(R.id.iv_header_bg);
        mIvCover = (ImageView) view.findViewById(R.id.iv_cover);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mTvUpdateDate = (TextView) view.findViewById(R.id.tv_update_date);
        mTvComment = (TextView) view.findViewById(R.id.tv_comment);
        mTvTitle.setText(onLineMusicLists.getBillboard().getName());
        mTvUpdateDate.setText("最近更新："+onLineMusicLists.getBillboard().getUpdate_date());
        mTvComment.setText(onLineMusicLists.getBillboard().getComment());
        Glide.with(this).load(onLineMusicLists.getBillboard().getPic_s640()).into(mIvCover);
        Glide.with(this).load(onLineMusicLists.getBillboard().getPic_s640()).into(mIvHeaderBg);
    }

    private void initDatas(){
        HttpClient.getSongListInfo(songListInfo.getType(), musicListSize, 0, new HttpCallBack<OnLineMusicList>() {
            @Override
            public void onSuccess(OnLineMusicList onLineMusicList) {
                onLineMusicLists = onLineMusicList;
                initHeaderView(headerView);
                data.addAll(onLineMusicList.getSong_list());
                adapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        initDatas();
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
        OnLineMusic onLineMusic = (OnLineMusic) parent.getAdapter().getItem(position);
        HttpClient.getDownloadMusicInfo(onLineMusic.getSong_id());
    }
}
