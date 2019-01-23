package com.example.calnourish;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class for specifying different categories within Category View.
     * cardIndex: card shows up at this index
     * cardText: text shown on the card
     * photo: image shown in the background
     * color: color of the color overlay over the background image
     * opacity: how transparent to make the color overlay on the background image
 */

public class Category implements Serializable {
    private String text;
    private String photo;
    private Integer drawable;

    public Category (String text, String photo) {
        this.text = text;
        this.photo = photo;
    }

    public Category (String text, Integer drawable) {
        this.text = text;
        this.drawable = drawable;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getDrawable() {
        return drawable;
    }

    public void setDrawable(Integer drawable) {
        this.drawable = drawable;
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
