package in.blogspot.programmingdesire.sfbrowsernew.bookmark;

/**
 * Created by Shashi kumar on 08-Apr-18.
 */

public class BookMarkModelClass {
    String BM_url,BM_title;

    public BookMarkModelClass(String BM_url, String BM_title) {
        this.BM_url = BM_url;
        this.BM_title = BM_title;
    }

    public String getBM_url() {
        return BM_url;
    }

    public void setBM_url(String BM_url) {
        this.BM_url = BM_url;
    }

    public String getBM_title() {
        return BM_title;
    }

    public void setBM_title(String BM_title) {
        this.BM_title = BM_title;
    }
}
