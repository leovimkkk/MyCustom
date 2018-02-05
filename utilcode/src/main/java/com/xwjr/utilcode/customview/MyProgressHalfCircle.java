package com.xwjr.utilcode.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.xwjr.utilcode.utils.LogUtils;
import com.xwjr.utilcode.utils.SizeUtils;
import com.xwjr.utilcode.utils.Utils;
import com.xwjr.utilcode.utils.ViewUtil;

import static java.lang.Math.PI;

/**
 * Created by Administrator on 2017/9/29.
 */

public class MyProgressHalfCircle extends View {
    private ValueAnimator progressAnimator;
    private int progress;
    float width;
    float viewWidth;
    float viewHeight;
    float height;
    float paddingLeftRight;
    float paddingTopBottom;
    float borderWidth = 15;//圆弧宽度
    private float startAngle = 180;//开始绘制的角度
    private float totalAngle = 180;//开始绘制的角度
    private float lastAngle = 0;//最后绘制的角度
    private float currentAngle = 0;
    private float textLocation = 20;

    private float currentAnimationTime = 0;
    private float totalAnimationTime = 5000;
    private int currentProgress = 0;

    private int[] colors = new int[]{
            Color.parseColor("#ffa251"),
            Color.parseColor("#ff5728"),
            Color.parseColor("#ff5728"),
            Color.parseColor("#ffa251")
    };
    private float[] positions = new float[]{0f,
            0.25f,
            0.5f,
            0.75f,
            1f
    };

    public MyProgressHalfCircle(Context context) {
        super(context);
        init(context);
    }

