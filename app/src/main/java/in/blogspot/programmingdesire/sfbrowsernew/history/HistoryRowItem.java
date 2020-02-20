package in.blogspot.programmingdesire.sfbrowsernew.history;

/**
 * Created by Shashi kumar on 07-Feb-18.
 */

public class HistoryRowItem {
    String hisTitle,hisUrl,hisDate;

    public HistoryRowItem(String hisTitle, String hisUrl, String hisDate) {
        this.hisTitle = hisTitle;
        this.hisUrl = hisUrl;
        this.hisDate = hisDate;
    }

    public String getHisTitle() {
        return hisTitle;
    }

    public String getHisUrl() {
        return hisUrl;
    }

    public String getHisDate() {
        return hisDate;
    }
}
