package kingsley.www.runsong.utils;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/5/24 23:37
 * file change date : on 2017/5/24 23:37
 * version: 1.0
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class CoverLoader {
    private static CoverLoader INSTANCE;

    private CoverLoader() {}

    public static CoverLoader getInstance() {
        if (INSTANCE == null){
            synchronized (CoverLoader.class){
                if (INSTANCE == null){
                    INSTANCE = new CoverLoader();
                }
            }
        }
        return INSTANCE;
    }


    /**
     * 获得指定大小的bitmap
     */
    private Bitmap loadBitmap(String path, int length) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 仅获取大小
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int maxLength = Math.max(options.outWidth, options.outHeight);
        // 压缩尺寸，避免卡顿
        int inSampleSize = maxLength / length;
        if (inSampleSize < 1) {
            inSampleSize = 1;
        }
        options.inSampleSize = inSampleSize;
        // 获取bitmap
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(path, options);
    }

    public Bitmap loadBitmapForPath(String path, int length){
        return loadBitmap(path,length);
    }


}
