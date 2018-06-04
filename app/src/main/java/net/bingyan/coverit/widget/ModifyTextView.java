package net.bingyan.coverit.widget;

import android.content.ClipboardManager;
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
import android.view.View;

import net.bingyan.coverit.data.local.dataadapter.RedData;
import net.bingyan.coverit.util.DelHtmlTag;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static java.lang.Math.abs;

/**
 * Author       cychust
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

    private float firstX = 0;
    private float firstY = 0;
    private float lastX = 0;
    private float lastY = 0;

    private long downTime;

    private boolean canEdit = false;


    private boolean canModify = true;

    private ArrayList<RedData> redList = new ArrayList<>();


    private ArrayList<RedData> blackList = new ArrayList<>();

    public ArrayList<RedData> getRedList() {
        return redList;
    }

    public void setRedList(ArrayList<RedData> redList) {
        this.redList = redList;
    }

    public ArrayList<RedData> getBlackList() {
        return blackList;
    }

    public void setBlackList(ArrayList<RedData> blackList) {
        this.blackList = blackList;
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
        this.setOverScrollMode(View.OVER_SCROLL_NEVER);
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
                try{
                    getText().setSpan(redColorSpan, redData.getPrevious(), redData.getNext(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }

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
    public boolean onTextContextMenuItem(int id) {

        if (id == android.R.id.paste) {
            //只设置粘贴文本
            int lastCursorPosion = getSelectionStart();
            //拿到粘贴板的文本，setSpan的时候第二个参数last+文本的长度
            ClipboardManager clip = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            String text = clip.getPrimaryClip().getItemAt(0).getText().toString();
            StringBuilder stringBuilder = new StringBuilder(getText());
            stringBuilder.append(DelHtmlTag.delHTMLTag(text));
            //之后，设置光标的时候，填这第二个参数即可
            super.onTextContextMenuItem(android.R.id.paste);
           /* SpannableString ss = new SpannableString(getText());
            //这里之所以分两种情况是因为android系统的粘贴，为了用户体验，会在粘贴的文本前后加上空格，表示是粘贴的内容
            //如果在文本中间粘贴，会在粘贴文本前后都加上空格；如果在文末粘贴，会在粘贴文本前加上空格；如果空的内容中粘贴，则不加空格
            if (lastCursorPosion != 0) {

                ss.setSpan(new StyleSpan(Typeface.NORMAL), lastCursorPosion + 1, lastCursorPosion + 1 + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new AbsoluteSizeSpan((int) ResUtil.Companion.sp2px(getContext(), 14.0f)), lastCursorPosion + 1, lastCursorPosion + 1 + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                setText(DelHtmlTag.delHTMLTag(text));
                setSelection(lastCursorPosion + 1 + text.length());
            } else {
                //ss.setSpan(new StyleSpan(Typeface.NORMAL), lastCursorPosion, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new AbsoluteSizeSpan((int) ResUtil.Companion.sp2px(getContext(), 14.0f)),
                       lastCursorPosion, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new StyleSpan(android.graphics.Typeface.NORMAL),lastCursorPosion, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
              //  ss.setSpan(n,lastCursorPosion, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE););
                setText(DelHtmlTag.delHTMLTag(text));
                setSelection(text.length());
            }*/

            // Log.d(TAG, "onTextContextMenuItem: " + ss.toString());
            setText(stringBuilder.toString());
            Log.d(TAG, "onTextContextMenuItem: " + getText().toString());
            setSelection(getText().length());
            return true;
        }
        return super.onTextContextMenuItem(id);
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
            if (canEdit) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN: {
                        downTime = event.getDownTime();
                        firstX = event.getX();
                        firstY = event.getY();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        lastX = event.getX();
                        lastY = event.getY();
                        if (Math.abs(lastX - firstX) <= 1 && Math.abs(lastY - firstY) <= 1) {
                            if (event.getEventTime() - downTime > 500) {
                                super.performLongClick();
                                return true;
                            }
                        } else return super.onTouchEvent(event);
                    }
                }
                return super.onTouchEvent(event);
            }

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


                    if ((getScrollY() + (int) event.getY()) > layout.getHeight()) break;
                    line = layout.getLineForVertical(getScrollY() + (int) event.getY());
                    offset = layout.getOffsetForHorizontal(line, (int) event.getX());
                    Selection.setSelection(getEditableText(), offset);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if ((getScrollY() + (int) event.getY()) > layout.getHeight()) break;
                    Log.d(TAG, "onTouchEvent: action move");
                    newX = (int) event.getX();
                    newY = (int) event.getY();
                    Log.d(TAG, "onTouchEvent: oldX" + oldX);
                    Log.d(TAG, "onTouchEvent: newX" + newX);
                    if (abs(newX - oldX) < 10) {
                        oldX = newX;
                        if (getScrollY()==0&&newY>oldY){
                           // Log.d("getMeasu",getMeasuredHeight()+"  "+getHeight());
                        }else {
                            this.scrollBy(0, -(newY - oldY));
                        }
                        if (getScrollY()<0){
                          //  Log.d("get","<0");
                            this.scrollTo(0,0);
                        }
                        oldY = newY;
                        return true;
                    }
                    if (oldmSelectionForegroundColorSpan != null && !isNewText)
                        getText().removeSpan(oldmSelectionForegroundColorSpan);

                    isNewText = false;

                    line = layout.getLineForVertical(getScrollY() + (int) event.getY());
                    int curMoveOffset = layout.getOffsetForHorizontal(line, (int) event.getX());
                    Selection.setSelection(getEditableText(), offset, curMoveOffset);
                    ForegroundColorSpan[] spans = getText().getSpans(offset, offset + 1, ForegroundColorSpan.class);
                    if (spans.length != 0) {
                        mSelectionForegroundColorSpan = new ForegroundColorSpan(spans[spans.length - 1].getForegroundColor() == Color.RED ? Color.BLACK : Color.RED);
                        oldmSelectionForegroundColorSpan = mSelectionForegroundColorSpan;
                        getText().setSpan(mSelectionForegroundColorSpan, begin, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    if ((getScrollY() + (int) event.getY()) > layout.getHeight()) break;
                    Log.d(TAG, "onTouchEvent: action up");
                    line = layout.getLineForVertical(getScrollY() + (int) event.getY());
                    int curOffset = layout.getOffsetForHorizontal(line, (int) event.getX());
                    Selection.setSelection(getEditableText(), offset, curOffset);
                    isNewText = true;
                    break;
            }
        }
        return true;


    }

    public void calculateText() {
        if (!redList.isEmpty()) {
            redList.clear();
            blackList.clear();
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
                } else {
                    Log.d(TAG, "changeBlackText: " + i + "::" + next);
                    blackList.add(new RedData(i, next));
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
