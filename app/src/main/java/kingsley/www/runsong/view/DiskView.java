package kingsley.www.runsong.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import kingsley.www.runsong.R;

public  class DiskView extends
        RelativeLayout {
    FrameLayout frameLayout_Disk;
    ImageView imageView_Pin;
    CircleImageView circleImageView_Header;

    public DiskView(Context context,
                    AttributeSet attrs) {
        super(context, attrs);
        //解析布局
        View view=View.inflate(
                context,
                R.layout.include_discview_layout,
                this);
        frameLayout_Disk= (FrameLayout) view.
                findViewById(R.id.frameLayout_Disk);
        imageView_Pin= (ImageView) view.
                findViewById(R.id.imageView_Disk_Pin);
        circleImageView_Header= (CircleImageView)
                view.findViewById(R.id.imageView_Disk_Header);
    }
    //开始播放唱片时的动画
    public void startRotation(){
       //清除应用在控件上的原有的动画
       frameLayout_Disk.clearAnimation();
        imageView_Pin.clearAnimation();

        //定义一个动画对象
        //绕着自己的0度的位置转25度角
        RotateAnimation pin_anim=
                new RotateAnimation(0,25,
                        RotateAnimation.RELATIVE_TO_SELF
                        ,0.0f,
                        RotateAnimation.RELATIVE_TO_SELF,
                        0.0f);
        pin_anim.setDuration(2000);
        pin_anim.setFillAfter(true);
        //应用动画
        imageView_Pin.setAnimation(pin_anim);
        //定义唱片的动画对象
        RotateAnimation disk_anim=
                new RotateAnimation(
                        0,359,
                        RotateAnimation.RELATIVE_TO_SELF,
                        0.5f,
                        RotateAnimation.RELATIVE_TO_SELF,
                        0.5f);
        disk_anim.setDuration(10000);
        //设置旋转的重复方式
        disk_anim.setRepeatCount(Animation.INFINITE);
        //设置它的旋转的方式是匀速旋转
        disk_anim.setInterpolator(
                new LinearInterpolator());
        frameLayout_Disk.setAnimation(disk_anim);
    }

    //停止的旋转动画
    public void stopRotation(){
        frameLayout_Disk.clearAnimation();
        imageView_Pin.clearAnimation();
        RotateAnimation back_anim=new RotateAnimation(
                25,0,
                RotateAnimation.RELATIVE_TO_SELF,0.0f,
                RotateAnimation.RELATIVE_TO_SELF,0.0f);
        back_anim.setDuration(2000);
        back_anim.setFillAfter(true);
        imageView_Pin.setAnimation(back_anim);
    }
    public void setAlbumpic(Bitmap bitmap){
        circleImageView_Header.setImageBitmap(bitmap);
    }
    public void setAlbumpic(int resource){
        circleImageView_Header.setImageResource(resource);
    }
    public CircleImageView getAlbumpic(){
        return  circleImageView_Header;
    }
}
