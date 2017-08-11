package kingsley.www.runsong.fragment;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import butterknife.BindView;
import kingsley.www.runsong.R;
import kingsley.www.runsong.cache.CacheMusic;
import kingsley.www.runsong.m_interface.IIsMusicChange;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends BaseFragment {
    private static final String TAG = "MusicFragment";
    @BindView(R.id.mainMusic_TabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.mainMusic_ViewPager)
    ViewPager mViewPager;
    @BindView(R.id.music_fragment)
    FrameLayout mFragment;
    private Context context;
    private int[] imgResId = {R.mipmap.my, R.mipmap.internet};
    private String[] tabTitles = {"我的", "音乐馆"};

    public MusicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View createMyView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void doBusiness() {

    }


    private void initView() {
        FragmentPagerAdapter mAdapter = new MusicFrgAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private class MusicFrgAdapter extends FragmentPagerAdapter {
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
            // 返回ICON和文字
            Drawable image = context.getResources().getDrawable(imgResId[position]);
            image.setBounds(0, 0, image.getIntrinsicWidth() / 2, image.getIntrinsicHeight() / 2);
            SpannableString sb = new SpannableString("   " + tabTitles[position]);
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
        }

        @Override
        public Fragment getItem(int position) {
            Log.i(TAG, "getItem: position = " + position);
            switch (position) {
                case LOCALMUSIC:
                    if (musicLocalFragment == null) {
                        musicLocalFragment = new MusicLocalFragment();
                        CacheMusic.isMusicChange = (IIsMusicChange) musicLocalFragment;
                        //activity.mMusicLocalFragment = (MusicLocalFragment) musicLocalFragment;
                    }
                    return musicLocalFragment;

                case NETMUSIC:
                default:
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(null,2000);
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
