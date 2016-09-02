package com.slash.youth.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.slash.youth.R;

public class RatioLayout extends FrameLayout {

	private float mRatio;

	public RatioLayout(Context context) {
		this(context, null);
	}

	public RatioLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RatioLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.com_slash_youth_ui_view_RatioLayout);
		mRatio = typedArray.getFloat(
				R.styleable.com_slash_youth_ui_view_RatioLayout_ratio, -1);
		typedArray.recycle();

		// System.out.println(mRatio);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int width = MeasureSpec.getSize(widthMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		// int height = MeasureSpec.getSize(heightMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (widthMode == MeasureSpec.EXACTLY
				&& heightMode != MeasureSpec.EXACTLY && mRatio > 0) {
			int imageWidth = width - this.getPaddingLeft()
					- this.getPaddingRight();
			int imageHeight = (int) (imageWidth / mRatio + 0.5f);
			int height = imageHeight + this.getPaddingTop()
					+ this.getPaddingBottom();
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
					MeasureSpec.EXACTLY);
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
