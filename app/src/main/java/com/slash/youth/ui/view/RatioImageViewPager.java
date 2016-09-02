package com.slash.youth.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.slash.youth.R;

public class RatioImageViewPager extends ViewPager {

	private float mImgRatio;

	public RatioImageViewPager(Context context) {
		this(context, null);
	}

	public RatioImageViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.com_slash_youth_ui_view_RatioImageViewPager);
		mImgRatio = typedArray
				.getFloat(
						R.styleable.com_slash_youth_ui_view_RatioImageViewPager_imgratio,
						-1);
		typedArray.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int width = MeasureSpec.getSize(widthMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		// int height = MeasureSpec.getSize(heightMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (widthMode == MeasureSpec.EXACTLY
				&& heightMode != MeasureSpec.EXACTLY && mImgRatio > 0) {
			int imageWidth = width - this.getPaddingLeft()
					- this.getPaddingRight();
			int imageHeight = (int) (imageWidth / mImgRatio + 0.5f);
			int height = imageHeight + this.getPaddingTop()
					+ this.getPaddingBottom();
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
					MeasureSpec.EXACTLY);
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
