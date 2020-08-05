package com.cristianerm.pd2diretoria;

public class ExcluirBoletosItem {
    private int mImageResource;
    private String mText;
    private String mUserId;
    private String mUserName;

    public ExcluirBoletosItem(int imageResource, String text, String userId, String userName) {
        mImageResource = imageResource;
        mText = text;
        mUserId = userId;
        mUserName = userName;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public String getText() {
        return mText;
    }

    public String getUserId() {
        return mUserId;
    }

    public String getUserName() {
        return mUserName;
    }


}
