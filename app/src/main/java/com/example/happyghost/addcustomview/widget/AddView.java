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
    private OnAddviewClickListener mListener;
    private boolean mIsGradient;

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
        //边框颜色
        mCircleColor = typedArray.getColor(R.styleable.AddCustomView_av_criclecolor, Color.parseColor("#FF4081"));
        //线条颜色
        mLineColor = typedArray.getColor(R.styleable.AddCustomView_av_lineColor, Color.BLUE);
        //边框粗细
        mCircleSize = typedArray.getDimension(R.styleable.AddCustomView_av_arc_size, 1);
        //线条粗细
        mLineSize = typedArray.getDimension(R.styleable.AddCustomView_av_line_size, 1);
        //中间背景颜色
        mBackGroundColor = typedArray.getColor(R.styleable.AddCustomView_av_background, Color.WHITE);
        //动画结束时间
        mEndTime = typedArray.getInteger(R.styleable.AddCustomView_av_animation_time,2);
        //横线是否移动
        mHmove = typedArray.getBoolean(R.styleable.AddCustomView_av_moveH, false);
        //是否设置边框颜色渐变（2种之间）
        mIsGradient = typedArray.getBoolean(R.styleable.AddCustomView_av_isGradient, true);
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
                if(mAngleV1Animator!=null&&mAngleV1Animator.isRunning()){
                    return;
                }else {
                    startAnimationV();
                    isAdd=!isAdd;
                    if(mListener!=null){
                        mListener.onAddViewClick();
                    }
                }
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

    /**
     * 测量控件宽高
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
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

    /**
     * 获取控件宽高及半径
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
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
        if(mIsGradient){
            LinearGradient linearGradient = new LinearGradient(0,0,mRadius*3.16f,mRadius*3.16f, getFristGradientColor(null), getSecondGradientColor(null), Shader.TileMode.MIRROR);
            mCriclePaint.setShader(linearGradient);
        }
        canvas.drawCircle(mWidth/2,mHeight/2,mRadius-mCircleSize/2,mCriclePaint);
    }

    /**
     * 就拿sin30°为列：Math.sin(30*Math.PI/180)，思路为PI相当于π，而此时的PI在角度值里相当于180°，
     * 所以Math.PI/180得到的结果就是1°，然后再乘以30就得到相应的30°。
     * 而如果是想用反正弦函数来求相应的对数的话就应该写成：Math.asin(0.5)*(180/Math.PI)，
     * 此时的PI相当于圆周率的值，所以180/Math.PI得到的结果就是一弧度的值，然后再乘以0.5就得到相应的弧度。
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        //这里必须 数值*Math.PI/180
        float sinV =(float) Math.sin(mAngleV1*Math.PI/180);
        float cosV =(float) Math.cos(mAngleV1*Math.PI/180);

        float sinH =(float) Math.sin(mAngleH1*Math.PI/180);
        float cosH =(float) Math.cos(mAngleH1*Math.PI/180);

        canvas.drawLine(mRadius*(1-cosH*3/5),mRadius*(1-sinH*3/5),mRadius*(1+cosH*3/5),mRadius*(1+sinH*3/5),mLinePaint);
        canvas.drawLine(mRadius*(1+sinV*3/5), mRadius*(1-cosV*3/5) ,mRadius*(1-sinV*3/5),mRadius*(1+cosV*3/5),mLinePaint);
    }


    public void startAnimationV(){
        mAngleV1Animator = ObjectAnimator.ofFloat(this, "mAngleV1", mDisAngle, mDisAngle+90);
        mAngleV1Animator.setDuration(mEndTime*1000);
        mAngleV1Animator.start();
        if(mAngleH1Animator==null&&mHmove){
            mAngleH1Animator = ObjectAnimator.ofFloat(this, "mAngleH1", 0, 360);
        }
        mAngleH1Animator.setDuration(mEndTime*1000);
        mAngleH1Animator.start();
        mDisAngle = mDisAngle+90;
    }
    public void setMAngleV1(float mAngleV){
        this.mAngleV1 = mAngleV;
        invalidate();
    }
    public void setMAngleH1(float mAngleH){
        this.mAngleH1 = mAngleH;
        invalidate();
    }
    public interface OnAddviewClickListener{
        void onAddViewClick();
    }
    public void setOnAddviewClickListener(OnAddviewClickListener listener){
        this.mListener = listener;
    }

    /**
     * 边框渐变颜色
     * @param color
     * @return
     */
    public int getSecondGradientColor(String color) {
        if (color==null){
            return Color.BLUE;
        }
        return Color.parseColor(color);
    }

    public int getFristGradientColor(String color) {
        if(color==null){
            return Color.RED;
        }
        return Color.parseColor(color);
    }
    /**
     * 获取当前控件状态（+ 或 -）
     */
     public boolean isAdd(){
         return isAdd;
     }

    public int getmCircleColor() {
        return mCircleColor;
    }

    public int getmLineColor() {
        return mLineColor;
    }

    public float getmCircleSize() {
        return mCircleSize;
    }

    public float getmLineSize() {
        return mLineSize;
    }

    public int getmBackGroundColor() {
        return mBackGroundColor;
    }

    public int getmEndTime() {
        return mEndTime;
    }

    public boolean ismHmove() {
        return mHmove;
    }
    public void setmCircleColor(int mCircleColor) {
        this.mCircleColor = mCircleColor;
    }

    public void setmLineColor(int mLineColor) {
        this.mLineColor = mLineColor;
    }

    public void setmCircleSize(float mCircleSize) {
        this.mCircleSize = mCircleSize;
    }

    public void setmLineSize(float mLineSize) {
        this.mLineSize = mLineSize;
    }

    public void setmBackGroundColor(int mBackGroundColor) {
        this.mBackGroundColor = mBackGroundColor;
    }

    public void setmEndTime(int mEndTime) {
        this.mEndTime = mEndTime;
    }

    public void setmHmove(boolean mHmove) {
        this.mHmove = mHmove;
    }
    public boolean ismIsGradient() {
        return mIsGradient;
    }

    public void setmIsGradient(boolean mIsGradient) {
        this.mIsGradient = mIsGradient;
    }


}
