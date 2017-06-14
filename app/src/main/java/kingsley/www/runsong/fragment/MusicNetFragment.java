package kingsley.www.runsong.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

import kingsley.www.runsong.R;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicNetFragment extends Fragment {
    private static final String TAG = "MusicNetFragment";


    public MusicNetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                oKHttpTest();
            }
        }).start();*/
        return inflater.inflate(R.layout.fragment_music_net, container, false);
    }
    private String url = "http://tingapi.ting.baidu.com/v1/restserver/ting?size=20&type=2&callback=cb_list&_t=1468380543284&format=json&method=baidu.ting.billboard.billList";
    private String mUrl = "http:\\/\\/musicdata.baidu.com\\/data2\\/pic\\/d59cab8d47b4ae5cd500cbb67de9cc5c\\/540108358\\/540108358.jpg@s_0,w_150";
    private String mUri = "http://www.xiami.com/song/playlist/id/1773430479";
    private void oKHttpTest(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(mUrl)
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            Log.i(TAG, "oKHttpTest: "+response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
