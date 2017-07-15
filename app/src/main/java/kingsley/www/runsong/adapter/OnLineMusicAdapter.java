package kingsley.www.runsong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wevey.selector.dialog.DialogOnItemClickListener;
import com.wevey.selector.dialog.NormalSelectionDialog;

import java.util.ArrayList;
import java.util.List;

import kingsley.www.runsong.R;
import kingsley.www.runsong.entity.OnLineMusic;
import kingsley.www.runsong.utils.MusicUtils;

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
    private NormalSelectionDialog dialog;

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
    public OnLineMusic getItem(int position) {
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
        final OnLineMusic onLineMusic = mData.get(position);
        Glide.with(mContext).load(onLineMusic.getPic_small()).into(holder.mOnlineItemIvCover);
        holder.mOnlineItemTvTitle.setText(onLineMusic.getTitle());
        holder.mOnlineItemTvArtist.setText(onLineMusic.getArtist_name());
        holder.mOnlineItemIvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDiaLog(onLineMusic);
            }
        });
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

    public void addAll(List<OnLineMusic> data,boolean isClear){
        if (data != null) {
            if (isClear) {
                mData.clear();
            }
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void clear(){
        mData.clear();
        notifyDataSetChanged();
    }

    private void showDiaLog(final OnLineMusic onLineMusic){
        //设置item标题
        final ArrayList<String> s = new ArrayList<>();
        s.add("下一首播放");
        s.add("添加到歌单");
        s.add("分享");
        s.add("下载");
        dialog = new NormalSelectionDialog.Builder(mContext)
                .setlTitleVisible(true)   //设置是否显示标题
                .setTitleHeight(50)   //设置标题高度
                .setTitleText(onLineMusic.getTitle())  //设置标题提示文本
                .setTitleTextSize(14) //设置标题字体大小 sp
                .setTitleTextColor(R.color.titleRed) //设置标题文本颜色
                .setItemHeight(40)  //设置item的高度
                .setItemWidth(0.9f)  //屏幕宽度*0.9
                .setItemTextColor(R.color.colorPrimaryDark)  //设置item字体颜色
                .setItemTextSize(14)  //设置item字体大小
                .setCancleButtonText("取消")  //设置最底部“取消”按钮文本
                .setOnItemListener(new DialogOnItemClickListener() {  //监听item点击事件
                    @Override
                    public void onItemClick(Button button, int position) {
                        switch (position) {
                            case 0:
                                Toast.makeText(mContext, "下一首播放", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(mContext, "下一首播放", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(mContext, "分享", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                MusicUtils.downloadMusic(mContext,onLineMusic);
                                break;
                        }
                        dialog.dismiss();
                    }
                })
                .setCanceledOnTouchOutside(true)  //设置是否可点击其他地方取消dialog
                .build();
        dialog.setDataList(s);
        dialog.show();
    }
}
