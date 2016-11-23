package com.liuhw.radarview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by liuhw on 2016/11/23.
 * 类似雷达扫描的view
 */

public class RadarView extends View {

    private Paint mSmallCirclePaint; // 小圆形画笔
    private Paint mMiddleCirclePaint; // 中圆形画笔
    private Paint mScanPaint; // 扇形画笔
    private Paint mCircularPaint; // 圆环画笔
    private Paint mRectPaint; // 矩形画笔
    private Paint mBigCircularPaint1; //外层圆环1画笔
    private Paint mBigCircularPaint2; //外层圆环2画笔

    private int mWidth, mHeight; //控件的宽和高
    private int mPointX, mPointY; //控件的中心点x,y
    private int mSmallCircleRadius; // 中间小圆的半径
    private int mMiddleCircleRadius; //中间稍大的圆的半径
    private int degree;
    private int circularRadius1;
    private int circularRadius2;
    private int radiusStepSize;
    private int alpha1;
    private int alpha2;

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public RadarView(Context context) {
        super(context);
        initPaint();
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        Resources resources = this.getResources();
        mSmallCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSmallCirclePaint.setColor(Color.rgb(255, 255, 255));
        mSmallCirclePaint.setStyle(Paint.Style.FILL);

        mMiddleCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMiddleCirclePaint.setColor(Color.rgb(245, 148, 142));
        mMiddleCirclePaint.setStyle(Paint.Style.FILL);

        mScanPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScanPaint.setStrokeCap(Paint.Cap.ROUND);
        SweepGradient sweepGradient1 = new SweepGradient(0, 0, resources.getColor(R.color
                .endColor1), resources.getColor(R.color.startColor1));
        mScanPaint.setShader(sweepGradient1);

        mCircularPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircularPaint.setStyle(Paint.Style.STROKE);
        mCircularPaint.setStrokeWidth(4);
        SweepGradient sweepGradient2 = new SweepGradient(0, 0, resources.getColor(R.color
                .endColor2), resources.getColor(R.color.startColor2));
        mCircularPaint.setShader(sweepGradient2);

        mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPaint.setColor(Color.rgb(255, 255, 255));

        mBigCircularPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBigCircularPaint1.setColor(Color.rgb(255, 255, 255));
        mBigCircularPaint1.setAlpha(90);
        mBigCircularPaint1.setStyle(Paint.Style.STROKE);
        mBigCircularPaint1.setStrokeWidth(20);

        mBigCircularPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBigCircularPaint2.setColor(Color.rgb(255, 255, 255));
        mBigCircularPaint2.setAlpha(60);
        mBigCircularPaint2.setStyle(Paint.Style.STROKE);
        mBigCircularPaint2.setStrokeWidth(10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mPointX, mPointY);
        canvas.rotate(degree);
        canvas.drawCircle(0, 0, mMiddleCircleRadius, mMiddleCirclePaint);
        canvas.drawCircle(0, 0, mSmallCircleRadius, mSmallCirclePaint);
        canvas.drawRect(0, -2, mWidth / 3, 2, mRectPaint);
        canvas.drawCircle(0, 0, mWidth / 3, mScanPaint);
        canvas.drawCircle(0, 0, mWidth / 3, mCircularPaint);
        mBigCircularPaint1.setAlpha(alpha1);
        mBigCircularPaint2.setAlpha(alpha2);
        canvas.drawCircle(0, 0, circularRadius1, mBigCircularPaint1);
        canvas.drawCircle(0, 0, circularRadius2, mBigCircularPaint2);
        this.postInvalidateDelayed(16);
        degree += 12;
        alpha1 -= 2;
        alpha2 -= 2;
        circularRadius1 += radiusStepSize;
        circularRadius2 += radiusStepSize;
        if (degree == 360) {
            degree = 0;
            alpha1 = 90;
            alpha2 = 60;
            circularRadius1 = mWidth / 3;
            circularRadius2 = mWidth / 2;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mWidth == 0 || mHeight == 0) {
            final int minimumWidth = getSuggestedMinimumWidth();
            final int minimumHeight = getSuggestedMinimumHeight();
            mWidth = resolveMeasured(widthMeasureSpec, minimumWidth);
            mHeight = resolveMeasured(heightMeasureSpec, minimumHeight);
            mPointX = mWidth / 2;
            mPointY = mHeight / 2;
            mMiddleCircleRadius = mWidth / 20;
            mSmallCircleRadius = mMiddleCircleRadius / 3;
            radiusStepSize = mWidth / 6 / 30;
            circularRadius1 = mWidth / 3;
            circularRadius2 = mWidth / 2;
        }
    }

    private int resolveMeasured(int measureSpec, int desired) {
        int result;
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.UNSPECIFIED:
                result = desired;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(specSize, desired);
                break;
            case MeasureSpec.EXACTLY:
            default:
                result = specSize;
        }
        return result;
    }
}
