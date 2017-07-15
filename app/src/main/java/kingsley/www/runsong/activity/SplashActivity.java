package kingsley.www.runsong.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import java.io.IOException;

import kingsley.www.runsong.R;
import kingsley.www.runsong.adapter.SplashPagerAdapter;
import kingsley.www.runsong.cache.AppCache;
import kingsley.www.runsong.service.PlayService;
import kingsley.www.runsong.view.MyVideoView;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplashActivity extends BaseActivity implements ViewPager.OnPageChangeListener, ServiceConnection, View.OnClickListener {

    private Button mBtnStartActivity;
    private ViewPager mViewpager;
    private MyVideoView mVideoView;
    private Button mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        //设置视频播放
        mVideoView = (MyVideoView) findViewById(R.id.videoView);
        mVideoView.setVideoURI(Uri.parse("android.resource://"+this.getPackageName()+"/"+R.raw.kr36));
        mVideoView.start();
        //设置视频播放完成后的监听
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVideoView.start();
            }
        });
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);
        //setContentView(R.layout.activity_splash);
        //初始化控件及为ViePager设置adapter
        //initView();
        //为button设置监听事件
        //setListener();
        bindService(new Intent(getApplicationContext(), PlayService.class),this, Context.BIND_AUTO_CREATE);
/*        new Thread(new Runnable() {
            @Override
            public void run() {
                oKHttpTest();
            }
        }).start();*/

    }
    private void oKHttpTest(){
        String url = "https://www.baidu.com/";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            Log.i("TAG", "oKHttpTest: "+response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setListener() {
        mBtnStartActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mViewpager.addOnPageChangeListener(this);
    }

    private void initView() {
        RadioGroup mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup_splashId);
        mViewpager = (ViewPager) findViewById(R.id.viewPager_splash);
        mBtnStartActivity = (Button) findViewById(R.id.button_startActivityId);
        //设置adapter
        mViewpager.setAdapter(new SplashPagerAdapter(getApplicationContext()));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //当翻到最后一页时显示button
//        if (position == 2) mBtnStartActivity.setVisibility(View.VISIBLE);
//        else mBtnStartActivity.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        final PlayService playService = ((PlayService.PlayBinder) service).getService();
        //把PlayService赋给AppCache的mPlayService
        AppCache.setPlayService(playService);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
    }

    @Override
    protected void onDestroy() {
        unbindService(this);
        startService(new Intent(this,PlayService.class));
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},101);
        }else {
            startMainActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode ==101) {
            startMainActivity();
        }else {
            toast("权限被拒绝");
            finish();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void startMainActivity() {
        mVideoView.pause();
        mVideoView=null;
        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
