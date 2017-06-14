package kingsley.www.runsong.entity;

import java.util.Objects;

/**
 * class name : RunSong
 * author : Kingsley
 * created date : on 2017/5/24 22:47
 * file change date : on 2017/5/24 22:47
 * version: 1.0
 */

public class Music {
    // 歌曲类型:本地/网络
    private Type type;
    // [本地歌曲]歌曲id
    private long id;
    // 音乐标题
    private String title;
    // 艺术家
    private String artist;
    // 专辑
    private String album;
    // 持续时间
    private long duration;
    // 音乐路径
    private String path;
    // 专辑封面路径
    private String coverPath;
    // 文件名
    private String fileName;
    // 文件大小
    private long fileSize;

    public Music() {
    }

    public Music(Type type, long id, String title, String artist, String album, long duration, String path, String coverPath, String fileName, long fileSize) {
        this.type = type;
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.path = path;
        this.coverPath = coverPath;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public enum Type {
        LOCAL,
        ONLINE
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Music music = (Music) o;
        return id == music.id &&
                duration == music.duration &&
                fileSize == music.fileSize &&
                type == music.type &&
                Objects.equals(title, music.title) &&
                Objects.equals(artist, music.artist) &&
                Objects.equals(album, music.album) &&
                Objects.equals(path, music.path) &&
                Objects.equals(coverPath, music.coverPath) &&
                Objects.equals(fileName, music.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id, title, artist, album, duration, path, coverPath, fileName, fileSize);
    }
}
