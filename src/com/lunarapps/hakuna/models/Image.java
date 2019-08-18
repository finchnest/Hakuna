package com.lunarapps.hakuna.models;

public class Image extends Doc {


    private static final long serialVersionUID = 141414L;

    private byte[] imagesBytes;

    public Image(String name, User uploader,  Accessibility access, Type type, String path, int downloadCount, byte[] imageBytes) {
        super(name, uploader, access,type, path, downloadCount);
        this.imagesBytes=imageBytes;
    }

    public byte[] getImagesBytes(){
        return imagesBytes;
    }


}
