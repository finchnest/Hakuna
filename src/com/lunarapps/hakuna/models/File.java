package com.lunarapps.hakuna.models;

public class File extends Doc {

    private static final long serialVersionUID = 121212L;

    public File(String name, User uploader,  Accessibility access, Type type, String path, int downloadCount) {
        super(name, uploader, access,type, path, downloadCount);
    }


}
