package com.cristianerm.pd2diretoria;

public class DiarioItem {
    private int mImageResource;
    private String mText;
    private String mImageUrl;
    private String mTurmaSelecionada;

    public DiarioItem(int imageResource, String text, String imageUrl, String turmaSelecionada) {
        mImageResource = imageResource;
        mText = text;
        mImageUrl = imageUrl;
        mTurmaSelecionada = turmaSelecionada;
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

    public String getmTurmaSelecionada() {
        return mTurmaSelecionada;
    }
}
