package com.example.happyghost.addcustomview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.example.happyghost.addcustomview.widget.AddView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mContanter;
    private AddView mAddView;
    private int mWidth;
    private int currentAdd = 0;
//    private RelativeLayout mAddContain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWidth = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        mContanter = (LinearLayout) findViewById(R.id.contanter);
//        mAddContain = (RelativeLayout) findViewById(R.id.addContain);
        mAddView = (AddView) findViewById(R.id.addview);
    }

    private void initData() {
        mAddView.setOnAddviewClickListener(new AddView.OnAddviewClickListener() {

            private int height;
            private int anInt;

            @Override
            public void onAddViewClick() {

                getSubView();
                TextView addTextView = (TextView) getAddView();
//                int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
//                int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
//                addTextView.measure(w, h);
//                int width =addTextView.getMeasuredWidth();
//                int height =addTextView.getMeasuredHeight();




                anInt = addTextView.getMeasuredHeight();
                height = (int) mAddView.getHeight()+anInt;
                //(猜想)这个50应该是addView的高度（xml里的设置），比这个大或小都会出现addview距离新增加的view变大或变小
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(currentAdd*60,60*(1+currentAdd));
                valueAnimator.setTarget(mAddView);
                valueAnimator.setDuration(2000);
                valueAnimator.start();
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mAddView.setTranslationY((float) animation.getAnimatedValue());
                    }
                });
//                int[] a1 = new int[2];
//                mAddView.getLocationInWindow(a1);
//                int x1 = a1[0];
//                int y1 = a1[1];
//                Log.e("addview::","x0:"+x1+"==y0:"+y1);
//                Log.e("addview::","currentAdd*height:"+currentAdd*height);
                Log.e("addview::","-mAddView.getHeight():"+-mAddView.getHeight());
                currentAdd++;

            }
        });
    }

    private void getSubView() {
        AddView addView = new AddView(MainActivity.this);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)new LinearLayout.LayoutParams(50,50);
        layoutParams.gravity= Gravity.CENTER_HORIZONTAL;
        addView.setLayoutParams(layoutParams);
        addView.setmHmove(true);
        addView.startAnimationV();
        mContanter.addView(addView);
        ObjectAnimator oject1 = ObjectAnimator.ofFloat(addView,"translationY",-50);
        oject1.setDuration(1000);
        ObjectAnimator oject2 = ObjectAnimator.ofFloat(addView,"translationX",-mWidth/2+60);
        oject2.setDuration(1000);
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(oject1,oject2);
        set.start();

//        addView.animate().alpha(1)
//                .translationX(-mWidth/2+60).setDuration(2000).start();
    }

    @NonNull
    private View getAddView() {
        final TextView addTextView = new TextView(MainActivity.this);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(360,50);
        params1.gravity = Gravity.CENTER_HORIZONTAL;
        addTextView.setLayoutParams(params1);
        addTextView.setBackgroundColor(Color.parseColor("#FF4081"));
        addTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        addTextView.setText("新成员");
        mContanter.addView(addTextView);
        addTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"xin cheng yuan"+currentAdd,Toast.LENGTH_SHORT).show();
            }
        });
        addTextView.animate().translationY(-100).alpha(1).setDuration(2000).start();
        return addTextView;
    }

}
