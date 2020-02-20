package in.blogspot.programmingdesire.sfbrowsernew.download;

public class DownloadingFileModelClass {
    private String filename;
    private String url;
    private String downloadFilePath;


    public DownloadingFileModelClass(String filename, String url, String downloadFilePath) {
        this.filename = filename;
        this.url = url;
        this.downloadFilePath = downloadFilePath;
    }

    public String getDownloadFilePath() {
        return downloadFilePath;
    }

    public void setDownloadFilePath(String downloadFilePath) {
        this.downloadFilePath = downloadFilePath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
