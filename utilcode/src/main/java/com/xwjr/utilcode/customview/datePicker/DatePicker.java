package com.xwjr.utilcode.customview.datePicker;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


/**
 * Created by harrishuang on 16/9/7.
 */

public class DatePicker extends WheelPicker {


    private Activity activity;
    private WheelView day;
    String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
    String[] months_little = {"4", "6", "9", "11"};
    private static int START_YEAR = 2018, END_YEAR = 2118;

    final List<String> list_big = Arrays.asList(months_big);
    final List<String> list_little = Arrays.asList(months_little);
    private OnDateSelectListener onDateSelectListener;

    public void setOnDateSelectListener(OnDateSelectListener onDateSelectListener) {
        this.onDateSelectListener = onDateSelectListener;
    }

    public DatePicker(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Nullable
    @Override
    protected View makeHeaderView() {
        return super.makeHeaderView();
    }


    @NonNull
    @Override
    protected View makeCenterView() {

        if (selectYear == 0 || selectMonth == 0 || selectDay == 0) {
            Calendar calendar = Calendar.getInstance();
            int current_year = calendar.get(Calendar.YEAR);
            int current_month = calendar.get(Calendar.MONTH);
            int current_day = calendar.get(Calendar.DATE);
            selectYear = current_year;
            selectMonth = current_month + 1;
            selectDay = current_day;
        }

        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        final WheelView year = new WheelView(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidthPixels / 3, WRAP_CONTENT);
        year.setLayoutParams(params);
        year.setTextSize(textSize);
        year.setTextColor(textColorNormal, textColorFocus);
        year.setLineVisible(lineVisible);
        year.setLineColor(lineColor);
        year.setOffset(offset);
        layout.addView(year);
        year.setItems(getNumItem(START_YEAR, END_YEAR), selectYear - START_YEAR);


        final WheelView month = new WheelView(activity);
        month.setLayoutParams(params);
        month.setTextSize(textSize);
        month.setTextColor(textColorNormal, textColorFocus);
        month.setLineVisible(lineVisible);
        month.setLineColor(lineColor);
        month.setOffset(offset);
        layout.addView(month);
        month.setItems(getNumItem(1, 12), selectMonth-1);


        day = new WheelView(activity);
        day.setLayoutParams(params);
        day.setTextSize(textSize);
        day.setTextColor(textColorNormal, textColorFocus);
        day.setLineVisible(lineVisible);
        day.setLineColor(lineColor);
        day.setOffset(offset);
        layout.addView(day);
        year.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {

                selectYear = Integer.parseInt(item);
                updateDayView();
            }
        });

        month.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectMonth = Integer.parseInt(item);
                updateDayView();

            }
        });
        day.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(boolean isUserScroll, int selectedIndex, String item) {
                selectDay = Integer.parseInt(item);
            }
        });

        if (list_big.contains(String.valueOf(selectMonth))) {
            day.setItems(getNumItem(1, 31), selectDay - 1);
        } else if (list_little.contains(String.valueOf(selectMonth))) {
            day.setItems(getNumItem(1, 30), selectDay - 1);
        } else {
            if ((selectYear % 4 == 0 && selectYear % 100 != 0) || selectYear % 400 == 0)
                day.setItems(getNumItem(1, 29), selectDay - 1);
            else
                day.setItems(getNumItem(1, 28), selectDay - 1);
        }

        return layout;
    }

    public void setDefaultSelect(String select) {
        try {
            String[] data = select.split("-");
            if (data.length == 3) {
                selectYear = Integer.valueOf(data[0]);
                selectMonth = Integer.valueOf(data[1]);
                selectDay = Integer.valueOf(data[2]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private int selectYear;
    private int selectMonth;
    private int selectDay;


    /**
     * 更新数据
     */
    private void updateDayView() {
        if (list_big.contains(String.valueOf(selectMonth))) {
            day.setItems(getNumItem(1, 31), selectDay - 1);
        } else if (list_little.contains(String.valueOf(selectMonth))) {
            if (selectDay > 30) {
                day.setItems(getNumItem(1, 30), 29);
            } else {
                day.setItems(getNumItem(1, 30), selectDay - 1);
            }
        } else {
            if ((selectYear % 4 == 0 && selectYear % 100 != 0) || selectYear % 400 == 0)
                if (selectDay > 29) {
                    day.setItems(getNumItem(1, 29), 28);
                } else {
                    day.setItems(getNumItem(1, 29), selectDay - 1);
                }
            else if (selectDay > 28) {
                day.setItems(getNumItem(1, 28), 27);
            } else {
                day.setItems(getNumItem(1, 28), selectDay - 1);
            }
        }

    }


    /**
     * 提交其他问题
     *
     * @param start
     * @param end
     * @return
     */
    public List<String> getNumItem(int start, int end) {
        List<String> item = new ArrayList<>();
        for (int i = 0; i < (end - start) + 1; i++) {
            item.add((start + i) + "");
        }
        return item;
    }

    @Override
    protected void onSubmit() {

        if (onDateSelectListener != null) {
            String y = String.valueOf(selectYear);
            String m = String.valueOf(selectMonth);
            String d = String.valueOf(selectDay);
            if (selectMonth < 10) {
                m = "0" + m;
            }
            if (selectDay < 10) {
                d = "0" + d;
            }
            onDateSelectListener.onSelect(y, m, d);
        }
        super.onSubmit();
    }

    public interface OnDateSelectListener {

        void onSelect(String year, String month, String day);
    }

}