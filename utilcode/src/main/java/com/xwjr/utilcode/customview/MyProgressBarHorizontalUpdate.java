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
import com.xwjr.utilcode.utils.ConvertUtils;
import com.xwjr.utilcode.utils.LogUtils;
import com.xwjr.utilcode.utils.SizeUtils;


/**
 * Created by Administrator on 2017/5/26.
 */

public class MyProgressBarHorizontalUpdate extends android.support.v7.widget.AppCompatTextView {
    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 50;

    private Context context;

    private Bitmap bgBitmap;//背景图

    public Bitmap getProgressBitmap() {
        return progressBitmap;
    }

    public Bitmap getBgBitmap() {
        return bgBitmap;
    }

    private Bitmap progressBitmap;//进度条图
    private Bitmap progressBitmapHead;//进度条头图
    private int max;//最大值
    private int progress;//当前进度
    private int width;//view宽
    private int height;//view高
    private Paint defaultPaint;//默认画笔
    private Paint percentTextPaint;//百分比画笔
    private int currentAnimationTime = 0;//当前动画时间
    private int totalAnimationTime = 5000;//总共动画时间
    private int paddingRight;//文字距离右侧值


    public MyProgressBarHorizontalUpdate(Context context) {
        super(context);
        init(context);
    }


    public MyProgressBarHorizontalUpdate(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyProgressBarHorizontalUpdate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }


    private void init(Context context) {
        defaultPaint = new Paint();
        defaultPaint.setAntiAlias(true);
        percentTextPaint = new Paint();
        percentTextPaint.setAntiAlias(true);
        percentTextPaint.setTextSize(ConvertUtils.sp2px(12));
        percentTextPaint.setColor(Color.parseColor("#333333"));
        progress = 0;
        max = 100;
        setTotalAnimationTime();

        bgBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.update_progress_bg);
        progressBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.update_progress_select);
        progressBitmapHead = BitmapFactory.decodeResource(context.getResources(), R.drawable.update_progress_circle);


        currentAnimationTime = totalAnimationTime;
//            new Thread() {
//                @Override
//                public void run() {
//                    super.run();
//                    while (currentAnimationTime < totalAnimationTime) {
//                        try {
//                            sleep(10);
//                            currentAnimationTime += 10;
//                            postInvalidate();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }.start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = canvas.getWidth();
        height = canvas.getHeight();
        drawBackground(canvas);
        drawProgressView(canvas);
//        drawPercentText(canvas);
    }


    /**
     * 进度百分比
     */
    private void drawPercentText(Canvas canvas) {
        int currentX;
        currentX = (width * currentAnimationTime * progress) / (totalAnimationTime * max);
        if (currentX > width - 80) {
            currentX = width - 80;
        }
        canvas.drawText((progress * currentAnimationTime) / totalAnimationTime + "%",
                currentX,
                height - 20,
                percentTextPaint);
    }

    /**
     * 进度条颜色
     */
    private void drawProgressView(Canvas canvas) {
        RectF selectRectF = new RectF(0, height - SizeUtils.dp2px(8), (width * progress * currentAnimationTime) / (max * totalAnimationTime), height);
        canvas.drawBitmap(progressBitmap, null, selectRectF, defaultPaint);

        RectF selectRectF2 = new RectF((width * progress * currentAnimationTime) / (max * totalAnimationTime)-SizeUtils.dp2px(8), height - SizeUtils.dp2px(8),
                (width * progress * currentAnimationTime) / (max * totalAnimationTime), height);
        canvas.drawBitmap(progressBitmapHead, null, selectRectF2, defaultPaint);

    }

    /**
     * 背景颜色
     */
    private void drawBackground(final Canvas canvas) {
        RectF bgRectF = new RectF(0, height - SizeUtils.dp2px(8), width, height);
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
            totalAnimationTime = 5000;
        } else if (progress > 60) {
            totalAnimationTime = 4000;
        } else if (progress > 40) {
            totalAnimationTime = 3000;
        } else {
            totalAnimationTime = 2000;
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
        postInvalidate();
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
}
