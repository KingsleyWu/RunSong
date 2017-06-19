package kingsley.www.runsong.Http;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/6/17 14:32
 * file change date : on 2017/6/17 14:32
 * version: 1.0
 */

public abstract class HttpCallBack<T> {
    public abstract void onSuccess(T t);
}
