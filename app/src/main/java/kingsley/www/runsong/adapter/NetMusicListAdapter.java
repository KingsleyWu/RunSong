package kingsley.www.runsong.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import kingsley.www.runsong.Http.HttpCallBack;
import kingsley.www.runsong.Http.HttpClient;
import kingsley.www.runsong.R;
import kingsley.www.runsong.entity.Music;
import kingsley.www.runsong.entity.OnLineMusic;
import kingsley.www.runsong.entity.OnLineMusicList;
import kingsley.www.runsong.entity.SongListInfo;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/6/15 21:04
 * file change date : on 2017/6/15 21:04
 * version: 1.0
 */

public class NetMusicListAdapter extends RecyclerView.Adapter {
    private static final String TAG = "NetMusicListAdapter";
    private Context mContext;
    private static final int TYPE_PROFILE = 0;
    private static final int TYPE_MUSIC_LIST = 1;
    private LayoutInflater inflater;
    private List<SongListInfo> mSongLists;
    private RecyclerView mRecyclerView;
    private static List<OnLineMusic> musicList;
    private static Music music;

    public NetMusicListAdapter(Context context, List<SongListInfo> songLists) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mSongLists = songLists;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.i(TAG, "onCreateViewHolder: viewType="+viewType);
        switch (viewType) {
            case TYPE_PROFILE:
                //Log.i(TAG, "onCreateViewHolder: profile");
                return new ProfileViewHolder(inflater.inflate(R.layout.net_music_list_profile, parent, false));
            case TYPE_MUSIC_LIST:
            default:
                return new MusicListViewHolder(inflater.inflate(R.layout.net_music_list, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getType(position);
    }

    private int getType(int position) {
        //Log.i(TAG, "getType: position ="+position);
        if (mSongLists.get(position).getType().equals("#")) {
            return TYPE_PROFILE;
        } else {
            return TYPE_MUSIC_LIST;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SongListInfo songListInfo = mSongLists.get(position);
        Log.i(TAG, "onBindViewHolder: songListInfo="+songListInfo);
        switch (getType(position)){
            case TYPE_PROFILE:
                ProfileViewHolder profileViewHolder = (ProfileViewHolder) holder;
                profileViewHolder.mProfile.setText(songListInfo.getTitle());
                break;
            case TYPE_MUSIC_LIST:
                MusicListViewHolder musicListViewHolder = (MusicListViewHolder) holder;
                getMusicListInfo(songListInfo,musicListViewHolder);
                break;
        }
    }
    private void getMusicListInfo(final SongListInfo songListInfo, final MusicListViewHolder musicListViewHolder) {
        if (songListInfo.getCoverUrl() == null){
            musicListViewHolder.mIvCover.setImageResource(R.mipmap.i_love_my_music);
            musicListViewHolder.mTvMusic1.setText("1.加载中……");
            musicListViewHolder.mTvMusic2.setText("2.加载中……");
            musicListViewHolder.mTvMusic3.setText("3.加载中……");
            HttpClient.getSongListInfo(songListInfo.getType(), 3, 0, new HttpCallBack<OnLineMusicList>() {
                @Override
                public void onSuccess(OnLineMusicList response) {
                    parse(response,songListInfo);
                    //Log.i(TAG, "onSuccess: musicList="+musicList);
                    setData(songListInfo,musicListViewHolder);
                }
            });
            /*RetrofitClient.getInstance().getSongListInfo(songListInfo.getType(), 3, 0, new Callback<OnLineMusicList>() {
                @Override
                public void onResponse(Call<OnLineMusicList> call, Response<OnLineMusicList> response) {
                    parse(response.body(),songListInfo);
                    //Log.i(TAG, "onSuccess: musicList="+musicList);
                    setData(songListInfo,musicListViewHolder);
                }

                @Override
                public void onFailure(Call<OnLineMusicList> call, Throwable t) {

                }
            });*/
        }else {
            setData(songListInfo,musicListViewHolder);
        }
    }

    private void parse(OnLineMusicList response, SongListInfo songListInfo) {
        //获取在线音乐列表
        List<OnLineMusic> onlineMusics = response.getSong_list();
        //设置列表清单的图片
        songListInfo.setCoverUrl(response.getBillboard().getPic_s260());
        if (onlineMusics.size() >= 1) {
            songListInfo.setMusic1(mContext.getString(R.string.song_list_item_title_1,
                    onlineMusics.get(0).getTitle(), onlineMusics.get(0).getArtist_name()));
        } else {
            songListInfo.setMusic1("");
        }
        if (onlineMusics.size() >= 2) {
            songListInfo.setMusic2(mContext.getString(R.string.song_list_item_title_2,
                    onlineMusics.get(1).getTitle(), onlineMusics.get(1).getArtist_name()));
        } else {
            songListInfo.setMusic2("");
        }
        if (onlineMusics.size() >= 3) {
            songListInfo.setMusic3(mContext.getString(R.string.song_list_item_title_3,
                    onlineMusics.get(2).getTitle(), onlineMusics.get(2).getArtist_name()));
        } else {
            songListInfo.setMusic3("");
        }
    }

    private void setData(SongListInfo songListInfo, MusicListViewHolder MusicListHolder) {
        Glide.with(mContext).load(songListInfo.getCoverUrl()).into(MusicListHolder.mIvCover);
        MusicListHolder.mTvMusic1.setText(songListInfo.getMusic1());
        MusicListHolder.mTvMusic2.setText(songListInfo.getMusic2());
        MusicListHolder.mTvMusic3.setText(songListInfo.getMusic3());
    }

        @Override
    public int getItemCount() {
        return mSongLists.size();
    }

    class ProfileViewHolder extends RecyclerView.ViewHolder {
        TextView mProfile;
        ProfileViewHolder(View itemView) {
            super(itemView);
            mProfile = (TextView) itemView.findViewById(R.id.tv_profile);
        }
    }

    class MusicListViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvCover;
        TextView mTvMusic1;
        TextView mTvMusic2;
        TextView mTvMusic3;
        View mVDivider;
        MusicListViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = mRecyclerView.getChildAdapterPosition(v);
                    onItemClickListener.onItemClick(position);
                }
            });
            mIvCover = (ImageView) itemView.findViewById(R.id.iv_cover);
            mTvMusic1 = (TextView) itemView.findViewById(R.id.tv_music_1);
            mTvMusic2 = (TextView) itemView.findViewById(R.id.tv_music_2);
            mTvMusic3 = (TextView) itemView.findViewById(R.id.tv_music_3);
            mVDivider = itemView.findViewById(R.id.v_divider);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mRecyclerView = null;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    //当调用setItemClickListener方法时会自动调用onClick方法
    private OnItemClickListener onItemClickListener;

    public void setItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
