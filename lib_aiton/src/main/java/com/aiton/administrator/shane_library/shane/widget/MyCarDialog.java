package com.aiton.administrator.shane_library.shane.widget;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.aiton.administrator.shane_library.R;

/**
 * Created by Administrator on 2016/8/11.
 */
public class MyCarDialog extends ProgressDialog {
    private Context context;
    public MyCarDialog(Context context) {
        super(context, R.style.dialog);
        this.context = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }
    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_car_dialog, null);
        setContentView(view);

        ImageView imageView_rotate = (ImageView) view.findViewById(R.id.imageView_rotate);
        imageView_rotate.setImageResource(R.drawable.car_gif);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView_rotate.getDrawable();
        animationDrawable.setOneShot(false);
        animationDrawable.start();
//        Window dialogWindow = getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
//        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
//        dialogWindow.setAttributes(lp);
    }
}
