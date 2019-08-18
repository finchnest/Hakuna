package com.lunarapps.hakuna.models;

import java.io.Serializable;
import java.sql.Date;

public class History implements Serializable {

    private static final long serialVersionUID = 131313L;
    private User searcher;
    private String search;
    private Date date;


    public History(User searcher, String search, Date date) {

        this.searcher = searcher;
        this.search = search;
        this.date = date;

    }


    public User getSearcher() {
        return searcher;
    }

    public String getSearch() {
        return search;
    }

    public Date getDate() {
        return date;
    }
}