package com.slash.youth.ui.viewmodel;

import android.app.AlertDialog;
import android.databinding.BaseObservable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.slash.youth.R;
import com.slash.youth.databinding.DialogPasswordBinding;
import com.slash.youth.utils.LogKit;

/**
 * Created by zss on 2016/11/6.
 */
public class DialogPassWorsModel extends BaseObservable {

    private DialogPasswordBinding dialogPasswordBinding;
    public AlertDialog currentDialog;//当前对话框
    private ImageView[] imageViews = new ImageView[6];//用来存小圆点的
    private StringBuffer stringBuffer = new StringBuffer();//存储密码字符
    private int count = 6;
    private String strPassword;//密码字符串

    public DialogPassWorsModel(DialogPasswordBinding dialogPasswordBinding) {
        this.dialogPasswordBinding = dialogPasswordBinding;
        initData();
        initView();
    }

    private void initData() {


    }

    private void initView() {
        imageViews[0]=  dialogPasswordBinding.password1;
        imageViews[1]=  dialogPasswordBinding.password2;
        imageViews[2]=  dialogPasswordBinding.password3;
        imageViews[3]=  dialogPasswordBinding.password4;
        imageViews[4]=  dialogPasswordBinding.password5;
        imageViews[5]=  dialogPasswordBinding.password6;

        //隐藏光标
        dialogPasswordBinding.itemEdittext.setCursorVisible(false);

        dialogPasswordBinding.itemEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
           /*     String s1 = s.toString();
                stringBuffer1.append(s1);
                int length = stringBuffer1.length();
                LogKit.d("==========="+stringBuffer1.toString()+"-----"+length);
*/


                //重点 如果字符不为""时才进行操作
                if (!s.toString().equals("")) {
                    if (DialogPassWorsModel.this.stringBuffer.length()>5){
                        //当密码长度大于5位时edittext置空
                        dialogPasswordBinding.itemEdittext.setText("");
                        return;
                    }else {
                        //将文字添加到StringBuffer中
                        DialogPassWorsModel.this.stringBuffer.append(s);
                        dialogPasswordBinding.itemEdittext.setText("");//添加后将EditText置空 造成没有文字输入的错局
                        count = DialogPassWorsModel.this.stringBuffer.length();//记录stringbuffer的长度
                        strPassword = DialogPassWorsModel.this.stringBuffer.toString();
                        if (DialogPassWorsModel.this.stringBuffer.length()==6){
                            //文字长度位6 则调用完成输入的监听
                            if (inputCompleteListener!=null){
                                inputCompleteListener.inputComplete();
                            }
                        }
                    }

                    for (int i = 0; i< DialogPassWorsModel.this.stringBuffer.length(); i++){
                        imageViews[i].setImageResource(R.drawable.shape_approval_tv_left);
                    }
                }
            }
        });


        dialogPasswordBinding.itemEdittext.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (onKeyDelete()) return true;
                    return true;
                }
                return false;
            }
        });

    }

    public boolean onKeyDelete() {
        if (count==0){
            count = 6;
            return true;
        }
        if (stringBuffer.length()>0){
            //删除相应位置的字符
            stringBuffer.delete((count-1),count);
            count--;
            strPassword = stringBuffer.toString();
            imageViews[stringBuffer.length()].setImageResource(R.drawable.shape_approval_tv_left);

        }
        return false;
    }



  /*  @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }*/



    private InputCompleteListener inputCompleteListener;

    public void setInputCompleteListener(InputCompleteListener inputCompleteListener) {
        this.inputCompleteListener = inputCompleteListener;
    }

    public interface InputCompleteListener{
        void inputComplete();
    }

    public EditText getEditText() {
        return  dialogPasswordBinding.itemEdittext;
    }

    //获取密码
    public String getStrPassword() {
        return strPassword;
    }

    public void setContent(String content){
        dialogPasswordBinding.itemEdittext.setText(content);
    }


    //取消
    public void cannel(View view){
        currentDialog.dismiss();
    }

    //确定
    public void sure(View view){
        currentDialog.dismiss();
    }
}
