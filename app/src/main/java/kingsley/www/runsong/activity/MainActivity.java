package kingsley.www.runsong.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import kingsley.www.runsong.R;
import kingsley.www.runsong.cache.CacheMusic;
import kingsley.www.runsong.fragment.MusicFragment;
import kingsley.www.runsong.fragment.MusicLocalFragment;
import kingsley.www.runsong.fragment.PlayMusicFragment;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle toggle;
    private PlayMusicFragment mPlayMusicFragment;
    public MusicLocalFragment mMusicLocalFragment;
    private boolean isPlayFragmentShow;
    private boolean isFirstLocalMusicFragmentShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        initView();
        setToolBar();
        setDrawerLayout();
    }

    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutId);
        mToolbar = (Toolbar) findViewById(R.id.toolbarId);
    }

    private void setToolBar() {
        setSupportActionBar(mToolbar);
        if(getSupportActionBar()!=null)
        getSupportActionBar().setTitle("");
        RadioGroup mRadioGroup = (RadioGroup) View.inflate(getApplicationContext(),R.layout.toolbar_radiogroup,null);
        //为Toolbar添加RadioGroup
        mToolbar.addView(mRadioGroup,new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT,Toolbar.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        mRadioGroup.setOnCheckedChangeListener(this);
        mRadioGroup.check(R.id.toolbar_Rb_Music);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setDrawerLayout() {
        //获取DrawerLayout对象
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayoutId);
        //获取NavigationView对象
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationViewId);
        //为NavigationView设置Item点击事件
        navigationView.setNavigationItemSelectedListener(this);
        //让图片显示本身的颜色
        navigationView.setItemIconTintList(null);
        //为DrawerLayout设置监听器ActionBarDrawerToggle(多态)
        //ActionBarDrawerToggle实现了DrawerListener,可以直接调用
        toggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        };
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        switch (checkedId){
            case R.id.toolbar_Rb_Music:
                fTransaction.replace(R.id.frameLayoutId,new MusicFragment());
                break;
            case R.id.toolbar_Rb_Run:
                //fTransaction.replace(R.id.frameLayoutId,new MusicFragment());
                break;
            case R.id.toolbar_Rb_Book:
                //fTransaction.replace(R.id.frameLayoutId,new MusicFragment());
                break;
        }
        fTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawerExitId:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.drawerRecentlyPlayedId:

                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.drawerMyCollectionId:

                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.drawerSystemSettingsId:

                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        toggle.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    //显示PlayMusicFragment和隐藏MusicLocalFragment
    public void showPlayMusicFragment() {
        if (isPlayFragmentShow) {
            return;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_up, 0);
        if (mPlayMusicFragment == null) {
            mPlayMusicFragment = new PlayMusicFragment();
            //如不把MusicLocalFragment隐藏会出现点击bug(可以在PlayMusicFragment中点击到mMusicLocalFragment的播放按钮)
            if (mMusicLocalFragment != null)
                ft.hide(mMusicLocalFragment);
            ft.replace(android.R.id.content, mPlayMusicFragment);
        } else {
            ft.show(mPlayMusicFragment);
        }
        ft.commitAllowingStateLoss();
        //isPlayFragmentShow = true;
    }
    //隐藏PlayMusicFragment和显示MusicLocalFragment
    public void hidePlayMusicFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(0, R.anim.fragment_slide_down);
        if (mMusicLocalFragment != null)
            ft.show(mMusicLocalFragment);
        ft.hide(mPlayMusicFragment);
        ft.commitAllowingStateLoss();
        isPlayFragmentShow = false;
    }

    //activity进入栈时调用
    @Override
    protected void onPause() {
        super.onPause();
        //isFirstLocalMusicFragmentShow = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: "+CacheMusic.IsMusicChange);
        //从PlayMusicActivity中返回时调用
        /*if (isFirstLocalMusicFragmentShow) {
            isFirstLocalMusicFragmentShow = false;
            MusicLocalFragment mlfragment = (MusicLocalFragment) CacheMusic.IsMusicChange;
            mlfragment.isResumeInit = true;
            mlfragment.setView(getPlayService().getPlayingPosition());
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CacheMusic.IsMusicChange = null;
    }

    @Override
    public void onBackPressed() {
        /*if (isPlayFragmentShow){
            hidePlayMusicFragment();
            return;
        }*/
        super.onBackPressed();
    }
}
