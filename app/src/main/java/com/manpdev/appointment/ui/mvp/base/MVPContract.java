package com.manpdev.appointment.ui.mvp.base;

/**
 * novoa on 9/24/16.
 */
public interface MVPContract {
    interface Presenter{
        void attachView(MVPContract.View view);
        void detachView();
    }

    interface View{
        void showError(String msg);
        void showError(int msgRes);
    }
}
