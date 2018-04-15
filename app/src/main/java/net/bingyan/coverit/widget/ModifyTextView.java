package net.bingyan.coverit.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.Selection;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MotionEvent;

import net.bingyan.coverit.data.local.dataadapter.RedData;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static java.lang.Math.abs;

/**
 * Author       zdlly
 * Date         2017.12.24
 * Time         9:24
 */

public class ModifyTextView extends android.support.v7.widget.AppCompatEditText {
    private int offset;
    private ForegroundColorSpan mSelectionForegroundColorSpan;
    private ForegroundColorSpan oldmSelectionForegroundColorSpan;
    private boolean isNewText = false;
    private int oldX;
    private int newX;
    private int oldY;
    private int newY;


    private boolean canEdit = false;


    private boolean canModify = true;

    private ArrayList<RedData> redList = new ArrayList<>();


    private ArrayList<RedData> blackList =new ArrayList<>();

    public ArrayList<RedData> getRedList() {
        return redList;
    }

    public void setRedList(ArrayList<RedData> redList) {
        this.redList = redList;
    }

    public void setBlackList(ArrayList<RedData> blackList) {
        this.blackList = blackList;
    }public ArrayList<RedData> getBlackList() {
        return blackList;
    }

    public void setCanModify(boolean canModify) {
        this.canModify = canModify;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }


    public ModifyTextView(Context context) {
        super(context);
        initLook();
    }


    public ModifyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initLook();
    }

    public ModifyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLook();
    }

    private void initLook() {
        setGravity(Gravity.TOP);
        setBackgroundColor(Color.WHITE);
        drawRed();

    }

    public void drawBlack() {
        ForegroundColorSpan blackColorSpan = new ForegroundColorSpan(Color.BLACK);
        getText().setSpan(blackColorSpan, 0, getText().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

    }

    public void drawRed() {
        if (!redList.isEmpty()) {
            for (RedData redData : redList) {
                ForegroundColorSpan redColorSpan = new ForegroundColorSpan(Color.RED);
                getText().setSpan(redColorSpan, redData.getPrevious(), redData.getNext(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }
        }
    }

    @Override
    protected void onCreateContextMenu(ContextMenu menu) {
        if (canEdit) {
            super.onCreateContextMenu(menu);
        }
    }


    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    protected boolean getDefaultEditable() {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (canModify) {
            int begin = Selection.getSelectionStart(getText());
            int end = Selection.getSelectionEnd(getText());
            if (begin > end) {
                int swap = begin;
                begin = end;
                end = swap;
            }
            int action = event.getAction();
            Layout layout = getLayout();
            int line;
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    Log.d(TAG, "onTouchEvent: action down");
                    oldX = (int) event.getX();
                    oldY = (int) event.getY();
                    line = layout.getLineForVertical(getScrollY() + (int) event.getY());
                    offset = layout.getOffsetForHorizontal(line, (int) event.getX());
                    Selection.setSelection(getEditableText(), offset);
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.d(TAG, "onTouchEvent: action move");
                    newX = (int) event.getX();
                    newY = (int) event.getY();
                    Log.d(TAG, "onTouchEvent: oldX" + oldX);
                    Log.d(TAG, "onTouchEvent: newX" + newX);

                    if (abs(newX - oldX) < 10) {
                        oldX = newX;
                        this.scrollBy(0, -(newY - oldY));
                        oldY = newY;
                        return true;
                    }
                    if (oldmSelectionForegroundColorSpan != null && !isNewText)
                        getText().removeSpan(oldmSelectionForegroundColorSpan);

                    isNewText = false;

                    line = layout.getLineForVertical(getScrollY() + (int) event.getY());
                    int curMoveOffset = layout.getOffsetForHorizontal(line, (int) event.getX());
                    Selection.setSelection(getEditableText(), offset, curMoveOffset);
                    ForegroundColorSpan[] spans = getText().getSpans(offset, offset+1, ForegroundColorSpan.class);
                    mSelectionForegroundColorSpan = new ForegroundColorSpan(spans[spans.length-1].getForegroundColor()==Color.RED? Color.BLACK : Color.RED);
                    oldmSelectionForegroundColorSpan = mSelectionForegroundColorSpan;
                    getText().setSpan(mSelectionForegroundColorSpan, begin, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    break;

                case MotionEvent.ACTION_UP:
                    Log.d(TAG, "onTouchEvent: action up");
                    line = layout.getLineForVertical(getScrollY() + (int) event.getY());
                    int curOffset = layout.getOffsetForHorizontal(line, (int) event.getX());
                    Selection.setSelection(getEditableText(), offset, curOffset);
                    isNewText = true;
                    break;
            }
            if (canEdit) {
                super.onTouchEvent(event);
                return true;
            } else return true;
        } else if(canEdit){
            return super.onTouchEvent(event);
        }else return true;
    }

    public void calculateText() {
        if (!redList.isEmpty()) {
            redList.clear();
        }
        int next;
        for (int i = 0; i < getText().length(); i = next) {
            next = getText().nextSpanTransition(i, getText().length(), ForegroundColorSpan.class);
            ForegroundColorSpan[] spans = getText().getSpans(i, next, ForegroundColorSpan.class);
            if (spans.length != 0) {
                ForegroundColorSpan span = spans[spans.length - 1];
                if (span.getForegroundColor() == Color.RED) {
                    Log.d(TAG, "changeRedText: " + i + "::" + next);
                    redList.add(new RedData(i, next));
                }else {
                    Log.d(TAG, "changeBlackText: " + i + "::" + next);
                    blackList.add(new RedData(i,next));
                }
            }
        }
    }

    public void changeText() {
        calculateText();
        for (RedData redData : redList) {
            String replacedString = getText().subSequence(redData.getPrevious(), redData.getNext()).toString().replaceAll("[^(\\p{P}|\\n|\\r|\\s)]", "_");
            this.setText(getText().replace(redData.getPrevious(), redData.getNext(), replacedString));
        }
    }


}
