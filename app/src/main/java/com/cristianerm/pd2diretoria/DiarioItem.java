package com.cristianerm.pd2diretoria;

public class DiarioItem {
    private int mImageResource;
    private String mText;

    public DiarioItem(int imageResource, String text) {
        mImageResource = imageResource;
        mText = text;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public String getText() {
        return mText;
    }

}
