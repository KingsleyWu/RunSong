package kingsley.www.runsong.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kingsley.www.runsong.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SportsFragment extends BaseFragment {

    public SportsFragment() {
    }

    @Override
    public View createMyView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sports, container, false);
    }

    @Override
    public void doBusiness() {

    }

}
