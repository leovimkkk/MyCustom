package com.xwjr.utilcode.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义折线图
 */

public class MyLineChartView extends View {
    private Context context;
    public String tag = "zxh";
    public Paint paint;//默认画笔
    public Paint axisPaint;//坐标轴画笔
    public Paint YaxisTextPaint;//纵坐标字体画笔
    public Paint XaxisTextPaint;//横坐标字体画笔
    public Paint gridPaint;//网格线画笔
    public Paint linePaint;//折线画笔
    public Paint fillAreaPaint;//填充区域画笔
    public Paint dataTopTextPaint;//数据点上方文字
    public Paint dataTopBgPaint;//数据点上方背景
    public Point startPoint = new Point(100, 800);//坐标原点
    public Point XEndPoint = new Point(600, 800);//X轴结束点
    public Point YEndPoint = new Point(100, 100);//Y轴结束点
    public List<Point> dataPointList = new ArrayList<>();

    public int dataPointRadius = 3;//绘制点的半径
    public int arrowLength = 10;//箭头长度
    public String[] XAxisTextList = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    public double XaxisDownMove = 30;//X轴向下移的位置写文字
    public double XaxisLeftMove = 35;//X轴向左移的位置写文字
    public double XLocationGap;//x坐标差
    public int XLabelAmount = 7;//X轴坐标数量
    public double YMax = 0;//Y最大值
    public double YMin = 999999999;//Y最小值
    public double YLabelAmount = 7;//Y轴坐标数量
    public double YLocationGap;//y坐标差
    public double YDataGap;//y坐标值得差
    public double YaxisLeftMove = 60;//Y轴向左位置偏移位置
    public double dataPointUpMove = 30;//数据点数据向上偏移位置
    public String[] dataList = {"12.4546", "13.2085", "13.54465", "14.259", "14.4545", "14.5988", "15.54865"};
    public int dataCheckId = 999999999;//被点击的数据点ID

    private double totalAnimationTime = 2000;
    private double currentAnimationTime = 0;

    private int marginBottom = 50;
    private int marginLeft = 100;

    public MyLineChartView(Context context) {
        this(context, null);
    }

    public MyLineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setDefaultPaint();


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

