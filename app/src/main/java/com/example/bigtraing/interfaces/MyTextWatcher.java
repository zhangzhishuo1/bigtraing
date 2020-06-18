package com.example.bigtraing.interfaces;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * 作者：Leishen  秦宇
 * 创建于： 2020/6/3 13:47
 * 作者邮箱：1623060075@qq.com
 */

public abstract class MyTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        onMyTextChanged(charSequence, i, i1, i2);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public abstract void onMyTextChanged(CharSequence charSequence, int i, int i1, int i2);
}
