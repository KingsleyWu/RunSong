package kingsley.www.runsong.view.lrcView;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/7/9 19:33
 * file change date : on 2017/7/9 19:33
 * version: 1.0
 */

/**
 * 包括该行歌曲的时间,歌词内容
 */
public class LrcRow implements Comparable<LrcRow> {
    private static final String TAG = "LrcRow";
    //该行歌词要开始播放的时间 [02:34:14]
    public String strTime;
    //把上面string类型的时间转换成long类型
    public long time;
    //歌词内容
    public String content;
    public static String reg = "[a-zA-Z]";
    public LrcRow(){}

    public LrcRow(String strTime, long time, String content) {
        this.strTime = strTime;
        this.time = time;
        this.content = content;
    }

    //读取歌词的每一行内容，转换为LrcRow，加入到集合中
    public static List<LrcRow> createLrcRows(String standardLrcLine){
        List<LrcRow> lrcRows = new ArrayList<>();
        if (standardLrcLine.indexOf("[") != 0 || standardLrcLine.indexOf("]") != 9 || standardLrcLine.substring(1,2).matches(reg)){
            Log.d(TAG, "createLrcRows: null   "+standardLrcLine.substring(1,2));
            Log.d(TAG, "standardLrcLine ="+standardLrcLine);
            return lrcRows;
        }
        //找到最后一个 ‘]’ 的位置
        int lastIndexOfRightBracket = standardLrcLine.lastIndexOf("]");
        //获取歌词内容
        String content = standardLrcLine.substring(lastIndexOfRightBracket+1,standardLrcLine.length());

        //将时间格式转换一下  [mm:ss.SS][mm:ss.SS] 转换为  -mm:ss.SS--mm:ss.SS
        String strTime = standardLrcLine.substring(0,lastIndexOfRightBracket+1)
                .replace("[","-").replace("]","-");
        String[] arrTimes = strTime.split("-");
        for (String arrTime : arrTimes) {
            if (arrTime.trim().length() == 0) {
                continue;
            }
            LrcRow lrcRow = new LrcRow(arrTime,timeConvert(arrTime),content);
            Log.d(TAG, "createLrcRows: arrTime="+arrTime);
            lrcRows.add(lrcRow);
        }
        return lrcRows;
    }

    private static long timeConvert(String arrTime) {
        try {
            //因为给如的字符串的时间格式为XX:XX.XX,返回的long要求是以毫秒为单位
            //将字符串 XX:XX.XX 转换为 XX:XX:XX
            String strTime = arrTime.replace(".", ":");
            //将字符串 XX:XX:XX 拆分
            String[] times = strTime.split(":");
            // mm:ss:SS
            //返回毫秒值
            long time = 0;
            for (int i = 0; i < times.length; i++) {
                for (int j = 0; j < 10; j++) {
                    if (times[i].equals(String.valueOf(j))) {
                        if (i==0){
                            time+=Integer.valueOf(times[0]) * 60 * 1000;
                        }else if (i==1){
                            time+=Integer.valueOf(times[1]) * 1000;
                        }else if (i==2){
                            time+=Integer.valueOf(times[2]);
                        }
                    }
                }
            }
            return Integer.valueOf(times[0]) * 60 * 1000
                    + Integer.valueOf(times[1]) * 1000
                    + Integer.valueOf(times[2]);
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "timeConvert: "+e +" arrTime="+arrTime);
            return 0;
        }
    }

    @Override
    public String toString() {
        return "[" + strTime + "]" + content;
    }

    //排序的时候，根据歌词的时间来排序
    @Override
    public int compareTo(@NonNull LrcRow lrcRow) {
        return (int) (time - lrcRow.time);
    }
}
