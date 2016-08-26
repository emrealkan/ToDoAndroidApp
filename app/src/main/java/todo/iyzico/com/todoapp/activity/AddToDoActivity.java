package todo.iyzico.com.todoapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

import todo.iyzico.com.todoapp.R;
import todo.iyzico.com.todoapp.models.UserToDos;
import todo.iyzico.com.todoapp.tools.ActivityAnimator;

/**
 * Created by emrealkan on 26/08/16.
 */
public class AddToDoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public final static String TAG_ComplexId = "complexId";
    public final static String TAG_ComplexName = "complexName";

    private final int DATE_SIGN = 0;
    private final int TIME_SIGN = 1;
    private final int OTHER_SIGN = 2;
    private final String DEFAULT_VALUE = "...";
    private final String pastTime_Message = "Geçmiş gün veya saat seçemezsiniz..";
    private final String maxTime_Message = "14 günden daha ilerisini seçemezsiniz..";

    protected ActivityAnimator activityAnimator;

    private ImageView start_date, end_date;
    private TextView txt_start_date, txt_end_date;
    private LinearLayout layout_start_date, layout_end_date;

    private int complexId;
    private String chosenDate_webServiceFormat;
    private boolean isChosenToday;
    //
    private static CreateToDoResponseListener createToDoResponseListener;

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    public interface CreateToDoResponseListener
    {
        public void onResponseSuccess(Activity source,  UserToDos userToDos);
    }

    final long minDate = System.currentTimeMillis() - DateUtils.DAY_IN_MILLIS + DateUtils.HOUR_IN_MILLIS;

    public static void setCreateToDoResponseListener(CreateToDoResponseListener listener)
    {
        createToDoResponseListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        activityAnimator = new ActivityAnimator();
        activityAnimator.pullTop(this);

        TextView txt_TitleName = (TextView) findViewById(R.id.addTodo_pageName);
        txt_TitleName.setText("Create New Todo");

        start_date = (ImageView) findViewById(R.id.addTodo_start_date);
        end_date = (ImageView) findViewById(R.id.addTodo_end_date);

        txt_start_date = (TextView) findViewById(R.id.addTodo_startDate);
        txt_end_date = (TextView) findViewById(R.id.addTodo_endDate);

        layout_start_date = (LinearLayout) findViewById(R.id.addTodo_layout_startDate);
        layout_end_date = (LinearLayout) findViewById(R.id.addTodo_layout_endDate);

        View.OnClickListener dateClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showDateDialog(txt_start_date, AddToDoActivity.this);
            }
        };


        start_date.setOnClickListener(dateClickListener);
        end_date.setOnClickListener(dateClickListener);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.addtodos_toolbar);
        toolbar.setTitle(getResources().getString(R.string.activity_addTodo_name));
        toolbar.setNavigationIcon(R.drawable.ic_delete);
        //        toolbar.inflateMenu(R.menu.addtodo_page);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });*/
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener()
//        {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem)
//            {
//                Log.e("Menu", "New Menu Item is clicked");
//                int id = menuItem.getItemId();
//                if (id == R.id.action_save_todos)
//                {
//                    Log.e("Menu", "Save Clicked");
//                    checkAllFieldsAreFilled();
//                    return true;
//                }
//                return false;
//            }
//        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        activityAnimator.pullBottom(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        /*if (id == R.id.action_save_todos)
        {
            Log.e("Menu", "Save Clicked");
            String value = checkAllFieldsAreFilled();
            if(value != null)
            {
                showAlertDialog(OTHER_SIGN, value);
            }
            else
            {
                //Save todo
               // new CreateTodoTask().execute();
            }

            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    private String checkAllFieldsAreFilled()
    {
        if(txt_start_date.getText().equals(DEFAULT_VALUE))
        {
            return "Lütfen tarih seçiniz..";
        }
        return null;
    }

    public void showDateDialog(final TextView dateTxt, Activity activity) {
        Time today = new Time();
        today.setToNow();
        DatePickerDialog dateDlg = new DatePickerDialog(activity,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Time chosenDate = new Time();
                        chosenDate.set(dayOfMonth, monthOfYear, year);

                        long dtDob = chosenDate.toMillis(true);

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);

                        Calendar maxDate = Calendar.getInstance();
                        maxDate.add(Calendar.DATE, 14);

                        if(calendar.getTimeInMillis() < minDate)
                        {
                            // Warn user
                            showAlertDialog(DATE_SIGN, pastTime_Message);
                        }
                        else if(calendar.getTimeInMillis() > maxDate.getTimeInMillis())
                        {
                            showAlertDialog(DATE_SIGN, maxTime_Message);
                        }
                        else
                        {
                            isChosenToday = DateUtils.isToday(dtDob);
                            chosenDate_webServiceFormat = DateFormat.format("yyyy-MM-dd", dtDob).toString();
                            String DateStr = DateFormat.format("EEEE, d MMMM", dtDob).toString();
                            dateTxt.setText(DateStr);
                            txt_start_date.setText(DEFAULT_VALUE);
                        }
                    }
                }, today.year, today.month, today.monthDay);
        DatePicker datePicker = dateDlg.getDatePicker();
        datePicker.setCalendarViewShown(false);
        datePicker.setMinDate(System.currentTimeMillis() - 1000);
        dateDlg.show();
    }

    private void showAlertDialog(final int dateOrTime, String message)
    {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage(message);
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton(R.string.ok_text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(dateOrTime == DATE_SIGN) {
                    showDateDialog(txt_start_date, AddToDoActivity.this);
                }
            }
        });
        dlgAlert.create().show();
    }

}
