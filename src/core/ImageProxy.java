package core;

import javafx.scene.image.Image;

/**
 * Created by mattm on 4/21/2017
 */
public class ImageProxy {

    private Image theImage;
    private final String url;

    public ImageProxy(String url) {
        this.url = url;
    }

    public Image getImage() {
        if(theImage == null) {
            theImage = new Image(Utils.getResourceAsStream(url));
        }
        return theImage;
    }

    private String getURL() {
        return url;
    }

    public String toString() {
        return "ImageProxy for " + getURL();
    }

}
