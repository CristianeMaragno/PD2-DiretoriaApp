package com.cristianerm.pd2diretoria;

public class RemoverUserItem {
    private int mImageResource;
    private String mText;
    private String mEmail;

    public RemoverUserItem(int imageResource, String text, String email) {
        mImageResource = imageResource;
        mText = text;
        mEmail = email;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public String getText() {
        return mText;
    }

    public String getEmail() {
        return mEmail;
    }

}