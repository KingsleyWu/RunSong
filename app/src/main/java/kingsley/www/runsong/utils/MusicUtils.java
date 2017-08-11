package kingsley.www.runsong.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import kingsley.www.runsong.DownLoadReceiver;
import kingsley.www.runsong.Http.HttpCallBack;
import kingsley.www.runsong.Http.HttpClient;
import kingsley.www.runsong.R;
import kingsley.www.runsong.cache.AppCache;
import kingsley.www.runsong.entity.DownloadInfo;
import kingsley.www.runsong.entity.Music;
import kingsley.www.runsong.entity.OnLineMusic;

/**
 * class name : RanSong
 * author : Kingsley
 * created date : on 2017/5/24 22:45
 * file change date : on 2017/5/24 22:45
 * version: 1.0
 */

public class MusicUtils {
    //扫描歌曲
    public static List<Music> scanMusic(Context context, List<Music> musics){
        //清空list
        musics.clear();
        //查询所有音乐文件
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        //如查询为空则返回null
        if (cursor == null) return null;
        //
        while (cursor.moveToNext()){
            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
            if (isMusic == 0)continue;
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String title = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            //Log.i("title", "scanMusic: title="+title);
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String unknown = context.getString(R.string.unknown);
            artist = (TextUtils.isEmpty(artist) || artist.toLowerCase().contains("unknown")) ? unknown : artist;
            String album = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            long albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            String coverPath = getCoverPath(context, albumId);
            //Log.i("coverPath", "scanMusic: coverPath="+coverPath);
            String fileName = cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
            //Log.i("fileName", "scanMusic: fileName="+fileName);
            long fileSize = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            Music music = new Music();
            music.setId(id);
            music.setType(Music.Type.LOCAL);
            music.setTitle(title);
            music.setArtist(artist);
            music.setAlbum(album);
            music.setDuration(duration);
            music.setPath(path);
            music.setCoverPath(coverPath);
            music.setFileName(fileName);
            music.setFileSize(fileSize);
            musics.add(music);
        }
        cursor.close();
        return musics;
    }
    //获取专辑图片地址
    private static String getCoverPath(Context context, long albumId) {
        String path = null;
        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://media/external/audio/albums/" + albumId),
                new String[]{"album_art"}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToNext() && cursor.getColumnCount() > 0) {
                path = cursor.getString(0);
            }
            cursor.close();
        }
        return path;
    }

    public static void downloadMusic(final Context context, OnLineMusic onLineMusic) {
        final String artist = onLineMusic.getArtist_name();
        final String title = onLineMusic.getTitle();
        // 获取歌曲下载链接
        HttpClient.getDownloadMusicInfo(onLineMusic.getSong_id(), new HttpCallBack<DownloadInfo>() {
            @Override
            public void onSuccess(DownloadInfo response) {
                Log.d("TAG", "onSuccess: response="+response);
                downloadMusic(context,response.getBitrate().getFile_link(), artist, title);
            }
        });
        //下载歌词
        String lrcFileName = FileUtil.getLrcFileName(artist, title);
        File lrcFile = new File(FileUtil.getLrcDir() + lrcFileName);
        if (!TextUtils.isEmpty(onLineMusic.getLrclink()) && !lrcFile.exists()){
            downloadMusicLrc(onLineMusic.getLrclink(),lrcFileName);
        }
        //下载封面
        String albumFileName = FileUtil.getAlbumFileName(artist, title);
        File albumFile = new File(FileUtil.getAlbumDir(),albumFileName);
        String picUrl = onLineMusic.getPic_big();
        if (TextUtils.isEmpty(picUrl)) {
            picUrl = onLineMusic.getPic_small();
        }

        if (!albumFile.exists() && !TextUtils.isEmpty(picUrl)) {
            downloadMusicAlbum(picUrl,albumFileName);
        }
    }

    private static void downloadMusicAlbum(String picUrl, String albumFileName) {
        HttpClient.downloadFile(picUrl, FileUtil.getAlbumDir(), albumFileName, new HttpCallBack<File>() {
            @Override
            public void onSuccess(File file) {
                Log.d("TAG", "downloadMusicAlbum onSuccess: file= "+file);
            }
        });
    }

    public static void downloadMusicLrc(String lrcUrl, String lrcFileName) {
        HttpClient.downloadFile(lrcUrl, FileUtil.getLrcDir(), lrcFileName, new HttpCallBack<File>() {
            @Override
            public void onSuccess(File file) {
                Log.d("TAG", "downloadMusicLrc onSuccess: file= "+file);
            }
        });
    }

    private static void downloadMusic(Context context, String url, String artist, String title) {
        try {
            String fileName = FileUtil.getMp3FileName(artist, title);
            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setAllowedOverRoaming(false);// 不允许漫游
            //在通知栏中显示，默认就是显示的
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            request.setTitle(FileUtil.getFileName(artist, title));
            request.setDescription("正在下载…");
            //设置下载的路径
            request.setDestinationInExternalPublicDir(FileUtil.getRelativeMusicDir(), fileName);
            request.setMimeType(MimeTypeMap.getFileExtensionFromUrl(url));
            request.allowScanningByMediaScanner();
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);

            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            long id = downloadManager.enqueue(request);
            AppCache.getmDownloadList().put(id, title);
            //注册广播接收者，监听下载状态
            context.registerReceiver(new DownLoadReceiver(),
                    new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        } catch (Throwable th) {
            th.printStackTrace();
            Toast.makeText(context, "下载失败...", Toast.LENGTH_SHORT).show();
        }
    }
}
