package kingsley.www.runsong.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import kingsley.www.runsong.activity.BaseActivity;
import kingsley.www.runsong.cache.AppCache;
import kingsley.www.runsong.m_interface.IConstant;
import kingsley.www.runsong.service.PlayService;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/5/25 18:36
 * file change date : on 2017/5/25 18:36
 * version: 1.0
 */

public abstract class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";
    protected BaseActivity baseActivity;
    protected Toast toast;
    LayoutInflater inflater;
    private Unbinder unbinder;
    protected PlayService getPlayService() {
        PlayService playService = AppCache.getPlayService();
        if (playService == null) {
            throw new NullPointerException("play service is null");
        }
        return playService;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        View view = createMyView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        baseActivity = (BaseActivity)getActivity();
        //设置toolbar显示menu
        setHasOptionsMenu(true);
        toast = Toast.makeText(baseActivity, "", Toast.LENGTH_SHORT);
        doBusiness();
        return view;
    }

    public abstract View createMyView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void doBusiness();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //关于界面跳转
    public void jumpTo(Class<?> clazz, boolean isFinish, boolean isAnimation) {
        Intent intent = new Intent(baseActivity, clazz);
        if (isAnimation) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(baseActivity).toBundle());
            }else {
                startActivity(intent);
            }
        }else {
            startActivity(intent);
        }
        if (isFinish) {

            baseActivity.finish();
        }
    }

    public void jumpTo(Intent intent, boolean isFinish, boolean isAnimation) {
        if (isAnimation) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(baseActivity).toBundle());
            }else {
                startActivity(intent);
            }
        }else {
            startActivity(intent);
        }
        if (isFinish) {
            baseActivity.finish();
        }
    }

    //关于toast和log
    public void toast(String text) {
        if (!TextUtils.isEmpty(text)) {
            toast.setText(text);
            toast.show();
        }
    }

    public void log(String log) {
        if (IConstant.DEBUG) {
            Log.d("TAG", "从" + this.getClass().getSimpleName() + "打印的日志：" + log);
        }
    }

    public void log(int code, String message) {
        String log = "错误信息：" + code + ":" + message;
        log(log);
    }

    public void toastAndLog(String text, String log) {
        toast(text);
        log(log);
    }

    public void toastAndLog(String text, int code, String message) {
        toast(text);
        log(code, message);
    }
}
