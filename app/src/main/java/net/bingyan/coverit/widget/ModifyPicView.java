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
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import net.bingyan.coverit.ui.reciteother.ModifyPicActivity;
import net.bingyan.coverit.util.ToolScaleViewUtil;

/**
 * Author       cychust
 * Date         2017.12.24
 * Time         0:08
 */

public class ModifyPicView extends android.support.v7.widget.AppCompatImageView {

    private static final String TAG = "ModifyView";

    private float tmp;

    private int isReload = 0;
    private int mSides = 3;
    private Paint mPaint;

    private Paint linePint;
    private float[] pts;

    private Xfermode mXfermode;
    private Bitmap mMask;
    private Bitmap currBitmap;
    private Path path;


    private int color = Color.WHITE;

    final public static int DRAG = 1;
    final public static int ZOOM = 2;
    final public static int COLOR = 1;
    final public static int PICTURE = 2;


    final public static int DRAG_DOWN_RIGHT = 3;
    final public static int DRAG_TOP_RIGHT = 4;
    final public static int DRAG_TOP_LEFT = 5;
    final public static int DRAG_DOWN_LEFT = 6;


    private int curState = DRAG;

    public int mode = 0;

   // private Matrix matrix;


    private float rectLeft;
    private float rectRight;
    private float rectTop;
    private float rectDown;

    private float priRectLeft;
    private float priRectRight;
    private float priRectTop;
    private float priRectDown;

    private float calRectLeft;

    private float calRectRight;
    private float calRectTop;
    private float calRectDown;
    private float firstX, firstY, actionFirstX, actionFirstY;
    private float moveX, moveY;
    private float lastX, lastY;
    private boolean canClick = false;
    private boolean isTransparent;
    private boolean isLongClick;

    private boolean isMove = false;

    private boolean canModify = true;

    private boolean isDragging = false;

    private boolean isScaling = false;

    private Rect clipSrcRect;//保存要裁剪的矩形

    private Rect clipDstRect;

    private Bitmap bitmap;

    private double widTimes;

    private double heiTimes;

    private boolean isSwitch = false;

    private long downTime;

    private PointF mid = new PointF();

    private float initDis = 1f;

    private Canvas canvas;
    //  private String picPath;

    private final float lineLength = 25.0f;
    private final float lineWidth = 11.0f;

    private ModifyPicActivity thisActivity;

    public void setCalRectLeft(float calRectLeft) {
        this.calRectLeft = calRectLeft;
    }

    public void setCalRectRight(float calRectRight) {
        this.calRectRight = calRectRight;
    }

    public void setCalRectTop(float calRectTop) {
        this.calRectTop = calRectTop;
    }

    public void setCalRectDown(float calRectDown) {
        this.calRectDown = calRectDown;
    }

    public void setThisActivity(ModifyPicActivity thisActivity) {
        this.thisActivity = thisActivity;
    }

    public void setCanModify(boolean canModify) {
        this.canModify = canModify;
    }

    public void setMove(boolean move) {
        isMove = move;
    }

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

    public ModifyPicView(Context context, Bitmap bitmap) {
        this(context, null, bitmap);
    }

    public ModifyPicView(Context context, AttributeSet attrs, Bitmap bitmap) {
        this(context, attrs, 0, bitmap);
    }

    public ModifyPicView(Context context, AttributeSet attrs, int defStyleAttr, Bitmap bitmap) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        linePint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePint.setColor(Color.BLACK);
        linePint.setStrokeWidth(lineWidth);

        path = new Path();
       // matrix = new Matrix();
        //this.picPath=picPath;
        //bitmap = BitmapFactory.decodeFile(picPath);
        if (bitmap == null) {
            throw new IllegalStateException("bitmap cannot be null");
        } else
            this.bitmap = bitmap;
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
        float[] pts = {rectLeft, rectTop - lineWidth / 2, rectLeft, rectTop + lineLength
                , rectLeft, rectTop, rectLeft + lineLength, rectTop
                , rectRight - lineLength, rectTop, rectRight + lineWidth / 2, rectTop
                , rectRight, rectTop, rectRight, rectTop + lineLength
                , rectRight, rectDown - lineLength, rectRight, rectDown + lineWidth / 2
                , rectRight, rectDown, rectRight - lineLength, rectDown
                , rectLeft - lineWidth / 2, rectDown, rectLeft + lineLength, rectDown
                , rectLeft, rectDown, rectLeft, rectDown - lineLength};     //画四角的黑线
        canvas.drawLines(pts, linePint);


