package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;

import com.slash.youth.databinding.ActivityPublishServiceBinding;

/**
 * Created by zhouyifeng on 2016/9/19.
 */
public class PublishServiceModel extends BaseObservable {

    ActivityPublishServiceBinding mActivityPublishServiceBinding;

    public PublishServiceModel(ActivityPublishServiceBinding activityPublishServiceBinding) {
        this.mActivityPublishServiceBinding = activityPublishServiceBinding;
        initView();
    }

    private void initView() {


    }


//    EditText mInputText;
//    Field[] pickerFields;
//    NumberPicker np;
//    Field pfInputText;

//    private void initView() {
////        Calendar calendar = Calendar.getInstance();
////        Date date = new Date();
////        LogKit.v(date.toGMTString());
////        calendar.setTime(date);
////        LogKit.v(calendar.getTime().toString());
////        calendar.add(Calendar.DAY_OF_MONTH, 1);
////        LogKit.v(calendar.getTime().toString());
////        calendar.add(Calendar.DAY_OF_MONTH, 1);
////        LogKit.v(calendar.getTime().toString());
//
//
//        np = mActivityPublishServiceBinding.np;
//
//        pickerFields = NumberPicker.class.getDeclaredFields();
//
//        for (Field pf : pickerFields) {
//            try {
//                if (pf.getName().equals("mSelectionDivider")) {
//                    pf.setAccessible(true);
//                    pf.set(np, new ColorDrawable(0x00000000));
//                } else if (pf.getName().equals("mInputText")) {
//                    pf.setAccessible(true);
//                    pfInputText = pf;
//                    mInputText = (EditText) pf.get(np);
////                    mInputText.setTextColor(0xffff0000);
////                    mInputText.setTextSize(CommonUtils.dip2px(30));
////                    mInputText.setTextColor(0xffff0000);
//                    mInputText.setTextAppearance(CommonUtils.getContext(),R.color.selector_datepicker_textcolor);
//                }
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//
//        final String[] strings = {"a", "b", "c", "d", "e", "f", "g", "h"};
//        np.setDisplayedValues(strings);
//        np.setMinValue(0);
//        np.setMaxValue(7);
//        np.setValue(3);
//        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                LogKit.v("oldVal" + oldVal);
//                LogKit.v("newVal" + newVal);
//                strings[newVal] = "change";
//                mInputText.setTextColor(0xffff0000);
//                mInputText.invalidate();
//            }
//        });
//    }
}
