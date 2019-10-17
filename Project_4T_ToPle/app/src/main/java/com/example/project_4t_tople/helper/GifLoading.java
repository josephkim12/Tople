package com.example.project_4t_tople.helper;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.project_4t_tople.R;

public class GifLoading extends Dialog {

    private ImageView iv;

    public GifLoading(Context context) {
        super(context, R.style.transDialog);
        WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wlmp);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        iv = new ImageView(context);
        //GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
        //Glide.with(context).load(R.drawable.common_google_signin_btn_icon_dark).apply(new RequestOptions().placeholder(R.drawable.common_google_signin_btn_icon_dark)).into(new DrawableImageViewTarget(holder.profileImage));
        Glide.with(context).load(R.raw.gif_loader).into(iv);
        layout.addView(iv, params);
        addContentView(layout, params);
    }

    @Override
    public void show() {
        super.show();
    }


}
