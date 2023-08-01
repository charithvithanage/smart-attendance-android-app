package lnbti.charithgtp01.smartattendanceuserapp.customviews;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import lnbti.charithgtp01.smartattendanceuserapp.R;
import me.dm7.barcodescanner.core.DisplayUtils;
import me.dm7.barcodescanner.core.IViewFinder;
public class CustomZXingScannerView extends View implements IViewFinder {
    private static final String TAG = "ViewFinderView";

    private Rect mFramingRect;

    private static final float PORTRAIT_WIDTH_RATIO = 6f / 8;
    private static final float PORTRAIT_WIDTH_HEIGHT_RATIO = 1.0f;

    private static final float LANDSCAPE_HEIGHT_RATIO = 5f / 8;
    private static final float LANDSCAPE_WIDTH_HEIGHT_RATIO = 1.0f;
    private static final int MIN_DIMENSION_DIFF = 100;

    private static final float SQUARE_DIMENSION_RATIO = 5f / 8;

    private final int mDefaultLaserColor = getResources().getColor(R.color.viewfinder_laser);
    private final int mDefaultMaskColor = getResources().getColor(R.color.maskColor);
    private final int mDefaultBorderColor = getResources().getColor(R.color.color_accent);
    private final int mDefaultBorderStrokeWidth = 8;
    private final int mDefaultBorderStrokeElevation = mDefaultBorderStrokeWidth / 5;
    private final int mDefaultBorderLineLength = 100;

    protected Paint mLaserPaint;
    protected Paint mFinderMaskPaint;
    protected Paint mBorderPaint;
    protected int mBorderLineLength;
    protected boolean mSquareViewFinder;
    int bgRes;

    public CustomZXingScannerView(Context context, int bgRes) {
        super(context);
        this.bgRes = bgRes;
        init();
    }

    public CustomZXingScannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //set up laser paint
        mLaserPaint = new Paint();
        mLaserPaint.setColor(mDefaultLaserColor);
        mLaserPaint.setStyle(Paint.Style.FILL);

        //finder mask paint
        mFinderMaskPaint = new Paint();
        mFinderMaskPaint.setColor(mDefaultMaskColor);

        //border paint
        mBorderPaint = new Paint();
        mBorderPaint.setColor(mDefaultBorderColor);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mDefaultBorderStrokeWidth);

        mBorderLineLength = mDefaultBorderLineLength;
    }

    public void setLaserColor(int laserColor) {
        mLaserPaint.setColor(laserColor);
    }

    public void setMaskColor(int maskColor) {
        mFinderMaskPaint.setColor(maskColor);
    }

    public void setBorderColor(int borderColor) {
        mBorderPaint.setColor(borderColor);
    }

    public void setBorderStrokeWidth(int borderStrokeWidth) {
        mBorderPaint.setStrokeWidth(borderStrokeWidth);
    }

    public void setBorderLineLength(int borderLineLength) {
        mBorderLineLength = borderLineLength;
    }

    @Override
    public void setLaserEnabled(boolean b) {

    }

    @Override
    public void setBorderCornerRounded(boolean b) {

    }

    @Override
    public void setBorderAlpha(float v) {

    }

    @Override
    public void setBorderCornerRadius(int i) {

    }

    @Override
    public void setViewFinderOffset(int i) {

    }

    // TODO: Need a better way to configure this. Revisit when working on 2.0
    public void setSquareViewFinder(boolean set) {
        mSquareViewFinder = set;
    }

    public void setupViewFinder() {
        updateFramingRect();
        invalidate();
    }

    public Rect getFramingRect() {
        return mFramingRect;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (getFramingRect() == null) {
            return;
        }

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), bgRes);

        DisplayMetrics display = getResources().getDisplayMetrics();

        Bitmap dstbmp = Bitmap.createScaledBitmap(bmp, display.widthPixels, display.heightPixels, false);
        canvas.drawBitmap(dstbmp, 0, 0, null);

        drawViewFinderMask(canvas);
        drawViewFinderBorder(canvas);
