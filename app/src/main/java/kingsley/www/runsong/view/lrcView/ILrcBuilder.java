package kingsley.www.runsong.view.lrcView;

import java.util.List;

/**
 * class name : NewTestProject
 * author : Kingsley
 * created date : on 2017/7/9 20:30
 * file change date : on 2017/7/9 20:30
 * version: 1.0
 */

public interface ILrcBuilder {
    List<LrcRow> getLrcRows(String rawLrc);
}
