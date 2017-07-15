package kingsley.www.runsong.view.lrcView;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/7/9 20:33
 * file change date : on 2017/7/9 20:33
 * version: 1.0
 */

public class LrcBuilder implements ILrcBuilder {
    private static final String TAG = "LrcBuilder";

    @Override
    public List<LrcRow> getLrcRows(String rawLrc) {
        if (rawLrc == null || rawLrc.length() == 0) {
            Log.e(TAG, "getLrcRows rawLrc null or empty");
            return null;
        }
        BufferedReader reader = new BufferedReader(new StringReader(rawLrc));
        List<LrcRow> lrcRows = new ArrayList<>();
        String line;
        try {
            while ((line = reader.readLine()) != null && line.length() > 0) {
                List<LrcRow> lrcRow = LrcRow.createLrcRows(line);
                if (lrcRow != null && lrcRow.size() > 0) {
                    for (LrcRow row : lrcRow) {
                        lrcRows.add(row);
                    }
                }
            }

            if (lrcRows.size() > 0) {
                //根据歌词行的时间排序
                Collections.sort(lrcRows);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return lrcRows;
    }
}
