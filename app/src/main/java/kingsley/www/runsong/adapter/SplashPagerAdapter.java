package kingsley.www.runsong.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import kingsley.www.runsong.R;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/5/22 22:38
 * file change date : on 2017/5/22 22:38
 * version: 1.0
 */

public class SplashPagerAdapter extends PagerAdapter {
    private static final String TAG = "SplashPagerAdapter";
    //图片资源
    private int[] images ={R.mipmap.splash1,R.mipmap.splash2,R.mipmap.splash3};
    //显示的View
    private List<ImageView> imageViews;
    //Context
    private Context context;

    /**
     * 显示的View数量
     * @return 返回需显示的数量
     */
    @Override
    public int getCount() {
        return images.length;
    }
    //判断显示的View与返回的view是否一致
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    //自动销毁没显示的View
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imageViews.get(position));
    }
    //为父View添加子view
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(imageViews.get(position));
        return imageViews.get(position);
    }
    //初始化数据添加显示的图片
    private void init() {
        imageViews = new ArrayList<>();
        for (int image1 : images) {
            ImageView image = new ImageView(context);
            image.setImageResource(image1);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            //使图片实现可以放大缩小的功能
            //PhotoViewAttacher mAttacher = new PhotoViewAttacher(image);
            imageViews.add(image);
        }
    }
    //构造方法,传递Context 及 Button进来
    public SplashPagerAdapter (Context context){
        this.context =context;
        //数据初始化
        init();
    }
}
