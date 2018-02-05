package com.xwjr.utilcode.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

import com.xwjr.utilcode.R;
import com.xwjr.utilcode.utils.SizeUtils;


/**
 * Created by Administrator on 2017/5/26.
 */

public class MyProgressBarHorizontalWithAnimation extends android.support.v7.widget.AppCompatTextView {
    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 50;

    private Context context;

    private Bitmap bgBitmap;//背景图
    private Bitmap progressBitmap;//进度条图
    private int max;//最大值
    private int progress;//当前进度
    private int width;//view宽
    private int height;//view高
    private Paint defaultPaint;//默认画笔
    private Paint percentTextPaint;//百分比画笔
    private int currentAnimationTime = 0;//当前动画时间
    private int totalAnimationTime = 2000;//总共动画时间
    private int paddingRight;//文字距离右侧值
    private int paddingLeft;//文字距离右侧值
    private int progressHeight;//进度条高度


    public MyProgressBarHorizontalWithAnimation(Context context) {
        super(context);
        init(context);
    }


    public MyProgressBarHorizontalWithAnimation(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyProgressBarHorizontalWithAnimation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }


    private void init(Context context) {
        this.context = context;
        defaultPaint = new Paint();
        percentTextPaint = new Paint();
        percentTextPaint.setAntiAlias(true);
        percentTextPaint.setTextSize(SizeUtils.sp2px(12));
        percentTextPaint.setColor(Color.parseColor("#ffeee0"));
        progress = 0;
        max = 100;
        paddingRight = 160;
        paddingLeft = 20;
        progressHeight = 8;
        setTotalAnimationTime();

        bgBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bgprogress);
        progressBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.select_progress);

        startPaintThread();

    }

    private void startPaintThread() {
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
        drawBackground(canvas);
        drawProgressView(canvas);
        drawPercentText(canvas);
    }


    /**
     * 进度百分比
     */
    private void drawPercentText(Canvas canvas) {
        int currentX;
        currentX = (width * currentAnimationTime * progress) / (totalAnimationTime * max);
        if (progress > 80) {
            currentX = (width * currentAnimationTime * 80) / (totalAnimationTime * max);
        }

        if (currentX < paddingLeft) {
            currentX = paddingLeft;
        }

        canvas.drawText(getResources().getString(R.string.has_invest) + (progress * currentAnimationTime) / totalAnimationTime + "%",
                currentX,
                height - progressHeight - 10,
                percentTextPaint);
    }

    /**
     * 进度条颜色
     */
    private void drawProgressView(Canvas canvas) {
        RectF selectRectF = new RectF(0, height - progressHeight, (width * progress * currentAnimationTime) / (max * totalAnimationTime), height);
        canvas.drawBitmap(progressBitmap, null, selectRectF, defaultPaint);
    }

    /**
     * 背景颜色
     */
    private void drawBackground(final Canvas canvas) {
        RectF bgRectF = new RectF(0, height - progressHeight, width, height);
        canvas.drawBitmap(bgBitmap, null, bgRectF, defaultPaint);
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
            totalAnimationTime = 1000;
        } else if (progress > 60) {
            totalAnimationTime = 1000;
        } else if (progress > 40) {
            totalAnimationTime = 1000;
        } else {
            totalAnimationTime = 1000;
        }
    }


    public void setBgBitmap(Bitmap bgBitmap) {
        this.bgBitmap = bgBitmap;
    }

    public void setProgressBitmap(Bitmap progressBitmap) {
        this.progressBitmap = progressBitmap;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setPercentTextSize(int size) {
        percentTextPaint.setTextSize(SizeUtils.sp2px(size));
    }

    public void setPercentTextColor(int res) {
        percentTextPaint.setColor(getResources().getColor(res));
    }

    public void setPercentTextPaint(Paint percentTextPaint) {
        this.percentTextPaint = percentTextPaint;
    }

    public void setCurrentAnimationTime(int currentAnimationTime) {
        this.currentAnimationTime = currentAnimationTime;
    }

    public void setTotalAnimationTime(int totalAnimationTime) {
        this.totalAnimationTime = totalAnimationTime;
    }


    @Override
    public int getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public int getProgressHeight() {
        return progressHeight;
    }

    public void setProgressHeight(int progressHeight) {
        this.progressHeight = progressHeight;
    }
}
