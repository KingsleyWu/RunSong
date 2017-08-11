package kingsley.www.runsong.m_interface;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/6/5 19:15
 * file change date : on 2017/6/5 19:15
 * version: 1.0
 */

public interface IConstant {
    String SPLASH_URL = "http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";
    String BASE_URL = "http://tingapi.ting.baidu.com/v1/restserver/ting";
    String BASE_URL2 = "http://tingapi.ting.baidu.com/v1/";

    String METHOD_GET_MUSIC_LIST = "baidu.ting.billboard.billList";
    String METHOD_DOWNLOAD_MUSIC = "baidu.ting.song.play";
    String METHOD_ARTIST_INFO = "baidu.ting.artist.getInfo";
    String METHOD_SEARCH_MUSIC = "baidu.ting.search.catalogSug";
    String METHOD_LRC = "baidu.ting.song.lry";
    String PARAM_METHOD = "method";
    String PARAM_TYPE = "type";
    String PARAM_SIZE = "size";
    String PARAM_OFFSET = "offset";
    String PARAM_SONG_ID = "songid";
    String PARAM_TING_UID = "tinguid";
    String PARAM_QUERY = "query";
    int PLAY_MODE_LOOP = 0;
    int PLAY_MODE_SINGLE_LOOP = 1;
    int PLAY_MODE_RANDOM = 2;
    int ISONLINEMUSIC = -100;
    boolean DEBUG = true;
}
