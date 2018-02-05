package com.xwjr.utilcode.customview.GestureLockView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.View;

import com.xwjr.utilcode.R;
import com.xwjr.utilcode.utils.ImageUtils;

public class GestureLockView extends View {
    private static final String TAG = "GestureLockView";

    /**
     * GestureLockView的三种状态
     */
    enum Mode {
        STATUS_NO_FINGER, STATUS_FINGER_ON, STATUS_FINGER_UP, STATUS_NORMAL, STATUS_PRESS, STATUS_ERROR
    }

    /**
     * GestureLockView的当前状态
     */
    private Mode mCurrentStatus = Mode.STATUS_NO_FINGER;

    /**
     * 宽度
     */
    private int mWidth;
    /**
     * 高度
     */
    private int mHeight;
    /**
     * 外圆半径
     */
    private int mRadius;
    /**
     * 画笔的宽度
     */
    private int mStrokeWidth = 2;

    /**
     * 圆心坐标
     */
    private int mCenterX;
    private int mCenterY;
    private Paint mPaint;

    /**
     * 箭头（小三角最长边的一半长度 = mArrawRate * mWidth / 2 ）
     */
    private float mArrowRate = 0.333f;
    private int mArrowDegree = -1;
    private Path mArrowPath;
    /**
     * 内圆的半径 = mInnerCircleRadiusRate * mRadus
     */
    private float mInnerCircleRadiusRate = 0.3F;

    /**
     * 四个颜色，可由用户自定义，初始化时由GestureLockViewGroup传入
     */
    private int mColorNoFingerInner;
    private int mColorNoFingerOutter;
    private int mColorFingerOn;
    private int mColorFingerUp;

    public GestureLockView(Context context, int colorNoFingerInner, int colorNoFingerOutter, int colorFingerOn, int colorFingerUp) {
        super(context);
        this.mColorNoFingerInner = colorNoFingerInner;
        this.mColorNoFingerOutter = colorNoFingerOutter;
        this.mColorFingerOn = colorFingerOn;
        this.mColorFingerUp = colorFingerUp;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArrowPath = new Path();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 取长和宽中的小值  
        mWidth = mWidth < mHeight ? mWidth : mHeight;
        mRadius = mCenterX = mCenterY = mWidth / 2;
        mRadius -= mStrokeWidth / 2;

        // 绘制三角形，初始时是个默认箭头朝上的一个等腰三角形，用户绘制结束后，根据由两个GestureLockView决定需要旋转多少度  
        float mArrowLength = mWidth / 2 * mArrowRate;
        mArrowPath.moveTo(mWidth / 2, mStrokeWidth + 2);
        mArrowPath.lineTo(mWidth / 2 - mArrowLength, mStrokeWidth + 2
                + mArrowLength);
        mArrowPath.lineTo(mWidth / 2 + mArrowLength, mStrokeWidth + 2
                + mArrowLength);
        mArrowPath.close();
        mArrowPath.setFillType(Path.FillType.WINDING);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        Matrix matrix = new Matrix();
        // 缩放原图
        matrix.postScale(0.25f, 0.25f);
        switch (mCurrentStatus) {
            case STATUS_FINGER_ON:
                Bitmap bitmap = ImageUtils.drawable2Bitmap(getResources().getDrawable(R.mipmap.gesture_press));
                Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                canvas.drawBitmap(dstbmp, mCenterX - (bitmap.getWidth() / 8), mCenterY - (bitmap.getWidth() / 8), null);
                break;
            case STATUS_FINGER_UP:
                Bitmap bitmap2 = ImageUtils.drawable2Bitmap(getResources().getDrawable(R.mipmap.gesture_error));
                Bitmap dstbmp2 = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix, true);
                canvas.drawBitmap(dstbmp2, mCenterX - (bitmap2.getWidth() / 8), mCenterY - (bitmap2.getWidth() / 8), null);
                break;

            case STATUS_NO_FINGER:
                Bitmap bitmap3 = ImageUtils.drawable2Bitmap(getResources().getDrawable(R.mipmap.gesture_no));
                Bitmap dstbmp3 = Bitmap.createBitmap(bitmap3, 0, 0, bitmap3.getWidth(), bitmap3.getHeight(), matrix, true);
                canvas.drawBitmap(dstbmp3, mCenterX - (bitmap3.getWidth() / 8), mCenterY - (bitmap3.getWidth() / 8), null);
                break;
            case STATUS_ERROR:
                Bitmap bitmap4 = ImageUtils.drawable2Bitmap(getResources().getDrawable(R.mipmap.gesture_error));
                Bitmap dstbmp4 = Bitmap.createBitmap(bitmap4, 0, 0, bitmap4.getWidth(), bitmap4.getHeight(), matrix, true);
                canvas.drawBitmap(dstbmp4, mCenterX - (bitmap4.getWidth() / 8), mCenterY - (bitmap4.getWidth() / 8), null);
                break;

        }

    }

    /**
     * 设置当前模式并重绘界面
     *
     * @param mode
     */
    public void setMode(Mode mode) {
        this.mCurrentStatus = mode;
        invalidate();
    }

    public void setArrowDegree(int degree) {
        this.mArrowDegree = degree;
    }

    public int getArrowDegree() {
        return this.mArrowDegree;
    }
}  