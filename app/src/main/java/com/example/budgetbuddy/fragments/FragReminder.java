package com.example.budgetbuddy.fragments;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.budgetbuddy.R;
import com.example.budgetbuddy.data.AlarmReminderContract;
import com.example.budgetbuddy.reminder.AlarmScheduler;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;


public class FragReminder extends FragBase  {

    private Calendar mCalendar;
    private int mYear, mMonth, mHour, mMinute, mDay;
    private long mRepeatTime;
    private Switch mRepeatSwitch;
    private String mTitle;
    private String mTime;
    private String mDate;
    private String mRepeat;
    private String mRepeatNo;
    private String mRepeatType;
    private String mActive;
    private LinearLayout lytSelectTime;
    private Uri mCurrentReminderUri;
    private ImageView switchReminder;




    // Constant values in milliseconds
    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;
    private TextView txtTime;


    @Override
    int getResourceLayout() {
        return R.layout.frag_reminder;
    }

    @Override
    void setUpView() {

        init();
        clickListeners();

    }

    private void clickListeners() {
        lytSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime(view);
            }
        });



        switchReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchReminder.setSelected(!switchReminder.isSelected());
                Log.e("switch", String.valueOf(switchReminder.isSelected()));
            }
        });


    }

    private void init() {
        switchReminder = getFragView().findViewById(R.id.switch_reminder);
        txtTime = getFragView().findViewById(R.id.txt_time);
        lytSelectTime = getFragView().findViewById(R.id.lyt_select_time);
        mCurrentReminderUri = ContentUris.withAppendedId(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, 00);
    }

    public void setTime(View v){

        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;

                        if(hourOfDay > 12 && minute < 10){
                            mTime = (hourOfDay-12) + ":"+"0"+minute +" PM";
                        }else if(hourOfDay<13 && minute < 10){
                            mTime = hourOfDay + ":"+"0"+minute +" AM";
                        }else if(hourOfDay>12){
                            mTime = (hourOfDay-12) + ":"+minute +" PM";
                        }else {
                            mTime = (hourOfDay-12) + ":"+minute +" AM";
                        }


                        txtTime.setText(mTime);

                    }
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.setThemeDark(false);
        tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
    }

    public void saveReminder(){

     /*   if (mCurrentReminderUri == null ) {
            // Since no fields were modified, we can return early without creating a new reminder.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }
*/

        ContentValues values = new ContentValues();

        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TITLE, mTitle);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_DATE, mDate);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TIME, mTime);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT, mRepeat);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO, mRepeatNo);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE, mRepeatType);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE, mActive);


        // Set up calender for creating the notification
        mCalendar.set(Calendar.MONTH, --mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        mCalendar.set(Calendar.MINUTE, mMinute);
        mCalendar.set(Calendar.SECOND, 0);

        long selectedTimestamp =  mCalendar.getTimeInMillis();

        // Check repeat type
        if (mRepeatType.equals("Minute")) {
            mRepeatTime = Integer.parseInt(mRepeatNo) * milMinute;
        } else if (mRepeatType.equals("Hour")) {
            mRepeatTime = Integer.parseInt(mRepeatNo) * milHour;
        } else if (mRepeatType.equals("Day")) {
            mRepeatTime = Integer.parseInt(mRepeatNo) * milDay;
        } else if (mRepeatType.equals("Week")) {
            mRepeatTime = Integer.parseInt(mRepeatNo) * milWeek;
        } else if (mRepeatType.equals("Month")) {
            mRepeatTime = Integer.parseInt(mRepeatNo) * milMonth;
        }

        if (mCurrentReminderUri == null) {
            // This is a NEW reminder, so insert a new reminder into the provider,
            // returning the content URI for the new reminder.
            Uri newUri = baseContext.getContentResolver().insert(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(baseContext, getString(R.string.editor_insert_reminder_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(baseContext, getString(R.string.editor_insert_reminder_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {

            int rowsAffected = baseContext.getContentResolver().update(mCurrentReminderUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(baseContext, getString(R.string.editor_update_reminder_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(baseContext, getString(R.string.editor_update_reminder_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Create a new notification
        if (mActive.equals("true")) {
            if (mRepeat.equals("true")) {
                new AlarmScheduler().setRepeatAlarm(baseContext, selectedTimestamp, mCurrentReminderUri, mRepeatTime);
            } else if (mRepeat.equals("false")) {
                new AlarmScheduler().setAlarm(baseContext, selectedTimestamp, mCurrentReminderUri);
            }

            Toast.makeText(baseContext, "Alarm time is " + selectedTimestamp,
                    Toast.LENGTH_LONG).show();
        }

        // Create toast to confirm new reminder
        Toast.makeText(baseContext, "Saved",
                Toast.LENGTH_SHORT).show();

    }
}