package kingsley.www.runsong.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kingsley.www.runsong.R;
import kingsley.www.runsong.activity.MainActivity;
import kingsley.www.runsong.m_interface.IIsMusicChange;
import kingsley.www.runsong.cache.CacheMusic;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends BaseFragment {
    private static final String TAG = "MusicFragment";
    private ViewPager mViewPager;
    private MainActivity activity;
    public MusicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_main, container, false);
        setViewPager(view);
        setTabLayout(view);
        return view;
    }

    private void setViewPager(View view) {
        Log.i(TAG, "setViewPager: ");
        mViewPager = (ViewPager) view.findViewById(R.id.mainMusic_ViewPager);
        FragmentPagerAdapter mAdapter = new MusicFrgAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
    }

    private void setTabLayout(View view) {
        Log.i(TAG, "setTabLayout: ");
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.mainMusic_TabLayout);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void init() {

    }

    private class MusicFrgAdapter extends FragmentPagerAdapter{
        private static final String TAG = "MusicFrgAdapter";
        private Fragment musicLocalFragment;
        private Fragment musicNetFragment;
        private static final int LOCALMUSIC = 0;
        private static final int NETMUSIC = 1;
        private MusicFrgAdapter(FragmentManager fm) {
            super(fm);
            //Log.i(TAG, "MusicFrgAdapter: ");
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //Log.i(TAG, "getPageTitle: ");
            switch (position){
                case 0:
                    return "我的";
                case 1:
                    return "音乐馆";
            }
            return null;
        }

        @Override
        public Fragment getItem(int position) {
            Log.i(TAG, "getItem: position = " + position);
            switch (position){
                case LOCALMUSIC:
                    if (musicLocalFragment == null) {
                        musicLocalFragment = new MusicLocalFragment();
                        CacheMusic.isMusicChange = (IIsMusicChange) musicLocalFragment;
                        //activity.mMusicLocalFragment = (MusicLocalFragment) musicLocalFragment;
                    }
                    return musicLocalFragment;

                case NETMUSIC:
                    default:
                    if (musicNetFragment == null)
                        musicNetFragment = new MusicNetFragment();
                        Log.i(TAG, "getItem: MusicNetFragment");
                    return musicNetFragment;
            }
        }

        @Override
        public int getCount() {
            //Log.i(TAG, "getCount: ");
            return 2;
        }

    }

}