        initNeedData(canvas);
        drawGrid(canvas);
        drawAxis(canvas);
        drawXaxisText(canvas);
        drawYaxisText(canvas);
        getDataPoint();
        drawDataPoint(canvas);
        drawDataLine(canvas);
        drawDataTopText(canvas);
        drawDataArea(canvas);
    }


    /**
     * 计算初始数据
     * x轴数量
     * x轴位置间隔
     * y轴数据间隔
     * y轴位置间隔
     * y最大值
     * y最小值
     */
    private void initNeedData(Canvas canvas) {
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        YaxisLeftMove = canvasWidth * 0.1;
        XaxisLeftMove = canvasWidth * 0.05;
        XaxisDownMove = canvasHeight * 0.05;
        marginBottom = (int) (canvasHeight * 0.1);
        marginLeft = (int) (canvasWidth * 0.15);
        startPoint = new Point(marginLeft, canvasHeight - marginBottom);
        XEndPoint = new Point(canvasWidth - marginBottom, canvasHeight - marginBottom);
        YEndPoint = new Point(marginLeft, marginBottom);
        YMax = 0;//Y最大值
        YMin = 999999999;//Y最小值
        for (int i = 0; i < dataList.length; i++) {
            if (YMax < Double.valueOf(dataList[i])) {
                YMax = Double.valueOf(dataList[i]);
            }
            if (YMin > Double.valueOf(dataList[i])) {
                YMin = Double.valueOf(dataList[i]);
            }
        }
        XLabelAmount = XAxisTextList.length;
        XLocationGap = (XEndPoint.x - startPoint.x - marginBottom) / (XLabelAmount - 1);
        YDataGap = (YMax - YMin) / YLabelAmount;
        YLocationGap = (startPoint.y - YEndPoint.y) / YLabelAmount;

    }

    /**
     * 获取点的数据集合
     */
    private void getDataPoint() {
        dataPointList.clear();
        for (int i = 0; i < dataList.length; i++) {
            Point point = new Point((int) (startPoint.x + XLocationGap * i), (int) getYaxisByData(Double.parseDouble(dataList[i])));
            dataPointList.add(point);
        }
    }

    /**
     * 绘制数据点
     *
     * @param canvas
     */
    private void drawDataPoint(Canvas canvas) {
        //画原点
        for (int i = 0; i < dataList.length; i++) {
            canvas.drawCircle(dataPointList.get(i).x, (float) ((canvas.getHeight() - marginBottom) - (((canvas.getHeight() - marginBottom) - dataPointList.get(i).y) * (currentAnimationTime / totalAnimationTime)))
                    , dp2px(dataPointRadius), linePaint);
        }
    }

    /**
     * 绘制数据线
     */
    private void drawDataLine(Canvas canvas) {
        //画线
        for (int i = 1; i < dataList.length; i++) {
            canvas.drawLine(dataPointList.get(i - 1).x,
                    (float) ((canvas.getHeight() - marginBottom) - (((canvas.getHeight() - marginBottom) - dataPointList.get(i - 1).y) * (currentAnimationTime / totalAnimationTime))),
                    dataPointList.get(i).x,
                    (float) ((canvas.getHeight() - marginBottom) - (((canvas.getHeight() - marginBottom) - dataPointList.get(i).y) * (currentAnimationTime / totalAnimationTime))),
                    linePaint);
        }
    }

    /**
     * 绘制点上方数据显示View
     *
     * @param canvas
     */
    private void drawDataTopText(Canvas canvas) {
        double textWidth;
        Paint.FontMetrics metrics = paint.getFontMetrics();
//        画点上面的数据
        for (int i = 0; i < dataList.length; i++) {
            if (i == dataCheckId) {
                textWidth = dataTopTextPaint.measureText(dataList[i]);
                RectF re = new RectF(dataPointList.get(i).x - 10,
                        (float) ((float) ((canvas.getHeight() - marginBottom) - (((canvas.getHeight() - marginBottom) - dataPointList.get(i).y) * (currentAnimationTime / totalAnimationTime))) - dataPointUpMove * 1.5 - metrics.bottom * 1.5),
                        (float) (dataPointList.get(i).x + textWidth + 10),
                        (float) ((float) ((canvas.getHeight() - marginBottom) - (((canvas.getHeight() - marginBottom) - dataPointList.get(i).y) * (currentAnimationTime / totalAnimationTime))) - dataPointUpMove * 0.5 - metrics.bottom * 1.5));// 设置个新的长方形
                canvas.drawRoundRect(re, 5, 5, dataTopBgPaint);//第二个参数是
                canvas.drawText(dataList[i],
                        dataPointList.get(i).x,
                        (float) ((float) ((canvas.getHeight() - marginBottom) - (((canvas.getHeight() - marginBottom) - dataPointList.get(i).y) * (currentAnimationTime / totalAnimationTime))) - dataPointUpMove),
                        dataTopTextPaint);
            }
        }
    }

    /**
     * 绘制数据覆盖区域
     *
     * @param canvas
     */

    private void drawDataArea(Canvas canvas) {
        //画填充区域
        Path path = new Path();
        path.moveTo(startPoint.x, startPoint.y);
        for (int i = 0; i < dataList.length; i++) {
            path.lineTo(dataPointList.get(i).x, (float) ((canvas.getHeight() - marginBottom) - (((canvas.getHeight() - marginBottom) - dataPointList.get(i).y) * (currentAnimationTime / totalAnimationTime))));
            if ((i + 1) == dataList.length) {
                path.lineTo(dataPointList.get(i).x, startPoint.y);
            }
        }
        path.close();
        canvas.drawPath(path, fillAreaPaint);
    }


    /**
     * 绘制网格
     */
    private void drawGrid(Canvas canvas) {
        for (int i = 1; i < XLabelAmount; i++) {
            canvas.drawLine((float) (startPoint.x + XLocationGap * i), startPoint.y, (float) (startPoint.x + XLocationGap * i), YEndPoint.y, gridPaint);
        }
        for (int i = 1; i < YLabelAmount; i++) {
            canvas.drawLine(startPoint.x, (float) (startPoint.y - YLocationGap * i), XEndPoint.x, (float) (startPoint.y - YLocationGap * i), gridPaint);
        }
    }

    /**
     * 画纵坐标
     *
     * @param canvas
     */
    private void drawYaxisText(Canvas canvas) {
        for (int i = 0; i < YLabelAmount; i++) {
            canvas.drawText(keep3decimal(YMin + i * YDataGap), (float) (startPoint.x - YaxisLeftMove), (float) getYaxisByData(YMin + i * YDataGap), YaxisTextPaint);
        }
    }

    /**
     * 画横坐标
     */
    private void drawXaxisText(Canvas canvas) {
        for (int i = 0; i < XLabelAmount; i++) {
            canvas.drawText(XAxisTextList[i], (float) (startPoint.x + i * XLocationGap - XaxisLeftMove), (float) (startPoint.y + XaxisDownMove), XaxisTextPaint);
        }
    }

    /**
     * 画坐标轴
     */
    private void drawAxis(Canvas canvas) {
        canvas.drawLine(startPoint.x, startPoint.y, XEndPoint.x, XEndPoint.y, axisPaint);
        canvas.drawLine(XEndPoint.x - arrowLength, XEndPoint.y - arrowLength, XEndPoint.x, XEndPoint.y, axisPaint);
        canvas.drawLine(XEndPoint.x - arrowLength, XEndPoint.y + arrowLength, XEndPoint.x, XEndPoint.y, axisPaint);
        canvas.drawLine(startPoint.x, startPoint.y, YEndPoint.x, YEndPoint.y, axisPaint);
        canvas.drawLine(YEndPoint.x - arrowLength, YEndPoint.y + arrowLength, YEndPoint.x, YEndPoint.y, axisPaint);
        canvas.drawLine(YEndPoint.x + arrowLength, YEndPoint.y + arrowLength, YEndPoint.x, YEndPoint.y, axisPaint);
    }

    /**
     * 设置画笔
     */
    private void setDefaultPaint() {
        paint = new Paint();
        paint.setColor(Color.parseColor("#000000"));
        paint.setAntiAlias(true);

        axisPaint = new Paint();
        axisPaint.setColor(Color.parseColor("#333333"));
        axisPaint.setAntiAlias(true);
        axisPaint.setStrokeWidth(2);

        XaxisTextPaint = new Paint();
        XaxisTextPaint.setColor(Color.parseColor("#333333"));
        XaxisTextPaint.setAntiAlias(true);
        XaxisTextPaint.setTextSize(sp2px(12));

        YaxisTextPaint = new Paint();
        YaxisTextPaint.setColor(Color.parseColor("#999999"));
        YaxisTextPaint.setAntiAlias(true);
        YaxisTextPaint.setTextSize(sp2px(12));

        gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#eeeeee"));
        gridPaint.setAntiAlias(true);
        gridPaint.setTextSize(sp2px(12));

        linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#ff4a00"));
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(dp2px(1));

        fillAreaPaint = new Paint();
        fillAreaPaint.setColor(Color.parseColor("#33ff4a00"));
        fillAreaPaint.setAntiAlias(true);
        fillAreaPaint.setStyle(Paint.Style.FILL);

        dataTopTextPaint = new Paint();
        dataTopTextPaint.setColor(Color.parseColor("#ffffff"));
        dataTopTextPaint.setAntiAlias(true);
        dataTopTextPaint.setTextSize(sp2px(8));

        dataTopBgPaint = new Paint();
        dataTopBgPaint.setColor(Color.parseColor("#ff4a00"));
        dataTopBgPaint.setAntiAlias(true);
        dataTopBgPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * 根据Y轴数据获取Y坐标
     *
     * @param data
     * @return
     */
    private double getYaxisByData(double data) {
        return (startPoint.y - (startPoint.y - YEndPoint.y) * ((data - YMin) / (YMax - YMin)));
    }

    /**
     * 保留两位小数
     */
    private String keep3decimal(double data) {
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        return decimalFormat.format(data);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < dataList.length; i++) {
                    if (isInDataPoint(event.getX(), event.getY(), dataPointList.get(i))) {
                        dataCheckId = i;
                        postInvalidate();
                        break;
                    } else {
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < dataList.length; i++) {
                    if (isInDataPoint(event.getX(), event.getY(), dataPointList.get(i))) {
                        dataCheckId = i;
                        postInvalidate();
                        break;
                    } else {
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
     *
     * @param x
     * @param y
     * @param point
     * @return
     */
    private boolean isInDataPoint(float x, float y, Point point) {
//        return (dataPointRadius + 20) * (dataPointRadius + 20) >= (point.x - x) * (point.x - x) + (point.y - y) * (point.y - y);
        return ((dataPointRadius + 20) > Math.abs(point.x - x));
    }

    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    public int dp2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param pxValue px值
     * @return dp值
     */
    public int px2dp(float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param spValue sp值
     * @return px值
     */
    public int sp2px(float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转sp
     *
     * @param pxValue px值
     * @return sp值
     */
    public int px2sp(float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));  //这里面是原始的大小，需要重新计算可以修改本行
        //dosomething
    }


    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public Paint getAxisPaint() {
        return axisPaint;
    }

    public void setAxisPaint(Paint axisPaint) {
        this.axisPaint = axisPaint;
    }

    public Paint getYaxisTextPaint() {
        return YaxisTextPaint;
    }

    public void setYaxisTextPaint(Paint yaxisTextPaint) {
        YaxisTextPaint = yaxisTextPaint;
    }

    public Paint getXaxisTextPaint() {
        return XaxisTextPaint;
    }

    public void setXaxisTextPaint(Paint xaxisTextPaint) {
        XaxisTextPaint = xaxisTextPaint;
    }

    public Paint getGridPaint() {
        return gridPaint;
    }

    public void setGridPaint(Paint gridPaint) {
        this.gridPaint = gridPaint;
    }

    public Paint getLinePaint() {
        return linePaint;
    }

    public void setLinePaint(Paint linePaint) {
        this.linePaint = linePaint;
    }

    public Paint getFillAreaPaint() {
        return fillAreaPaint;
    }

    public void setFillAreaPaint(Paint fillAreaPaint) {
        this.fillAreaPaint = fillAreaPaint;
    }

    public Paint getDataTopTextPaint() {
        return dataTopTextPaint;
    }

    public void setDataTopTextPaint(Paint dataTopTextPaint) {
        this.dataTopTextPaint = dataTopTextPaint;
    }

    public Paint getDataTopBgPaint() {
        return dataTopBgPaint;
    }

    public void setDataTopBgPaint(Paint dataTopBgPaint) {
        this.dataTopBgPaint = dataTopBgPaint;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public Point getXEndPoint() {
        return XEndPoint;
    }

    public void setXEndPoint(Point XEndPoint) {
        this.XEndPoint = XEndPoint;
    }

    public Point getYEndPoint() {
        return YEndPoint;
    }

    public void setYEndPoint(Point YEndPoint) {
        this.YEndPoint = YEndPoint;
    }

    public List<Point> getDataPointList() {
        return dataPointList;
    }

    public void setDataPointList(List<Point> dataPointList) {
        this.dataPointList = dataPointList;
    }

    public int getDataPointRadius() {
        return dataPointRadius;
    }

    public void setDataPointRadius(int dataPointRadius) {
        this.dataPointRadius = dataPointRadius;
    }

    public int getArrowLength() {
        return arrowLength;
    }

    public void setArrowLength(int arrowLength) {
        this.arrowLength = arrowLength;
    }

    public String[] getXAxisTextList() {
        return XAxisTextList;
    }

    public void setXAxisTextList(String[] XAxisTextList) {
        this.XAxisTextList = XAxisTextList;
    }

    public double getXaxisDownMove() {
        return XaxisDownMove;
    }

    public void setXaxisDownMove(double xaxisDownMove) {
        XaxisDownMove = xaxisDownMove;
    }

    public double getXaxisLeftMove() {
        return XaxisLeftMove;
    }

    public void setXaxisLeftMove(double xaxisLeftMove) {
        XaxisLeftMove = xaxisLeftMove;
    }

    public double getXLocationGap() {
        return XLocationGap;
    }

    public void setXLocationGap(double XLocationGap) {
        this.XLocationGap = XLocationGap;
    }

    public int getXLabelAmount() {
        return XLabelAmount;
    }

    public void setXLabelAmount(int XLabelAmount) {
        this.XLabelAmount = XLabelAmount;
    }

    public double getYMax() {
        return YMax;
    }

    public void setYMax(double YMax) {
        this.YMax = YMax;
    }

    public double getYMin() {
        return YMin;
    }

    public void setYMin(double YMin) {
        this.YMin = YMin;
    }

    public double getYLabelAmount() {
        return YLabelAmount;
    }

    public void setYLabelAmount(double YLabelAmount) {
        this.YLabelAmount = YLabelAmount;
    }

    public double getYLocationGap() {
        return YLocationGap;
    }

    public void setYLocationGap(double YLocationGap) {
        this.YLocationGap = YLocationGap;
    }

    public double getYDataGap() {
        return YDataGap;
    }

    public void setYDataGap(double YDataGap) {
        this.YDataGap = YDataGap;
    }

    public double getYaxisLeftMove() {
        return YaxisLeftMove;
    }

    public void setYaxisLeftMove(double yaxisLeftMove) {
        YaxisLeftMove = yaxisLeftMove;
    }

    public double getDataPointUpMove() {
        return dataPointUpMove;
    }

    public void setDataPointUpMove(double dataPointUpMove) {
        this.dataPointUpMove = dataPointUpMove;
    }

    public String[] getDataList() {
        return dataList;
    }

    public void setDataList(String[] dataList) {
        this.dataList = dataList;
    }

    public int getDataCheckId() {
        return dataCheckId;
    }

    public void setDataCheckId(int dataCheckId) {
        this.dataCheckId = dataCheckId;
    }

    public double getTotalAnimationTime() {
        return totalAnimationTime;
    }

    public void setTotalAnimationTime(double totalAnimationTime) {
        this.totalAnimationTime = totalAnimationTime;
    }
}