//        drawLaser(canvas);
    }

    public void drawViewFinderMask(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        Rect framingRect = getFramingRect();

        canvas.drawRect(0, 0, width, framingRect.top, mFinderMaskPaint);
        canvas.drawRect(0, framingRect.top, framingRect.left, framingRect.bottom + 1, mFinderMaskPaint);
        canvas.drawRect(framingRect.right + 1, framingRect.top, width, framingRect.bottom + 1, mFinderMaskPaint);
        canvas.drawRect(0, framingRect.bottom + 1, width, height, mFinderMaskPaint);
    }

    public void drawViewFinderBorder(Canvas canvas) {
        Rect framingRect = getFramingRect();

        canvas.drawLine(framingRect.left + mDefaultBorderStrokeElevation, framingRect.top, framingRect.left + mDefaultBorderStrokeElevation, framingRect.top + mBorderLineLength, mBorderPaint);
        canvas.drawLine(framingRect.left, framingRect.top + mDefaultBorderStrokeElevation, framingRect.left + mBorderLineLength, framingRect.top + mDefaultBorderStrokeElevation, mBorderPaint);

        canvas.drawLine(framingRect.left + 5, framingRect.bottom - mDefaultBorderStrokeElevation, framingRect.left + mDefaultBorderStrokeElevation, framingRect.bottom - mDefaultBorderStrokeElevation - mBorderLineLength, mBorderPaint);
        canvas.drawLine(framingRect.left, framingRect.bottom - mDefaultBorderStrokeElevation, framingRect.left + mBorderLineLength, framingRect.bottom - mDefaultBorderStrokeElevation, mBorderPaint);

        canvas.drawLine(framingRect.right - mDefaultBorderStrokeElevation, framingRect.top + mDefaultBorderStrokeElevation, framingRect.right - mDefaultBorderStrokeElevation, framingRect.top + mDefaultBorderStrokeElevation + mBorderLineLength, mBorderPaint);
        canvas.drawLine(framingRect.right, framingRect.top + mDefaultBorderStrokeElevation, framingRect.right - mBorderLineLength, framingRect.top + mDefaultBorderStrokeElevation, mBorderPaint);

        canvas.drawLine(framingRect.right - mDefaultBorderStrokeElevation, framingRect.bottom - mDefaultBorderStrokeElevation, framingRect.right - mDefaultBorderStrokeElevation, framingRect.bottom - mDefaultBorderStrokeElevation - mBorderLineLength, mBorderPaint);
        canvas.drawLine(framingRect.right, framingRect.bottom - mDefaultBorderStrokeElevation, framingRect.right - mBorderLineLength, framingRect.bottom - mDefaultBorderStrokeElevation, mBorderPaint);

    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        updateFramingRect();
    }

    public synchronized void updateFramingRect() {
        Point viewResolution = new Point(getWidth(), getHeight());
        int width;
        int height;
        int orientation = DisplayUtils.getScreenOrientation(getContext());

        if (mSquareViewFinder) {
            if (orientation != Configuration.ORIENTATION_PORTRAIT) {
                height = (int) (getHeight() * SQUARE_DIMENSION_RATIO);
                width = height;
            } else {
                width = (int) (getWidth() * SQUARE_DIMENSION_RATIO);
                height = width;
            }
        } else {
            if (orientation != Configuration.ORIENTATION_PORTRAIT) {
                height = (int) (getHeight() * LANDSCAPE_HEIGHT_RATIO);
                width = (int) (LANDSCAPE_WIDTH_HEIGHT_RATIO * height);
            } else {
                width = (int) (getWidth() * PORTRAIT_WIDTH_RATIO);
                height = (int) (PORTRAIT_WIDTH_HEIGHT_RATIO * width);
            }
        }

        if (width > getWidth()) {
            width = getWidth() - MIN_DIMENSION_DIFF;
        }

        if (height > getHeight()) {
            height = getHeight() - MIN_DIMENSION_DIFF;
        }

        int leftOffset = ((viewResolution.x - width) / 2);
        int topOffset = ((viewResolution.y - height) / 2);

        mFramingRect = new Rect(leftOffset + 260, topOffset + 260, leftOffset + width - 260, topOffset + height - 260);
    }
}