        if (!isSwitch) {
            if (isMove)
                canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            mPaint.setColor(color);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setAlpha(255);
            canvas.drawRect(rectLeft, rectTop, rectRight, rectDown, mPaint);
        } else {
            clipSrcRect = new Rect((int) (calRectLeft * widTimes), (int) (calRectTop * heiTimes), (int) (calRectRight * widTimes), (int) (calRectDown * heiTimes));
            clipDstRect = new Rect((int) rectLeft, (int) rectTop, (int) rectRight, (int) rectDown);


            //bitmap = BitmapFactory.decodeFile(picPath);
            if (bitmap == null) {
                throw new IllegalStateException("bitmap cannot be init");
            } else {
                canvas.drawBitmap(bitmap, clipSrcRect, clipDstRect, null);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean isInRect = false;
        if ((event.getX() > Math.min(rectLeft - 50, rectRight + 50) && event.getX() < Math.max(rectLeft - 50, rectRight + 50)) && ((event.getY() > Math.min(rectTop - 50, rectDown + 50) && (event.getY() < Math.max(rectTop - 50, rectDown + 50)))))
            isInRect = true;
        if (isInRect) {
            int action = event.getAction();
            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    //curState = DRAG;
                    firstX = event.getX();
                    firstY = event.getY();

                    actionFirstX = event.getX();//为了长按删除
                    actionFirstY = event.getY();

                    priRectLeft = this.rectLeft;
                    priRectRight = this.rectRight;
                    priRectTop = this.rectTop;
                    priRectDown = this.rectDown;


                    if (Math.abs(event.getX() - this.rectRight) <= 50 && Math.abs(event.getY() - this.rectDown) <= 50) {
                        curState = DRAG_DOWN_RIGHT;
                    } else if (Math.abs(event.getX() - this.rectRight) <= 50 && Math.abs(event.getY() - this.rectTop) <= 50) {
                        curState = DRAG_TOP_RIGHT;
                    } else if (Math.abs(event.getX() - this.rectLeft) <= 50 & Math.abs(event.getY() - this.rectTop) <= 50) {
                        curState = DRAG_TOP_LEFT;
                    } else if (Math.abs(event.getX() - this.rectLeft) <= 50 & Math.abs(event.getY() - this.rectDown) <= 50) {
                        curState = DRAG_DOWN_LEFT;
                    } else {
                        curState = DRAG;
                    }

                    downTime = event.getDownTime();
                    break;

                case MotionEvent.ACTION_POINTER_DOWN:
                   /* if (event.getPointerCount() >= 2) {
                        curState = ZOOM;

                        priRectLeft = this.rectLeft;
                        priRectRight = this.rectRight;
                        priRectTop = this.rectTop;
                        priRectDown = this.rectDown;

                        initDis = ToolScaleViewUtil.spacing(event);
                        mid = ToolScaleViewUtil.midPoint(event);
                    }*/
                    break;

                case MotionEvent.ACTION_MOVE: {
                    /*if (curState == DRAG) {
                        moveX = event.getX();

                        moveY = event.getY();

                        if (Math.abs(moveX - firstX) > 5 || Math.abs(moveY - firstY) > 5) {
                            // isDragging = true;
                            float distanceX = moveX - firstX;
                            float distanceY = moveY - firstY;
                            this.rectRight += distanceX;
                            this.rectDown += distanceY;

                            changeState();

                            isMove = true;
                            this.invalidate();
                            isMove = false;
                            firstY = moveY;
                            firstX = moveX;
                        }
                    } else*/
                    if (curState == DRAG_TOP_RIGHT) {
                        moveX = event.getX();
                        moveY = event.getY();
                        if (Math.abs(moveY - firstX) > 5 || Math.abs(moveX - firstY) > 5) {
                            float distanceX = moveX - firstX;
                            float distanceY = moveY - firstY;
                            this.rectRight += distanceX;
                            this.rectTop += distanceY;

                            changeState();

                            isMove = true;
                            this.invalidate();
                            isMove = false;
                            firstY = moveY;
                            firstX = moveX;
                        }
                    } else if (curState == DRAG_TOP_LEFT) {
                        moveX = event.getX();
                        moveY = event.getY();
                        if (Math.abs(moveY - firstX) > 5 || Math.abs(moveX - firstY) > 5) {
                            float distanceX = moveX - firstX;
                            float distanceY = moveY - firstY;
                            this.rectLeft += distanceX;
                            this.rectTop += distanceY;

                            changeState();  //view 翻折

                            isMove = true;
                            this.invalidate();
                            isMove = false;
                            firstY = moveY;
                            firstX = moveX;
                        }
                    } else if (curState == DRAG_DOWN_LEFT) {
                        moveX = event.getX();
                        moveY = event.getY();
                        if (Math.abs(moveY - firstX) > 5 || Math.abs(moveX - firstY) > 5) {
                            float distanceX = moveX - firstX;
                            float distanceY = moveY - firstY;
                            this.rectLeft += distanceX;
                            this.rectDown += distanceY;

                            changeState();

                            isMove = true;
                            this.invalidate();
                            isMove = false;
                            firstY = moveY;
                            firstX = moveX;
                        }
                    } else if (curState == DRAG_DOWN_RIGHT) {
                        moveX = event.getX();
                        moveY = event.getY();
                        if (Math.abs(moveY - firstX) > 5 || Math.abs(moveX - firstY) > 5) {
                            float distanceX = moveX - firstX;
                            float distanceY = moveY - firstY;
                            this.rectRight += distanceX;
                            this.rectDown += distanceY;

                            changeState();

                            isMove = true;
                            this.invalidate();
                            isMove = false;
                            firstY = moveY;
                            firstX = moveX;
                        }
                    } else if (curState == DRAG) {
                        moveX = event.getX();

                        moveY = event.getY();

                        if (Math.abs(moveX - firstX) > 5 || Math.abs(moveY - firstY) > 5) {
                            isDragging = true;
                            float distanceX = moveX - firstX;
                            float distanceY = moveY - firstY;
                            this.rectLeft = priRectLeft + distanceX;
                            this.rectRight = priRectRight + distanceX;
                            this.rectTop = priRectTop + distanceY;
                            this.rectDown = priRectDown + distanceY;

                            this.invalidate();

                        }
                    }
                }

                break;

                case MotionEvent.ACTION_UP:
                    lastX = event.getX();
                    lastY = event.getY();
                    if (Math.abs(lastX - actionFirstX) <= 10 && Math.abs(lastY - actionFirstY) <= 10) {        //<5 认为没有动
                        if (event.getEventTime() - downTime > 1000) {
                            isLongClick = true;
                            onLongClick();
                            isLongClick = false;
                        } else performClick();
                    }
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


    private void changeState() {
        if (rectLeft > rectRight && rectTop < rectDown) {
            tmp = rectRight;
            rectRight = rectLeft;
            rectLeft = tmp;
            switch (curState) {
                case DRAG_TOP_LEFT:
                    curState = DRAG_TOP_RIGHT;
                    break;
                case DRAG_TOP_RIGHT:
                    curState = DRAG_TOP_LEFT;
                    break;
                case DRAG_DOWN_RIGHT:
                    curState = DRAG_DOWN_LEFT;
                    break;
                case DRAG_DOWN_LEFT:
                    curState = DRAG_DOWN_RIGHT;
                    break;
            }
        } else if (rectLeft < rectRight && rectTop > rectDown) {
            tmp = rectTop;
            rectTop = rectDown;
            rectDown = tmp;
            switch (curState) {
                case DRAG_TOP_LEFT:
                    curState = DRAG_DOWN_LEFT;
                    break;
                case DRAG_TOP_RIGHT:
                    curState = DRAG_DOWN_RIGHT;
                    break;
                case DRAG_DOWN_RIGHT:
                    curState = DRAG_TOP_RIGHT;
                    break;
                case DRAG_DOWN_LEFT:
                    curState = DRAG_TOP_LEFT;
                    break;
            }
        } else if (rectLeft > rectRight && rectTop > rectDown) {
            tmp = rectRight;
            rectRight = rectLeft;
            rectLeft = tmp;
            tmp = rectTop;
            rectTop = rectDown;
            rectDown = tmp;
            switch (curState) {
                case DRAG_TOP_LEFT:
                    curState = DRAG_DOWN_RIGHT;
                    break;
                case DRAG_TOP_RIGHT:
                    curState = DRAG_DOWN_LEFT;
                    break;
                case DRAG_DOWN_RIGHT:
                    curState = DRAG_TOP_LEFT;
                    break;
                case DRAG_DOWN_LEFT:
                    curState = DRAG_TOP_RIGHT;
                    break;
            }
        }

    }

    private void onLongClick() {
        if (canModify)
            thisActivity.removeView(this);
    }

    public void setCanClick(boolean canClick) {
        this.canClick = canClick;
    }

    public void ViewDestroy() {
        if (bitmap != null) {
            bitmap.recycle();
            Log.d("bitmap view", "destroy");
            bitmap=null;
        }


    }
}