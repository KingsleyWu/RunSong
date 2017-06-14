package kingsley.www.runsong.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kingsley.www.runsong.R;
import kingsley.www.runsong.utils.DensityUtil;
import kingsley.www.runsong.view.CircleImageView;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/6/7 14:12
 * file change date : on 2017/6/7 14:12
 * version: 1.0
 */

public class PlayMusicPagerAdapter extends PagerAdapter {
    private Context mContext;
    List<View> views;
    private LayoutInflater mLayoutInflater;
    private ImageView mImageViewDisk;
    private CircleImageView mImageViewDiskHeader;
    private TextView mTextViewLrc;
    private FrameLayout mFrameLayoutDisk;
    private ImageView mImageViewDiskPin;
    private Animation rotateAnimation;

    public PlayMusicPagerAdapter(Context context) { mContext = context;
        views = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(mContext);
        View view = mLayoutInflater.inflate(R.layout.include_discview_layout,null);
        initView(view);
        views.add(view);
    }

    private void initView(View view) {
        mImageViewDiskPin = (ImageView) view.findViewById(R.id.imageView_Disk_Pin);
        mFrameLayoutDisk = (FrameLayout) view.findViewById(R.id.frameLayout_Disk);
        mTextViewLrc = (TextView) view.findViewById(R.id.textView_Lrc);
        mImageViewDiskHeader = (CircleImageView) view.findViewById(R.id.imageView_Disk_Header);
        mImageViewDisk = (ImageView) view.findViewById(R.id.imageView_Disk);
        rotaView(mFrameLayoutDisk,0);
        rotaPin(mImageViewDiskPin,0);
    }


    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view == views.get((Integer) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View item = views.get(position);
        container.addView(item);
        return position;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get((Integer) object));
    }
    public void rotaView(View view, long startOffset) {
        rotateAnimation = AnimationUtils.loadAnimation(mContext, R.anim.play_music_anim);
        LinearInterpolator lin = new LinearInterpolator();
        rotateAnimation.setInterpolator(lin);
        rotateAnimation.setStartOffset(startOffset);
        view.startAnimation(rotateAnimation);
        view.invalidate();
    }

    public  void rotaPin(View view, long startOffset) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"rotation",0,25);
        animator.setDuration(2000);
        animator.setStartDelay(startOffset);
        animator.start();
        int x = DensityUtil.px2dip(mContext,200);
        int y = DensityUtil.px2dip(mContext,20);
        view.setPivotX(x);
        view.setPivotY(y);
    }
}
