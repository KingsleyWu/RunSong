package kingsley.www.runsong.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/6/27 19:39
 * file change date : on 2017/6/27 19:39
 * version: 1.0
 */

public class AutoLoadListView extends ListView implements AbsListView.OnScrollListener{
    private View vFooter;
    private OnLoadListener mListener;
    private int mFirstVisibleItem = 0;
    private boolean mEnableLoad = true;
    private boolean mIsLoading = false;

    public AutoLoadListView(Context context) {
        super(context);
    }

    public AutoLoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoLoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        boolean isPullDown = firstVisibleItem > mFirstVisibleItem;
        if (mEnableLoad && !mIsLoading && isPullDown){
            int lastVisibleItem = firstVisibleItem +visibleItemCount;
            //监听是否为最后一个listView的item,如是则进行加载数据
            if (lastVisibleItem >= totalItemCount - 1){
                onLoad();
            }
        }
    }

    private void onLoad(){
        mIsLoading = true;
        addFooterView(vFooter,null,false);
        if (mListener != null){
            mListener.onLoad();
        }
    }
    public void setOnLoadListener(OnLoadListener listener) {
        mListener = listener;
    }
    //当ListView拖到底部时回调
    public interface OnLoadListener {
        void onLoad();
    }
}
