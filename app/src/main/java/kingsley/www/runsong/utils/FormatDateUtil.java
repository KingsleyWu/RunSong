package kingsley.www.runsong.utils;

import android.text.format.DateUtils;

import java.util.Locale;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/6/8 15:55
 * file change date : on 2017/6/8 15:55
 * version: 1.0
 */

public class FormatDateUtil {
    public static String formatTime(String pattern, long milli) {
        int m = (int) (milli / DateUtils.MINUTE_IN_MILLIS);
        int s = (int) ((milli / DateUtils.SECOND_IN_MILLIS) % 60);
        String mm = String.format(Locale.getDefault(), "%02d", m);
        String ss = String.format(Locale.getDefault(), "%02d", s);
        return pattern.replace("mm", mm).replace("ss", ss);
    }
}
