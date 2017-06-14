package kingsley.www.runsong.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.util.List;

import kingsley.www.runsong.R;
import kingsley.www.runsong.entity.Music;

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
}
