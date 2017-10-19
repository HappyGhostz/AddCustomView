package com.example.happyghost.addcustomview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.happyghost.addcustomview.widget.AddView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mContanter;
    private AddView mAddView;
    private int mWidth;
    private int currentAdd = 0;
    private int currentSub ;
    private int width;
    private int height;
    private boolean isFrist = true;
    private EditText addTextView;
    private Button button;
    private int subViewHeight;
    private int addViewHeight;
    private int mAddViewStartY;
    private int mNewAddViewStartY;
    private int mNewSubView;
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
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addTextView!=null&& addTextView.getId()!=-1){
                    EditText editText = (EditText) findViewById(1 * 100000);
                    String fristText = editText.getText().toString();
                    button.setText(fristText);
                }
            }
        });


    }

    private void initData() {
        mAddView.setMAngleV1(360);
        getMeasureWH(mAddView);
        addViewHeight = mAddView.getMeasuredHeight();
        int[] a1 = new int[2];
        mAddView.getLocationInWindow(a1);
        mAddViewStartY = a1[1];
        Log.e("addview::","mAddViewStartY:"+mAddViewStartY);
        Log.e("addview::","addViewHeight:"+addViewHeight);
        mAddView.setOnAddviewClickListener(new AddView.OnAddviewClickListener() {

            private int paddingTop;

            @Override
            public void onAddViewClick() {

                getSubView();
                TextView addTextView = (TextView) getAddView();
                if(isFrist){
                    paddingTop = mAddView.getPaddingTop();
                    getValueAnmition(0,mNewSubView-mAddViewStartY);
//                    getValueAnmition(currentSub*60,60*(1+currentSub));
                }else {
                    getValueAnmition(currentSub*50+10,50*(1+currentSub)+10);
                }

//                int[] a1 = new int[2];
//                mAddView.getLocationInWindow(a1);
//                int x1 = a1[0];
//                int y1 = a1[1];
//                Log.e("addview::","x0:"+x1+"==y0:"+y1);
//                Log.e("addview::","currentAdd*height:"+currentAdd*height);
                Log.e("addview::","-mAddView.getHeight():"+-mAddView.getHeight());
                currentAdd++;
                currentSub++;
                isFrist=false;
            }
        });
    }

    private void getValueAnmition(float value1,float value2) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(value1,value2);
        valueAnimator.setTarget(mAddView);
        valueAnimator.setDuration(2000);
//        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAddView.setTranslationY((float) animation.getAnimatedValue());
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
        addView.setId(currentAdd);
        addView.setOnAddviewClickListener(new AddViewClick(addView));
        getMeasureWH(addView);
        subViewHeight = addView.getMeasuredHeight();
        int[] a1 = new int[2];
        addView.getLocationInWindow(a1);
        mNewSubView = a1[1];

        Log.e("addview::","subViewHeight:"+subViewHeight);
        Log.e("addview::","mNewSubView:"+mNewSubView);
        mContanter.addView(addView);
//        ObjectAnimator oject1 = ObjectAnimator.ofFloat(addView,"translationY",-50*(1+currentAdd));
        ObjectAnimator oject1 = ObjectAnimator.ofFloat(addView,"translationY",-addViewHeight);
        oject1.setDuration(500);
        ObjectAnimator oject2 = ObjectAnimator.ofFloat(addView,"translationX",-mWidth/2+60);
        oject2.setDuration(1500);
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(oject1,oject2);
//        set.setDuration(2000);
//        set.start();
//        addView.animate().alpha(1)
//                .translationX(-mWidth/2+60).setDuration(2000).start();
    }

    @NonNull
    private View getAddView() {


        addTextView = new EditText(MainActivity.this);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(360,50);
        params1.gravity = Gravity.CENTER_HORIZONTAL;
        addTextView.setLayoutParams(params1);
        addTextView.setBackgroundColor(Color.parseColor("#FF4081"));
        addTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        addTextView.setId((currentAdd+1)*100000);//扩大一下ID以免和Addview的id造成冲突
        addTextView.setHint("编号:"+ addTextView.getId()+"请输入内容:");
//        addTextView.setText("新成员");
        mContanter.addView(addTextView);
        getMeasureWH(addTextView);
        width = addTextView.getMeasuredWidth();
        height = addTextView.getMeasuredHeight();
        int[] a1 = new int[2];
        addTextView.getLocationInWindow(a1);
        mNewAddViewStartY = a1[1];
        Log.e("addview::","mNewAddViewStartY:"+mNewAddViewStartY);
        Log.e("addview::","height:"+height);
        if(isFrist){
            getAnimatorSet(addTextView,"alpha","translationY",0,1,-(mNewAddViewStartY-mAddViewStartY));
        }else {
            getAnimatorSet(addTextView,"alpha","translationY",0,1,-100*(currentAdd+1)+50*currentAdd);
        }

        return addTextView;
    }

    private void getMeasureWH(View addView) {
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        addView.measure(w, h);
    }

    //比较懒
    private AnimatorSet getAnimatorSet(View view, String fristProperty, String secondProperty, float float1, float float2, float float3) {
        ObjectAnimator oject1 = ObjectAnimator.ofFloat(view,fristProperty,float1,float2);
        ObjectAnimator oject2 = ObjectAnimator.ofFloat(view,secondProperty,float3);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(oject1,oject2);
        set.setDuration(2000);
//        set.start();
        return set;
    }
    private AnimatorSet animatorSet;
    public class AddViewClick implements AddView.OnAddviewClickListener{

        private final AddView newAddView;


        public AddViewClick(AddView addView){
            this.newAddView = addView;
        }

        @Override
        public void onAddViewClick() {
            int id = newAddView.getId();
            final EditText viewById = (EditText)findViewById((id+1) * 100000);
            if(animatorSet!=null&&animatorSet.isRunning()){
                newAddView.setmVmoveOne(true);
                return;
            }else {
                animatorSet = getAnimatorSet(viewById, "alpha", "translationY", 1, 0, 50);
                animatorSet.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        viewById.setVisibility(View.GONE);
                        getValueAnmition(50*(1+currentSub)+10,currentSub*50+10);
                        mAddView.startAnimationV();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                AnimatorSet animatorSet1 = getAnimatorSet(newAddView, "alpha", "translationX", 1, 0, mWidth/2+60);
                animatorSet1.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                          newAddView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                currentSub--;
            }


        }
    }

}
