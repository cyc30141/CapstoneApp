package com.inhatc.capstone;

/**
 * Created by YoungChul on 2017-05-10.
 */

import android.graphics.drawable.Drawable;

public class ListViewItem {
    // 아이템 타입을 구분하기 위한 type 변수.
    private int type ;

    private Drawable iconDrawable ;
    private String nameStr ;

    public void setType(int type) {
        this.type = type ;
    }
    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setName(String name) {
        nameStr = name ;
    }

    public int getType() {
        return this.type ;
    }
    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getName() {
        return this.nameStr ;
    }
}