package core;

import javafx.scene.image.Image;

/**
 * Created by mattm on 4/21/2017
 */
public class ImageProxy {

    private Image theImage;
    private String url;

    public ImageProxy(String url) {
        this.url = url;
    }

    public Image getImage() {
        if(theImage == null) {
            theImage = new Image(getClass().getClassLoader().getResourceAsStream(url));
        }
        return theImage;
    }

    public String getURL() {
        return url;
    }

    public String toString() {
        return "ImageProxy for " + getURL();
    }

}
