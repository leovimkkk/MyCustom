package com.xwjr.utilcode.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.xwjr.utilcode.R;
import com.xwjr.utilcode.utils.ImageUtils;
import com.xwjr.utilcode.utils.LogUtils;
import com.xwjr.utilcode.utils.ViewUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义折线图
 */

public class MyLineChartView2 extends View {

    private Context context;
    private int width;
    private int height;

    private List<String> XAxisList;

    public void setXAxisList(List<String> XAxisList) {
        this.XAxisList = XAxisList;
    }

    private List<Float> dataList;

    public void setDataList(List<Float> dataList) {
        this.dataList = dataList;
    }

    private List<Float> YAxisList = new ArrayList<>();
    private List<Point> pointList = new ArrayList<>();
    private float YMax = 0;
    private float YMin = 9999;
    private int currentShow = 10;
    private float totalAnimationTime = 2000;
    private float currentAnimationTime = 0;
    private float animationPercent;

    public MyLineChartView2(Context context) {
        this(context, null);
    }

    public MyLineChartView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLineChartView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
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
                            animationPercent = currentAnimationTime / totalAnimationTime;
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
        if (dataList != null && dataList.size() == 7) {
            init();
            gotoOrigin(canvas);
            getYAxisList();
            drawAxis(canvas);
            drawPoint(canvas);
            drawPointHint(canvas);
        }
    }


    private void init() {
        width = (int) getLength(750.0);
        height = (int) getLength(578.0);
    }

    /**
     * 绘制背景
     *
     * @param canvas
     */
    private void drawBackground(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap bitmap = ImageUtils.drawable2Bitmap(getResources().getDrawable(R.mipmap.chart_bac));
        Matrix matrix = new Matrix();
        // 缩放原图
        matrix.postScale(width / (float) bitmap.getWidth(), height / (float) bitmap.getHeight());
        //bmp.getWidth(), bmp.getHeight()分别表示缩放后的位图宽高
        Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                matrix, true);


        canvas.drawBitmap(dstbmp, 0, 0, paint);
    }

    /**
     * 移动到坐标原点
     *
     * @param canvas
     */
    private void gotoOrigin(Canvas canvas) {
        canvas.translate(getLength(124.0), getLength(490.0));
    }


    //通过数据计算Y坐标
    private void getYAxisList() {
        YMax = 0;
        YMin = 9999;
        for (int i = 0; i < dataList.size(); i++) {
            if (YMax < dataList.get(i)) {
                YMax = dataList.get(i);
            }
            if (YMin > dataList.get(i)) {
                YMin = dataList.get(i);
            }
        }
        YMax = Float.valueOf(String.valueOf(YMax).substring(0, 3)) + 0.1f;
        YMin = Float.valueOf(String.valueOf(YMin).substring(0, 3));

        DecimalFormat fnum = new DecimalFormat("##0.00");
        YAxisList.clear();
        YAxisList.add(YMin);
        YAxisList.add(Float.valueOf(fnum.format((YMax - YMin) / 4 + YMin)));
        YAxisList.add(Float.valueOf(fnum.format((YMax - YMin) * 2 / 4 + YMin)));
        YAxisList.add(Float.valueOf(fnum.format((YMax - YMin) * 3 / 4 + YMin)));
        YAxisList.add(YMax);
    }

    //绘制坐标轴
    private void drawAxis(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(getLength(24));
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        float textY = (0 - top / 2 - bottom / 2);

        //绘制横轴
        paint.setColor(Color.parseColor("#bdbdbd"));
        canvas.drawLine(0, 0, getLength(626.0), 0, paint);
        //绘制纵轴
        canvas.drawLine(0, 0, 0, getLength(-406.0), paint);
        //绘制纵坐标三角形
        Path path = new Path();
        path.moveTo(getLength(-4), getLength(-406.0));//左面的点
        path.lineTo(getLength(4), getLength(-406.0));//右面的点
        path.lineTo(0, getLength(-414.0));//顶点
        path.close();
        canvas.drawPath(path, paint);

        //绘制横坐标
        for (int i = 0; i < 7; i++) {
            paint.setColor(Color.parseColor("#999999"));
            canvas.drawText(XAxisList.get(i), getLength(43.0) + getLength(90.0) * i, getLength(26.0) + textY, paint);
        }

        //绘制纵坐标
        for (int i = 0; i < 5; i++) {
            paint.setColor(Color.parseColor("#999999"));
            DecimalFormat decimalFormat = new DecimalFormat(".00");
            canvas.drawText(decimalFormat.format(YAxisList.get(i)), -getLength(38.0), -getLength(87.0) * i + textY, paint);
            //绘制虚线
            if (i != 0) {
                paint.setColor(Color.parseColor("#e9e8e8"));
                canvas.drawLine(0, -getLength(87.0) * i, getLength(626.0), -getLength(87.0) * i, paint);
            }
        }
        //绘制收益率
        paint.setColor(Color.parseColor("#464646"));
        canvas.drawText("收益率", getLength(-60), getLength(-422) + textY, paint);
        canvas.drawText("(%)", getLength(-60), getLength(-390) + textY, paint);

    }


    //绘制坐标点 和连接线
    private void drawPoint(Canvas canvas) {
        for (int i = 0; i < 7; i++) {
            Point point = new Point(
                    (int) (getLength(43.0) + getLength(90.0) * i),
                    (int) (((dataList.get(i) - YMin) / (YMax - YMin)) * getLength(348))
            );
            pointList.add(point);
        }

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        for (int i = 0; i < 7; i++) {
            if (i != 0) {
                paint.setColor(Color.parseColor("#ff6f28"));
                paint.setStrokeWidth(getLength(4));
                canvas.drawLine(pointList.get(i - 1).x, -pointList.get(i - 1).y * animationPercent, pointList.get(i).x, -pointList.get(i).y * animationPercent, paint);
            }
        }
        for (int i = 0; i < 7; i++) {
            paint.setColor(Color.parseColor("#ffffff"));
            canvas.drawCircle(pointList.get(i).x, -pointList.get(i).y * animationPercent, getLength(10), paint);
            paint.setColor(Color.parseColor("#ff6f28"));
            canvas.drawCircle(pointList.get(i).x, -pointList.get(i).y * animationPercent, getLength(8), paint);
        }

    }

    /**
     * 绘制point上方的提示数字
     *
     * @param canvas
     */
    private void drawPointHint(Canvas canvas) {
        for (int i = 0; i < 7; i++) {
            if (i == currentShow) {
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                Bitmap bitmap = ImageUtils.drawable2Bitmap(getResources().getDrawable(R.mipmap.hint_blue_bg));
                Matrix matrix = new Matrix();
                // 缩放原图
                matrix.postScale(getLength(69.0) / (float) bitmap.getWidth(), getLength(45.0) / (float) bitmap.getHeight());
                //bmp.getWidth(), bmp.getHeight()分别表示缩放后的位图宽高
                Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                        matrix, true);
                canvas.drawBitmap(dstbmp, pointList.get(i).x - getLength(35), -pointList.get(i).y - getLength(55), paint);

                paint.setColor(Color.parseColor("#ffffff"));
                paint.setTextSize(getLength(24));
                paint.setTextAlign(Paint.Align.CENTER);
                Paint.FontMetrics fontMetrics = paint.getFontMetrics();
                float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
                float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
                float textY = (0 - top / 2 - bottom / 2);
                canvas.drawText(String.valueOf(dataList.get(i)), pointList.get(i).x, -pointList.get(i).y - getLength(38) + textY, paint);
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (dataList != null && dataList.size() == 7) {
                    for (int i = 0; i < 7; i++) {
                        if (isInDataPoint(event.getX(), event.getY(), pointList.get(i))) {
                            currentShow = i;
                            postInvalidate();
                            break;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (dataList != null && dataList.size() == 7) {
                    for (int i = 0; i < 7; i++) {
                        if (isInDataPoint(event.getX(), event.getY(), pointList.get(i))) {
                            currentShow = i;
                            postInvalidate();
                            break;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    /**
     * 判断点击位置是否在数据点内
     */
    private boolean isInDataPoint(float x, float y, Point point) {
//        return (dataPointRadius + 20) * (dataPointRadius + 20) >= (point.x - x) * (point.x - x) + (point.y - y) * (point.y - y);
        return ((getLength(20)) > Math.abs(point.x - (x - getLength(124))));
    }


    /**
     * 获取相对屏幕尺寸
     */
    private float getLength(double lenght) {
        return (float) (ViewUtil.getWindowWidth(context) * ViewUtil.getWeight(lenght));
    }
}


