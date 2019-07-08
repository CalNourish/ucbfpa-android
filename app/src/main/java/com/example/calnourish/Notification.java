package com.example.calnourish;

import java.io.Serializable;
import java.util.Objects;

public class Notification implements Serializable {
    private String title;
    private String body;

    public Notification(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public String getTitle() {
        return title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int hashCode() {
        return Objects.hash(title);
    }

}
