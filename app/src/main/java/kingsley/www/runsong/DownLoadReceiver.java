package kingsley.www.runsong;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.widget.Toast;

import kingsley.www.runsong.cache.AppCache;
import kingsley.www.runsong.cache.CacheMusic;
import kingsley.www.runsong.utils.MusicUtils;

public class DownLoadReceiver extends BroadcastReceiver {
    private static final String TAG = "DownLoadReceiver";
    @Override
    public void onReceive(final Context context, Intent intent) {
        long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
        checkStatus(context,downId);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //加载本地歌曲数据,把本地歌曲数据给到CacheMusic
                MusicUtils.scanMusic(context, CacheMusic.getInstance().getMusicList());
                context.unregisterReceiver(DownLoadReceiver.this);
            }
        },1000);
    }

    //检查下载状态
    private void checkStatus(Context context, long downId){
        Query query = new Query();
        //通过下载的id查找
        query.setFilterById(downId);
        DownloadManager downloadManager  = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst()) {
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                //下载暂停
                case DownloadManager.STATUS_PAUSED:

                    break;
                //下载延迟
                case DownloadManager.STATUS_PENDING:

                    break;
                //正在下载
                case DownloadManager.STATUS_RUNNING:
                    Toast.makeText(context, AppCache.getmDownloadList().get(downId)+"正在下载中...", Toast.LENGTH_SHORT).show();
                    break;
                //下载完成
                case DownloadManager.STATUS_SUCCESSFUL:
                    Toast.makeText(context, AppCache.getmDownloadList().get(downId)+"下载完成", Toast.LENGTH_SHORT).show();
                    break;
                //下载失败
                case DownloadManager.STATUS_FAILED:
                    Toast.makeText(context, AppCache.getmDownloadList().get(downId)+"下载失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    }
}
