package kingsley.www.runsong.entity;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/6/6 10:37
 * file change date : on 2017/6/6 10:37
 * version: 1.0
 */

public class DownloadInfo {
    private Bitrate bitrate;

    public Bitrate getBitrate() {
        return bitrate;
    }

    public void setBitrate(Bitrate bitrate) {
        this.bitrate = bitrate;
    }

    public static class Bitrate {
        private int file_duration;
        private String file_link;

        public int getFile_duration() {
            return file_duration;
        }

        public void setFile_duration(int file_duration) {
            this.file_duration = file_duration;
        }

        public String getFile_link() {
            return file_link;
        }

        public void setFile_link(String file_link) {
            this.file_link = file_link;
        }
    }
}
