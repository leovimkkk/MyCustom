package com.xwjr.utilcode.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

import com.xwjr.utilcode.R;


/**
 * Created by Administrator on 2017/5/26.
 */

public class MyProgressCircle extends android.support.v7.widget.AppCompatTextView {
    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 50;

    private Context context;

    private int max;
    private int progress;
    private int width;
    private int height;
    private Paint defaultPaint;
    private Paint borderPaint;
    private Paint whitePaint;
    Paint paint2 = new Paint();
    private int currentAnimationTime = 0;
    private int totalAnimationTime = 5000;
    private int stroke =2;

    public MyProgressCircle(Context context) {
        super(context);
        init(context);
    }


    public MyProgressCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyProgressCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }


    private void init(Context context) {
        defaultPaint = new Paint();
        defaultPaint.setAntiAlias(true);
        defaultPaint.setColor(context.getResources().getColor(R.color.circle_content));

        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(context.getResources().getColor(R.color.circle_border));
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(stroke);
        whitePaint = new Paint();
        whitePaint.setAntiAlias(true);
        whitePaint.setColor(context.getResources().getColor(R.color.white));


        progress = 90;
        max = 100;
        setTotalAnimationTime();

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (currentAnimationTime < totalAnimationTime) {
                    try {
                        sleep(10);
                        currentAnimationTime += 10;
                        postInvalidate();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = canvas.getWidth();
        height = canvas.getHeight();
        drawSector(canvas);
        drawBorder(canvas);


    }


    /**
     * 绘制扇形
     */
    private void drawSector(Canvas canvas) {
        RectF rectF = new RectF(2, 2, width - 2, height - 2);// 设置个新的长方形，扫描测量
        canvas.drawArc(rectF, -90, (360 * progress / max)*currentAnimationTime/totalAnimationTime, true, defaultPaint);

//        (Math.sin((360 * progress / max))*width-2)+height/2;

    }

    /**
     * 背景颜色
     */
    private void drawBorder(final Canvas canvas) {
        canvas.drawCircle(width / 2, height / 2, width / 2-stroke, borderPaint);

        RectF rectF = new RectF(2, 2, width - 2, height - 2);// 设置个新的长方形，扫描测量
        canvas.drawArc(rectF, -90, (360 * progress / max)*currentAnimationTime/totalAnimationTime, true, borderPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.UNSPECIFIED
                || widthMode == MeasureSpec.AT_MOST) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(DEFAULT_WIDTH, MeasureSpec.EXACTLY);
        }
        if (heightMode == MeasureSpec.UNSPECIFIED
                || heightMode == MeasureSpec.AT_MOST) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(DEFAULT_HEIGHT, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
            totalAnimationTime = 2000;
        }
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setCurrentAnimationTime(int currentAnimationTime) {
        this.currentAnimationTime = currentAnimationTime;
    }

    public void setTotalAnimationTime(int totalAnimationTime) {
        this.totalAnimationTime = totalAnimationTime;
    }

}
