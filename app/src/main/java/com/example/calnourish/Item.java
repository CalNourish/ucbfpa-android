package com.example.calnourish;


import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Objects;

public class Item implements Serializable {
    private String itemName;
    private String itemPoint;
    private String itemCount;
    private String itemImage;
    private Integer drawableImage;

    public Item(String itemName, String itemPoint, String itemCount, String itemImage) {
        this.itemName = itemName;
        this.itemPoint = itemPoint;
        this.itemCount = itemCount;
        this.itemImage = itemImage;
    }

    public String getItemName() {
        return this.itemName;
    }

    public String getItemPoint() { return this.itemPoint; }

    public String getItemCount() {
        return this.itemCount;
    }

    public String getItemImage() { return this.itemImage; }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemPoint(String itemPoint) { this.itemPoint = itemPoint; }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }

    public void setItemImage(String itemImage) { this.itemImage = itemImage; }

    @Override
    public int hashCode() {
        return Objects.hash(itemName);
    }
}
