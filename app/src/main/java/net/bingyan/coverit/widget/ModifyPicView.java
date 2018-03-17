package net.bingyan.coverit.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.azeesoft.lib.colorpicker.ColorPickerDialog;

/**
 * Author       zdlly
 * Date         2017.12.24
 * Time         0:08
 */

public class ModifyPicView extends android.support.v7.widget.AppCompatImageView {

    private static final String TAG = "ModifyView";
    private ColorPickerDialog colorPickerDialog;

    private int isReload = 0;
    private int mSides = 3;
    private Paint mPaint;
    private Xfermode mXfermode;
    private Bitmap mMask;
    private Bitmap currBitmap;
    private Path path;


    private int color = Color.WHITE;

    final public static int DRAG = 1;
    final public static int ZOOM = 2;
    final public static int COLOR = 1;
    final public static int PICTURE = 2;


    public int mode = 0;

    private Matrix matrix = new Matrix();
    private Matrix matrix1 = new Matrix();
    private Matrix saveMatrix = new Matrix();

    private float rectLeft;
    private float rectRight;
    private float rectTop;
    private float rectDown;

    private float firstX, firstY;
    private float lastX, lastY;
    private boolean canClick = false;
    private boolean isTransparent;
    private boolean isLongClick;

    private Rect clipSrcRect;//保存要裁剪的矩形
    private Rect clipDstRect;

    private static Bitmap bitmap;



    private double widTimes;
    private double heiTimes;

    private boolean isSwitch=false;

    private long downTime;


    private PointF mid = new PointF();
    private float initDis = 1f;

    private Canvas canvas;

    public void setWidTimes(double widTimes) {
        this.widTimes = widTimes;
    }

    public void setHeiTimes(double heiTimes) {
        this.heiTimes = heiTimes;
    }
    public void setSwitch(boolean aSwitch) {
        this.isSwitch = aSwitch;
    }
    public void setColor(int color) {
        this.color = color;
    }

    public void setIsReload(int isReload) {
        this.isReload = isReload;
    }

    public float getRectLeft() {
        return rectLeft;
    }

    public void setRectLeft(float rectLeft) {
        this.rectLeft = rectLeft;
    }

    public float getRectRight() {
        return rectRight;
    }

    public void setRectRight(float rectRight) {
        this.rectRight = rectRight;
    }

    public float getRectTop() {
        return rectTop;
    }

    public void setRectTop(float rectTop) {
        this.rectTop = rectTop;
    }

    public float getRectDown() {
        return rectDown;
    }

    public void setRectDown(float rectDown) {
        this.rectDown = rectDown;
    }

    public ModifyPicView(Context context,String picPath) {
        this(context, null,picPath);
    }

    public ModifyPicView(Context context, AttributeSet attrs,String picPath) {
        this(context, attrs, 0,picPath);
    }

    public ModifyPicView(Context context, AttributeSet attrs, int defStyleAttr,String picPath) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        path = new Path();
        matrix = new Matrix();
        bitmap = BitmapFactory.decodeFile(picPath);
    }

    @Override
    public boolean performClick() {
        super.performClick();
        if (canClick) {
            Log.d(TAG, "performClick: clicked!");
            super.performClick();
            if (isTransparent) {
                this.setAlpha(1.0f);
                isTransparent = false;
            } else {
                this.setAlpha(0.5f);
                isTransparent = true;
            }

            this.invalidate();
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!isSwitch){
            mPaint.setColor(color);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setAlpha(255);
            canvas.drawRect(rectLeft, rectTop, rectRight, rectDown, mPaint);
        }else{
            clipSrcRect=new Rect((int)(rectLeft*widTimes), (int)(rectTop*heiTimes), (int)(rectRight*widTimes), (int)(rectDown*heiTimes));
            clipDstRect=new Rect((int)rectLeft, (int)rectTop, (int)rectRight, (int)rectDown);

            canvas.drawBitmap(bitmap,clipSrcRect,clipDstRect,null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getX() >= rectLeft && event.getX() <= rectRight && event.getY() >= rectTop && event.getY() <= rectDown) {
            int action = event.getAction();
            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    firstX = event.getX();
                    firstY = event.getY();
                    downTime=event.getDownTime();
                    Log.d(TAG, "onTouchEvent: myView down");
                    break;

                case MotionEvent.ACTION_POINTER_DOWN:
                    Log.d(TAG, "onTouchEvent: myVIew MUL down");
                    break;

                case MotionEvent.ACTION_MOVE: {
                    Log.d(TAG, "onTouchEvent: myView move");
                }
                break;

                case MotionEvent.ACTION_UP:
                    lastX = event.getX();
                    lastY = event.getY();
                    Log.d(TAG, "onTouchEvent: myView up");
                    Log.d(TAG, "onTouchEvent: " + Math.abs(lastX - firstX));
                    Log.d(TAG, "onTouchEvent: " + Math.abs(lastY - firstY));
                    if (event.getEventTime() - downTime > 1000) {
                        isLongClick=true;
                        onLongClick();
                    }else if (Math.abs(lastX - firstX) <= 1 && Math.abs(lastY - firstY) <= 1) {
                        performClick();
                    }
                    isLongClick=false;
                    break;
                case MotionEvent.ACTION_POINTER_UP:

                    break;
                case MotionEvent.ACTION_OUTSIDE:
                    break;
            }
            return true;
        } else {
            return false;
        }

    }


    private void onLongClick() {
        colorPickerDialog = ColorPickerDialog.createColorPickerDialog(getContext());
        colorPickerDialog.setOnColorPickedListener(new ColorPickerDialog.OnColorPickedListener() {
            @Override
            public void onColorPicked(int color, String hexVal) {
                setColor(color);
                invalidate();
            }
        });
        colorPickerDialog.setHexaDecimalTextColor(Color.parseColor("#ffffff"));
        colorPickerDialog.show();
    }
    public void setCanClick(boolean canClick) {
        this.canClick = canClick;
    }

}