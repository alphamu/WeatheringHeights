package com.alimuzaffar.weatherapp.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alimuzaffar.weatherapp.R;
import com.alimuzaffar.weatherapp.util.DateUtils;

public class TransparentProgressDialog extends Dialog {

	private ImageView iv;

  Runnable mTimeout = new Runnable() {
    @Override
    public void run() {
      try {
        setCancelable(true);
      } catch (Exception e){}
    }
  };

	public TransparentProgressDialog(Context context) {
		this(context, R.drawable.progress_gray);
	}

	public TransparentProgressDialog(Context context, int resourceIdOfImage) {
		super(context, R.style.TransparentProgressDialog);
        	WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        	wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        	getWindow().setAttributes(wlmp);
		setTitle(null);
		setCancelable(false);
		setOnCancelListener(null);
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		iv = new ImageView(context);
		iv.setImageResource(resourceIdOfImage);
		layout.addView(iv, params);
		addContentView(layout, params);
    iv.postDelayed(mTimeout, DateUtils.SECOND_MILLISECONDS * 10);
	}
		
	@Override
	public void show() {
		super.show();
		RotateAnimation anim = new RotateAnimation(0.0f, 360.0f , Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
		anim.setInterpolator(new LinearInterpolator());
		anim.setRepeatCount(Animation.INFINITE);
		anim.setDuration(2500);
		iv.setAnimation(anim);
		iv.startAnimation(anim);
	}

  @Override
  public void dismiss() {
    iv.removeCallbacks(mTimeout);
    super.dismiss();
  }
}
