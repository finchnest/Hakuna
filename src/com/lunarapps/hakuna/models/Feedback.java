package com.lunarapps.hakuna.models;


import java.io.Serializable;
import java.sql.Date;

public class Feedback implements Serializable {
    private static final long serialVersionUID = 111111L;
    private User commenter;
    private String feed_back;
    private Date feed_date;


    public Feedback(User commenter, String feed_back, Date date) {

        this.commenter = commenter;
        this.feed_back = feed_back;
        feed_date = date;

    }


    public User getCommenter() {
        return commenter;
    }

    public String getFeed_back() {
        return feed_back;
    }

    public Date getFeed_date() {
        return feed_date;
    }
}
