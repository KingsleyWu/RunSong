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
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

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

    /**
     * 将图片剪裁为圆形
     */
    public Bitmap createCircleImage(Bitmap source) {
        int length = Math.min(source.getWidth(), source.getHeight());
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(length, length, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawCircle(length / 2, length / 2, length / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    /**
     * 将图片放大或缩小到指定尺寸
     */
    public Bitmap resizeImage(Bitmap source, int w, int h) {
        int width = source.getWidth();
        int height = source.getHeight();
        float scaleWidth = ((float) w) / width;
        float scaleHeight = ((float) h) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(source, 0, 0, width, height, matrix, true);
    }
}