    public MyProgressHalfCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyProgressHalfCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        width = (float) (ViewUtil.getWindowWidth(context) * ViewUtil.getWeight(525.0));
        viewWidth = (float) (ViewUtil.getWindowWidth(context) * ViewUtil.getWeight(690.0));
        height = width / 2;
        viewHeight = height;
        paddingLeftRight = (viewWidth - width) / 2;
        paddingTopBottom = 50;
        progress = 80;
        setTotalAnimationTime();

//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                while (currentAnimationTime < totalAnimationTime) {
//                    try {
//                        sleep(1);
//                        currentAnimationTime += 1;
//                        currentAngle = (progress/100.0f)*totalAngle*(currentAnimationTime/totalAnimationTime);
//                        LogUtils.i(currentAngle);
//                        postInvalidate();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBitmapBackground(canvas);
        drawBitmapInvested(canvas);
        drawCircle(canvas);
//        drawText(canvas);
    }


    //1------绘制默认灰色的圆弧
    public void drawBitmapBackground(Canvas canvas) {

        Paint paintDefalt = new Paint();
        paintDefalt.setColor(Color.parseColor("#eceef1"));
        //设置笔刷的样式 Paint.Cap.Round ,Cap.SQUARE等分别为圆形、方形
        paintDefalt.setStrokeCap(Paint.Cap.SQUARE);
        RectF oval1 = new RectF(borderWidth / 2 + paddingLeftRight,
                borderWidth / 2,
                (viewWidth - borderWidth / 2) - paddingLeftRight,
                (((viewWidth - borderWidth / 2) - paddingLeftRight) - (borderWidth / 2 + paddingLeftRight)) + borderWidth / 2);
        /**
         * Paint.Style.FILL    :填充内部
         Paint.Style.FILL_AND_STROKE  ：填充内部和描边
         Paint.Style.STROKE  ：仅描边
         */
        paintDefalt.setStyle(Paint.Style.STROKE);//设置填充样式
        paintDefalt.setAntiAlias(true);//抗锯齿功能
        paintDefalt.setStrokeWidth(borderWidth);//设置画笔宽度
        //        该类是第二个参数是角度的开始，第三个参数是多少度
        //        canvas.drawCircle(getWidth() / 2, getHeight() / 2, r, paintCircle);//画圆,圆心在中心位置,半径为长宽小者的一半

        /**
         * drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)//画弧，
         参数一是RectF对象，一个矩形区域椭圆形的界限用于定义在形状、大小、电弧，
         参数二是起始角(度)在电弧的开始，
         参数三扫描角(度)开始顺时针测量的，参数四是如果这是真的话,包括椭圆中心的电弧,并关闭它,如果它是假这将是一个弧线,参数五是Paint对象；
         */
        //绘制默认灰色的圆弧
        canvas.translate(0, paddingTopBottom);//这时候的画布已经移动到了中心位置
        canvas.drawArc(oval1, startAngle, totalAngle, false, paintDefalt);//小弧形
        //        参数一为渐变起初点坐标x位置，参数二为y轴位置，参数三和四分辨对应渐变终点，最后参数为平铺方式，这里设置为镜像.
    }

    //2-----绘制当前进度的圆弧
    public void drawBitmapInvested(Canvas canvas) {
        Paint paintCurrent = new Paint();
        paintCurrent.setStyle(Paint.Style.STROKE);//设置填充样式
        paintCurrent.setAntiAlias(true);//抗锯齿功能
        paintCurrent.setStrokeWidth(borderWidth);//设置画笔宽度
        //设置笔刷的样式 Paint.Cap.Round ,Cap.SQUARE等分别为圆形、方形
        paintCurrent.setStrokeCap(Paint.Cap.SQUARE);
        SweepGradient sweepGradient = new SweepGradient(width / 2, height, colors, null);
        Matrix matrix = new Matrix();
        sweepGradient.setLocalMatrix(matrix);
        paintCurrent.setShader(sweepGradient);
        //当前进度
        RectF oval1 = new RectF(borderWidth / 2 + paddingLeftRight,
                borderWidth / 2,
                (viewWidth - borderWidth / 2) - paddingLeftRight,
                (((viewWidth - borderWidth / 2) - paddingLeftRight) - (borderWidth / 2 + paddingLeftRight)) + borderWidth / 2);
        canvas.drawArc(oval1, startAngle, currentAngle, false, paintCurrent);
    }

    //3.绘制圆圈
    private void drawCircle(Canvas canvas) {
        Paint paintCircle = new Paint();
        paintCircle.setStyle(Paint.Style.FILL);//设置填充样式
        paintCircle.setAntiAlias(true);//抗锯齿功能
        paintCircle.setColor(Color.parseColor("#ff3232"));
        canvas.translate(viewWidth / 2, height);//这时候的画布已经移动到了中心位置

        textLocation = 20;
        //根据角度设置上下偏移量
        if (currentAngle <= 90) {
            textLocation = (float) (textLocation * (1 - currentAngle / 90.0) + 30);
        } else {
            textLocation = (float) (textLocation * (1 - (180 - currentAngle) / 90.0) + 30);
        }

        float textX = (float) (((width - borderWidth) / 2 + textLocation) * Math.cos(2 * PI / 360 * currentAngle));
        float textY = (float) (((width - borderWidth) / 2 + textLocation) * Math.sin(2 * PI / 360 * currentAngle));


        paintCircle.setTextAlign(Paint.Align.CENTER);
        paintCircle.setTextSize(SizeUtils.sp2px(12));
        Paint.FontMetrics fontMetrics = paintCircle.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        textY = (textY - top / 2 - bottom / 2);
        canvas.drawText(String.valueOf(Math.round((currentAngle*100/180.0))+"%"), -textX, -textY, paintCircle);

        canvas.rotate(currentAngle);
        paintCircle.setColor(Color.parseColor("#ff5628"));

        //根据角度设置上下偏移量
        int offset = 0;
        if (currentAngle < 5 || currentAngle > 175) {
            offset = 5;
        }

        canvas.drawCircle(-(width - borderWidth) / 2, offset, 16, paintCircle);
        paintCircle.setColor(Color.parseColor("#ffffff"));
        canvas.drawCircle(-(width - borderWidth) / 2, offset, 8, paintCircle);
        paintCircle.setStrokeWidth(3);
        paintCircle.setColor(Color.parseColor("#ffffff"));
        canvas.rotate(-currentAngle);
    }


    /***
     * 设置当前的进度值
     *
     * @param currentCounts
     */
    public void setCurrentCount(float currentCounts) {
//        this.currentCount = currentCounts > maxCount ? maxCount : currentCounts;
        if (currentCounts > 40 && currentCounts < 60) {
            paddingTopBottom = 80;
        } else {
            paddingTopBottom = 50;
        }
        currentProgress = (int) currentCounts;
        float last_angle = totalAngle * (currentCounts / 100);//最后要到达的角度
        lastAngle = currentAngle;//保存最后绘制的位置
        setAnimation(lastAngle, last_angle, 2000);
    }

    /**
     * 为进度设置动画
     *
     * @param last
     * @param current
     */
    private void setAnimation(float last, float current, int length) {
        progressAnimator = ValueAnimator.ofFloat(last, current);
        progressAnimator.setDuration(length);
//        progressAnimator.setTarget(currentAngle);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentAngle = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        progressAnimator.start();
    }


    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(float dpValue) {
        final float scale = Utils.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param pxValue px值
     * @return dp值
     */
    public static int px2dp(float pxValue) {
        final float scale = Utils.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据进度条百分比设置动画时间
     */
    private void setTotalAnimationTime() {
        if (progress > 80) {
            totalAnimationTime = 3000;
        } else if (progress > 60) {
            totalAnimationTime = 2000;
        } else if (progress > 40) {
            totalAnimationTime = 2000;
        } else {
            totalAnimationTime = 1000;
        }
    }

}
