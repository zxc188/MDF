package com.example.administrator.mdf.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;



public class MyGradeView extends GridView {
    public MyGradeView(Context context) {
        super(context);
    }

    public MyGradeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGradeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
