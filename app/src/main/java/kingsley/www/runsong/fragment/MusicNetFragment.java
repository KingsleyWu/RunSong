package kingsley.www.runsong.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kingsley.www.runsong.R;
import kingsley.www.runsong.activity.OnlineMusicActivity;
import kingsley.www.runsong.adapter.NetMusicListAdapter;
import kingsley.www.runsong.cache.AppCache;
import kingsley.www.runsong.entity.SongListInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicNetFragment extends BaseFragment implements NetMusicListAdapter.OnItemClickListener {
    private static final String TAG = "MusicNetFragment";
    @BindView(R.id.netMusicRecyclerView)
    RecyclerView mNetMusicRecyclerView;
    @BindView(R.id.netMusic_loading)
    LinearLayout mMetMusicLoading;
    @BindView(R.id.netMusic_load_fail)
    LinearLayout mNetMusicLoadFail;
    private List<SongListInfo> mSongLists;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach: ");
        checkList();
    }

    public MusicNetFragment() {
        mSongLists = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                oKHttpTest();
            }
        }).start();*/
        View view = inflater.inflate(R.layout.fragment_music_net, container, false);
        ButterKnife.bind(this,view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        Log.d(TAG, "onCreateView: ");
        mNetMusicRecyclerView.setLayoutManager(layoutManager);
        NetMusicListAdapter adapter = new NetMusicListAdapter(getContext(), mSongLists);
        mNetMusicRecyclerView.setAdapter(adapter);
        adapter.setItemClickListener(this);
        return view;
    }

    private void checkList(){
        mSongLists = AppCache.getSongListInfos();
        if (mSongLists.isEmpty()) {
            String[] titles = getResources().getStringArray(R.array.online_music_list_title);
            String[] types = getResources().getStringArray(R.array.online_music_list_type);
            for (int i = 0; i < titles.length; i++) {
                SongListInfo info = new SongListInfo();
                info.setTitle(titles[i]);
                info.setType(types[i]);
                mSongLists.add(info);
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        SongListInfo listInfo = mSongLists.get(position);
        Intent intent = new Intent(getContext(), OnlineMusicActivity.class);
        intent.putExtra("songListInfo",listInfo);
        startActivity(intent);
    }



       /* private String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?size=20&type=2&callback=cb_list&_t=1468380543284&format=json&method=baidu.ting.billboard.billList";
    private String mUrl = "http:\\/\\/musicdata.baidu.com\\/data2\\/pic\\/d59cab8d47b4ae5cd500cbb67de9cc5c\\/540108358\\/540108358.jpg@s_0,w_150";
    private String mUri = "http://www.xiami.com/song/playlist/id/1773430479";
    private void oKHttpTest(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(mUrl)
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            Log.i(TAG, "oKHttpTest: "+response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
