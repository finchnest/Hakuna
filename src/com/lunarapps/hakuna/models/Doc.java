package com.lunarapps.hakuna.models;

import java.io.Serializable;

public abstract class Doc implements Serializable {

    private static final long serialVersionUID = 101010L;

    private String name;
    private User uploader;
    private Accessibility access;
    private Type type;
    private String path;
    private int downloadCount;

    public Doc(String name, User uploader,  Accessibility access, Type type, String path, int downloadCount){
        this.name=name;
        this.uploader=uploader;
        this.access=access;
        this.type=type;
        this.path=path;
        this.downloadCount=downloadCount;
    }

    public String getName() {
        return name;
    }

    public User getUploader() {
        return uploader;
    }

    /**
     * returns the accessibility value*/
    public Accessibility getAccess(){
        return access;
    }

    public String getPath(){
        return path;
    }

    public Type getType(){
        return type;
    }

    public int getDownloadCount(){
        return downloadCount;
    }
}