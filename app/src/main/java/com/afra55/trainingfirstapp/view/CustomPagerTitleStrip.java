package com.afra55.trainingfirstapp.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Victor Yang on 2016/10/14.
 * CustomPagerTitleStrip
 */

public class CustomPagerTitleStrip extends HorizontalScrollView {

    private ViewPager mViewPager;

    private LinearLayout mContainer;

    private int mChildCount;

    private int mWidth;

    private int mContainerStartPadding;
    private int mContainerEndPadding;

    private LinearLayout.LayoutParams defaultChildLayoutParams;

    public CustomPagerTitleStrip(Context context) {
        super(context);
        init(context);
    }

    public CustomPagerTitleStrip(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomPagerTitleStrip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setFillViewport(true);
        setWillNotDraw(false);


        mContainer = new LinearLayout(context);
        mContainer.setOrientation(LinearLayout.HORIZONTAL);
        mContainer.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT
                        , LinearLayout.LayoutParams.MATCH_PARENT)
        );
        addView(mContainer);

        defaultChildLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        setStratAndEndPadding();
    }

    private void setStratAndEndPadding() {
        int mContainerChildCount = mContainer.getChildCount();
        if (mContainerChildCount <= 0) {
            return;
        }
        if (mContainerChildCount == 1) {
            mContainer.setGravity(Gravity.CENTER);
        } else {
            View childAtStart = mContainer.getChildAt(0);
            mContainerStartPadding =
                    (mWidth - childAtStart.getMeasuredWidth()) / 2;

            View childAtEnd = mContainer.getChildAt(mContainerChildCount - 1);
            mContainerEndPadding =
                    (mWidth - childAtEnd.getMeasuredWidth()) / 2;
            mContainer.setPadding(mContainerStartPadding, 0, mContainerEndPadding, 0);
        }
    }


    public void setViewPager(ViewPager viewPager) {
        mViewPager = viewPager;

        if (mViewPager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }

        mChildCount = mViewPager.getAdapter().getCount();

        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        if (mViewPager == null || mChildCount <= 0) {
            return;
        }
        mContainer.removeAllViews();

        for (int i = 0; i < mChildCount; i++) {
            addChild(i, mViewPager.getAdapter().getPageTitle(i).toString());
        }


    }

    private void addChild(final int position, String title) {
        TextView textView = new TextView(getContext());
//        textView.setLayoutParams(
//                new LinearLayout.LayoutParams(
//                        ViewGroup.LayoutParams.WRAP_CONTENT
//                        , ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(50, 0, 50, 0);
        textView.setText(title);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(position);
            }
        });
        mContainer.addView(textView, position, defaultChildLayoutParams);
    }

    private void toMeasureChild(View view) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec((1 << 30) - 1, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec((1 << 30) - 1, View.MeasureSpec.AT_MOST);
        view.measure(widthMeasureSpec, heightMeasureSpec);
    }

}
