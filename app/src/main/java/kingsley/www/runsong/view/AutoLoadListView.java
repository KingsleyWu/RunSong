package kingsley.www.runsong.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import kingsley.www.runsong.R;

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
        initFooterView();
    }

    public AutoLoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFooterView();
    }

    public AutoLoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFooterView();
    }

    private void initFooterView() {
        vFooter = LayoutInflater.from(getContext()).inflate(R.layout.auto_load_list_view_footer, null);
        addFooterView(vFooter, null, false);
        setOnScrollListener(this);
        onLoadComplete();
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
        mFirstVisibleItem = firstVisibleItem;
    }
    public void setEnable(boolean enable) {
        mEnableLoad = enable;
    }

    private void onLoad(){
        mIsLoading = true;
        addFooterView(vFooter,null,false);
        if (mListener != null){
            mListener.onLoad();
        }
    }
    public void onLoadComplete() {
        mIsLoading = false;
        removeFooterView(vFooter);
    }
    public void setOnLoadListener(OnLoadListener listener) {
        mListener = listener;
    }
    //当ListView拖到底部时回调
    public interface OnLoadListener {
        void onLoad();
    }
}
