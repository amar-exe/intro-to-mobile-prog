package ba.edu.ibu.bg;

import android.media.Image;
import android.net.Uri;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class Post {
    private String postID;
    private String postedOn;
    private String imageURL;
    private String[] likers;
    private String username;
    private String numOfLikes;

    public Post(){

    }

    public Post(String postID, String postedOn, String imageURL, String username) {
        this.postID = postID;
        this.username = username;
        this.postedOn = postedOn;
        this.imageURL = imageURL;
    }

    public String getNumOfLikes(){
        return numOfLikes;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(String postedOn) {
        this.postedOn = postedOn;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String[] getLikers() {
        return likers;
    }

    public void setLikers(String[] likers) {
        this.likers = likers;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
