package kingsley.www.runsong.dialog;

import android.content.Context;

import java.util.ArrayList;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/6/4 15:18
 * file change date : on 2017/6/4 15:18
 * version: 1.0
 */

public class DialogManger {

    public static void showShareDiaLog(Context context){

    }
/*    NormalSelectionDialog dialog1 = new NormalSelectionDialog.Builder(this)
            .setlTitleVisible(true)   //设置是否显示标题
            .setTitleHeight(65)   //设置标题高度
            .setTitleText("please select")  //设置标题提示文本
            .setTitleTextSize(14) //设置标题字体大小 sp
            .setTitleTextColor(R.color.colorPrimary) //设置标题文本颜色
            .setItemHeight(40)  //设置item的高度
            .setItemWidth(0.9f)  //屏幕宽度*0.9
            .setItemTextColor(R.color.colorPrimaryDark)  //设置item字体颜色
            .setItemTextSize(14)  //设置item字体大小
            .setCancleButtonText("Cancle")  //设置最底部“取消”按钮文本
            .setOnItemListener(new DialogOnItemClickListener() {  //监听item点击事件
                @Override
                public void onItemClick(Button button, int position) {
//                                dialog1.dismiss();
                    Toast.makeText(MainActivity.this, s.get(position), Toast.LENGTH_SHORT).show();

                }
            })
            .setCanceledOnTouchOutside(true)  //设置是否可点击其他地方取消dialog
            .build();*/
    ArrayList<String> s = new ArrayList<>();

        //dialog1.setDataList(s);
}
