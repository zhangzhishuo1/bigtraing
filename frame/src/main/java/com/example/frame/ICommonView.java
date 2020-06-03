package com.example.frame;


public interface ICommonView<D> {

    void onSuccess(int whichApi, D... pD);


    void onFailed(int whichApi, Throwable pThrowable);
}
