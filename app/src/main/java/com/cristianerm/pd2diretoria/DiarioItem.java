package com.cristianerm.pd2diretoria;

public class DiarioItem {
    private int mImageResource;
    private String mText;
    private String mImageUrl;

    public DiarioItem(int imageResource, String text, String imageUrl) {
        mImageResource = imageResource;
        mText = text;
        mImageUrl = imageUrl;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public String getText() {
        return mText;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

}
