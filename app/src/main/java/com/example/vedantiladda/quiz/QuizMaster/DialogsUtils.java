package com.example.vedantiladda.quiz.QuizMaster;

import android.app.ProgressDialog;
import android.content.Context;

public class DialogsUtils {
    private Context context;
    private ProgressDialog m_Dialog;

    public DialogsUtils(Context context){
        this.context = context;
        m_Dialog = new ProgressDialog(this.context);
    }

    public void  showProgressDialog(){
        m_Dialog.setMessage("wait");
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        m_Dialog.show();
    }
    public void  dismissDialogBox(){
        m_Dialog.dismiss();

    }

}
