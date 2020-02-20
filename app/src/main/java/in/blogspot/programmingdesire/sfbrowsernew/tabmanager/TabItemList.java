package in.blogspot.programmingdesire.sfbrowsernew.tabmanager;

import android.graphics.Bitmap;

/**
 * Created by Shashi kumar on 16-Feb-18.
 */

public class TabItemList {

    String urlTitle;
    String tabTag;
    Bitmap image;

    public String getTabTag() {
        return tabTag;
    }

    public void setTabTag(String tabTag) {
        this.tabTag = tabTag;
    }

    public TabItemList(String urlTitle,String tabTag) {
        this.urlTitle = urlTitle;
        this.tabTag=tabTag;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getUrlTitle() {
        return urlTitle;
    }

    public void setUrlTitle(String urlTitle) {
        this.urlTitle = urlTitle;
    }
}
