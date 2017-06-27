package kingsley.www.runsong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import kingsley.www.runsong.R;
import kingsley.www.runsong.entity.OnLineMusic;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/6/27 18:36
 * file change date : on 2017/6/27 18:36
 * version: 1.0
 */

public class OnLineMusicAdapter extends BaseAdapter {
    private List<OnLineMusic> mData;
    private Context mContext;
    private LayoutInflater inflater;

    public OnLineMusicAdapter(Context context, List<OnLineMusic> data) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.online_music_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        OnLineMusic onLineMusic = mData.get(position);
        Glide.with(mContext).load(onLineMusic.getPic_small()).into(holder.mOnlineItemIvCover);
        holder.mOnlineItemTvTitle.setText(onLineMusic.getTitle());
        holder.mOnlineItemTvArtist.setText(onLineMusic.getArtist_name());
        return convertView;
    }

    private class ViewHolder {

        private View mOnlineItemVPlaying;
        private ImageView mOnlineItemIvCover;
        private TextView mOnlineItemTvTitle;
        private TextView mOnlineItemTvArtist;
        private ImageView mOnlineItemIvMore;

        private ViewHolder(View view) {
            initView(view);
        }

        private void initView(View view) {
            mOnlineItemVPlaying = view.findViewById(R.id.online_item_v_playing);
            mOnlineItemIvCover = (ImageView) view.findViewById(R.id.online_item_iv_cover);
            mOnlineItemTvTitle = (TextView) view.findViewById(R.id.online_item_tv_title);
            mOnlineItemTvArtist = (TextView) view.findViewById(R.id.online_item_tv_artist);
            mOnlineItemIvMore = (ImageView) view.findViewById(R.id.online_item_iv_more);
        }
    }
}
