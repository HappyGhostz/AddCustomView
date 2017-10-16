package com.example.happyghost.addcustomview.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.happyghost.addcustomview.R;

/**
 * @author Zhao Chenping
 * @creat 2017/10/16.
 * @description
 */

public class AddView extends View {

    private int mCircleColor;
    private int mLineColor;
    private float mCircleSize;
    private float mLineSize;
    private int mBackGroundColor;
    private Paint mCriclePaint;
    private Paint mBackground;
    private Paint mLinePaint;
    private int mDefaultSize;
    private int mWidth;
    private int mHeight;
    private int mRadius;
    private double mAngleH1 ;
    private double mAngleV1 ;
    private ObjectAnimator mAngleV1Animator;
    private int mEndTime;
    private boolean mHmove;
    private boolean isAdd = true;
    private ObjectAnimator mAngleH1Animator;
    private float mDisAngle = 0;

    public AddView(Context context) {
        this(context,null);
    }

    public AddView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public AddView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        //自定义属性值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AddCustomView);
        mCircleColor = typedArray.getColor(R.styleable.AddCustomView_av_criclecolor, Color.parseColor("#FF4081"));
        mLineColor = typedArray.getColor(R.styleable.AddCustomView_av_lineColor, Color.BLUE);
        mCircleSize = typedArray.getDimension(R.styleable.AddCustomView_av_arc_size, 1);
        mLineSize = typedArray.getDimension(R.styleable.AddCustomView_av_line_size, 1);
        mBackGroundColor = typedArray.getColor(R.styleable.AddCustomView_av_background, Color.WHITE);
        mEndTime = typedArray.getInteger(R.styleable.AddCustomView_av_animation_time,2);
        mHmove = typedArray.getBoolean(R.styleable.AddCustomView_av_moveH, false);
        typedArray.recycle();
        initCirclrPaint();
        initLinePaint();
        initBackgroubdPaint();
        initClickListener();
    }

    private void initClickListener() {
        OnClickListener clickListener = new OnClickListener(){
            @Override
            public void onClick(View v) {
                startAnimationV();
                isAdd=!isAdd;
            }
        };
        setOnClickListener(clickListener);
    }

    /**
     * 初始化背景画笔
     */
    private void initBackgroubdPaint() {
        mBackground = new Paint();
        mBackground.setAntiAlias(true);
        mBackground.setColor(mBackGroundColor);
    }

    /**
     * 初始加减线条画笔
     */
    private void initLinePaint() {
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStrokeWidth(mLineSize);
    }

    /**
     * 初始化圆环画笔
     */
    private void initCirclrPaint() {
        mCriclePaint = new Paint();
        //抗锯齿
        mCriclePaint.setAntiAlias(true);
        //设置圆环线条颜色
        mCriclePaint.setColor(mCircleColor);
        //设置圆环线条粗细
        mCriclePaint.setStrokeWidth(mCircleSize);
        //设置为空心圆环
        mCriclePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        mDefaultSize = 20;
        if(widthMode == MeasureSpec.EXACTLY&&heightMode==MeasureSpec.EXACTLY){
            setMeasuredDimension(widthSize,heightSize);
        }else if (widthMode==MeasureSpec.EXACTLY){
            setMeasuredDimension(widthSize,mDefaultSize);
        }else if (heightMode == MeasureSpec.EXACTLY){
            setMeasuredDimension(mDefaultSize,heightSize);
        }else {
            setMeasuredDimension(mDefaultSize,mDefaultSize);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = getWidth();
        mHeight = getHeight();
        mRadius = Math.min(mWidth,mHeight)/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackGround(canvas);
        drawCircle(canvas);
        drawLine(canvas);
    }

    private void drawBackGround(Canvas canvas) {
        canvas.drawCircle(mWidth/2,mHeight/2,mRadius,mBackground);
    }

    private void drawCircle(Canvas canvas) {
        //颜色渐变
        LinearGradient linearGradient = new LinearGradient(0,0,mRadius*3.16f,mRadius*3.16f,Color.RED,Color.BLUE, Shader.TileMode.MIRROR);
        mCriclePaint.setShader(linearGradient);
        canvas.drawCircle(mWidth/2,mHeight/2,mRadius-mCircleSize/2,mCriclePaint);
    }

    private void drawLine(Canvas canvas) {
        float sinV =(float) Math.sin(mAngleV1*Math.PI/180);
        float cosV =(float) Math.cos(mAngleV1*Math.PI/180);

        float sinH =(float) Math.sin(mAngleH1*Math.PI/180);
        float cosH =(float) Math.cos(mAngleH1*Math.PI/180);

        float mStartXH = mRadius * (1 - cosH * 3 / 5);
        float mStartXV = mRadius*(1+sinV*3/5);
        float mStartYV = mRadius*(1-cosV*3/5);

        canvas.drawLine(mRadius*(1-cosH*3/5),mRadius*(1-sinH*3/5),mRadius*(1+cosH*3/5),mRadius*(1+sinH*3/5),mLinePaint);
        canvas.drawLine(mRadius*(1+sinV*3/5), mRadius*(1-cosV*3/5) ,mRadius*(1-sinV*3/5),mRadius*(1+cosV*3/5),mLinePaint);
    }


    public void startAnimationV(){

            if(mAngleV1Animator==null){
                mAngleV1Animator = ObjectAnimator.ofFloat(this, "mAngleV1", mDisAngle, mDisAngle+90);
            }
            mAngleV1Animator.setDuration(mEndTime*1000);
            mAngleV1Animator.start();
//            mAngleV1=mAngleV1+90;
//            if(mAngleH1Animator==null){
//                mAngleH1Animator = ObjectAnimator.ofFloat(this, "mAngleH1", 0, 360);
//            }
//            mAngleH1Animator.setDuration(mEndTime*1000);
//            mAngleH1Animator.start();
            mDisAngle = mDisAngle+90;

//            mAngleV1Animator = ObjectAnimator.ofFloat(this, "mAngleV1", 90, 0);
//            mAngleV1Animator.setDuration(mEndTime*1000);
//            mAngleV1Animator.start();
        invalidate();
//

    }
    public void setMAngleV1(float mAngleV){
        this.mAngleV1 = mAngleV;
        invalidate();
    }
    public void setMAngleH1(float mAngleH){
        this.mAngleH1 = mAngleH;
        invalidate();
    }


}
