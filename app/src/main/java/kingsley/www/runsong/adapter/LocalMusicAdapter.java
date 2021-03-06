package kingsley.www.runsong.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wevey.selector.dialog.DialogOnItemClickListener;
import com.wevey.selector.dialog.NormalSelectionDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.onekeyshare.OnekeyShare;
import kingsley.www.runsong.R;
import kingsley.www.runsong.entity.Music;
import kingsley.www.runsong.utils.CoverLoader;
import kingsley.www.runsong.utils.FileUtil;
import kingsley.www.runsong.view.CircleImageView;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/5/25 09:32
 * file change date : on 2017/5/25 09:32
 * version: 1.0
 */

public class LocalMusicAdapter extends RecyclerView.Adapter<LocalMusicAdapter.ViewHolder> implements View.OnClickListener {

    private NormalSelectionDialog dialog;

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.songItem_Iv_songImage)
        public CircleImageView songImage;
        @BindView(R.id.songItem_Iv_More)
        public ImageView songMoreImage;
        @BindView(R.id.songItem_Tv_songTitle)
        public TextView songTitle;
        @BindView(R.id.songItem_Tv_SongArtist)
        public TextView songArtist;
        @BindView(R.id.songItem_cardView)
        public CardView mCardView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    private List<Music> musicList = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private CoverLoader coverLoader;
    private Context context;
    private RecyclerView mRecyclerView;

    public LocalMusicAdapter(Context context, List<Music> musics) {
        musicList = musics;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        coverLoader = CoverLoader.getInstance();
    }

    public interface OnItemClickListener {
        /**
         * 点击recyclerView中的item时执行此方法
         * @param parent   指向RecyclerView
         * @param view     指向ItemView
         * @param position itemView在RecyclerView中的位置
         */
        void onItemClick(RecyclerView parent, View view, int position);
    }

    //当调用setItemClickListener方法时会自动调用onClick方法
    private OnItemClickListener onItemClickListener;

    public void setItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.local_song_tiem, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Music music = musicList.get(position);
        String path = FileUtil.getAlbumFilePath(music);
        if (path == null) {
            holder.songImage.setImageResource(R.mipmap.i_love_my_music);
        } else {
            Bitmap songImage = coverLoader.loadBitmapForPath(path, 54);
            holder.songImage.setImageBitmap(songImage);
        }

        holder.songTitle.setText(music.getTitle());
        holder.songArtist.setText(music.getArtist());
        holder.mCardView.setOnClickListener(this);
        holder.songMoreImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int position;
        switch (view.getId()) {
            case R.id.songItem_cardView:
                position = mRecyclerView.getChildAdapterPosition(view);
                onItemClickListener.onItemClick(mRecyclerView, view, position);
                break;
            case R.id.songItem_Iv_More:
                View v = (View) view.getParent().getParent();
                position = mRecyclerView.getChildAdapterPosition(v);
                //HttpClient.getSongListInfo("2",10,0);
                showShareDiaLog(position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return musicList != null ? musicList.size() : 0;
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

    private void showShareDiaLog(final int mPosition) {
        //设置item标题
        final ArrayList<String> s = new ArrayList<>();
        s.add("下一首播放");
        s.add("添加到歌单");
        s.add("分享");
        s.add("删除");
        dialog = new NormalSelectionDialog.Builder(context)
                .setlTitleVisible(true)   //设置是否显示标题
                .setTitleHeight(50)   //设置标题高度
                .setTitleText("提示")  //设置标题提示文本
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
                        String title = musicList.get(position).getTitle();
                        Toast.makeText(context, s.get(position) + ": " + title, Toast.LENGTH_SHORT).show();
                        if ("分享".equals(s.get(position))){
                            showShare(musicList.get(position));
                        }
                        dialog.dismiss();
                    }
                })
                .setCanceledOnTouchOutside(true)  //设置是否可点击其他地方取消dialog
                .build();
        dialog.setDataList(s);
        dialog.show();
    }

    public void addAll(List<Music> musics, boolean isClear) {
        if (isClear) {
            musicList.clear();
        }
        musicList.addAll(musics);
    }

    private void showShare(Music music) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        //oks.setTitleUrl(music.getTitle());
        // text是分享文本，所有平台都需要这个字段
        oks.setText(music.getTitle());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/splash_layout.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(music.getTitle()+" 不错哦,强烈推荐!! - RunSong");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(context.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        //oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(context);
    }

}
