package kingsley.www.runsong.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/6/27 19:39
 * file change date : on 2017/6/27 19:39
 * version: 1.0
 */

public class AutoLoadListView extends ListView {
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

    //当ListView拖到底部时回调
    public interface OnLoadListener {
        void onLoad();
    }
}
