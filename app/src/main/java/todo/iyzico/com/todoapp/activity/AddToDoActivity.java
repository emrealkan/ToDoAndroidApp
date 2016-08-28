package todo.iyzico.com.todoapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import todo.iyzico.com.todoapp.R;
import todo.iyzico.com.todoapp.models.BaseResponse;
import todo.iyzico.com.todoapp.models.ToDo;
import todo.iyzico.com.todoapp.models.User;
import todo.iyzico.com.todoapp.tools.ActivityAnimator;
import todo.iyzico.com.todoapp.tools.MessageDialog;
import todo.iyzico.com.todoapp.tools.ProgressDialogTool;
import todo.iyzico.com.todoapp.webservice.WebServiceBuilder;

/**
 * Created by emrealkan on 26/08/16.
 */
public class AddToDoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected ActivityAnimator activityAnimator;

    private ImageView start_date, end_date, backButton;
    private TextView txt_start_date, txt_end_date;
    private EditText addTodo_title, addTodo_subTitle, addTodo_content;
    private Button saveTodo;

    private String startDate_webServiceFormat = null;
    private String endDate_webServiceFormat = null;

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        activityAnimator = new ActivityAnimator();
        activityAnimator.pullTop(this);

        TextView txt_TitleName = (TextView) findViewById(R.id.addTodo_pageName);
        txt_TitleName.setText("Create New Todo");

        addTodo_title = (EditText) findViewById(R.id.addTodo_title);
        addTodo_subTitle = (EditText) findViewById(R.id.addTodo_subTitle);
        addTodo_content = (EditText) findViewById(R.id.addTodo_content);
        start_date = (ImageView) findViewById(R.id.addTodo_start_date);
        end_date = (ImageView) findViewById(R.id.addTodo_end_date);
        backButton = (ImageView) findViewById(R.id.backButton);
        txt_start_date = (TextView) findViewById(R.id.addTodo_startDate);
        txt_end_date = (TextView) findViewById(R.id.addTodo_endDate);

        View.OnClickListener startDateClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(txt_start_date, "startDate", AddToDoActivity.this);
            }
        };

        View.OnClickListener endDateClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(txt_end_date, "endDate", AddToDoActivity.this);
            }
        };

        backButton.setOnClickListener(backButtonClickListener);
        start_date.setOnClickListener(startDateClickListener);
        end_date.setOnClickListener(endDateClickListener);

        saveTodo = (Button) findViewById(R.id.saveTodo);
        saveTodo.setOnClickListener(saveTodoButtonListener);

    }

    private View.OnClickListener backButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AddToDoActivity.this.onBackPressed();
        }
    };

    private View.OnClickListener saveTodoButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String str_title = addTodo_title.getText().toString().trim();
            String str_subTitle = addTodo_subTitle.getText().toString().trim();
            String str_content = addTodo_content.getText().toString().trim();
            String str_startDate = txt_start_date.getText().toString().trim();
            String str_endDate = txt_end_date.getText().toString().trim();


            if (str_title.equals("") || str_subTitle.equals("") || str_content.equals("") || str_startDate.equals("") || str_endDate.equals("")) {
                MessageDialog.showDialogWithoutActions(AddToDoActivity.this, "Please fill empty fields.");
            } else {
                sendAddToDoRequest(str_title, str_subTitle, str_content, startDate_webServiceFormat, endDate_webServiceFormat);
            }
        }
    };

    private void sendAddToDoRequest(final String title, final String subTitle, final String content, final String startDate, final String endDate) {

        final ProgressDialog progressDialog = ProgressDialogTool.createAndShow(AddToDoActivity.this);
        WebServiceBuilder.WebService webService = WebServiceBuilder.getInstance();
        final User user = User.getInstance();
        webService.createTodo(user.getId(), title, subTitle, content, endDate, startDate,
                new Callback<BaseResponse>() {
                    @Override
                    public void success(BaseResponse baseResponse, Response response) {
                        ProgressDialogTool.dismiss(progressDialog);
                        if (baseResponse != null) {
                            if (baseResponse.isSuccess()) {
                                addCurrentUserTodos(title, subTitle, content, startDate, endDate);

                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(AddToDoActivity.this);
                                dlgAlert.setMessage("You saved todo successfully.");
                                dlgAlert.setCancelable(true);
                                dlgAlert.setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                AddToDoActivity.this.onBackPressed();
                                            }
                                        });
                                dlgAlert.create().show();
                            } else {
                                MessageDialog.showDialogWithoutActions(AddToDoActivity.this, baseResponse.getMessage());
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        ProgressDialogTool.dismiss(progressDialog);
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(AddToDoActivity.this);
                        dlgAlert.setMessage("Connection Error");
                        dlgAlert.setCancelable(true);
                        dlgAlert.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        dlgAlert.setNegativeButton("Try Again",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        sendAddToDoRequest(title, subTitle, content, startDate, endDate);
                                    }
                                });
                        dlgAlert.create().show();
                    }
                }
        );
    }

    private void addCurrentUserTodos(String title, String subTitle, String content, String startDate, String endDate) {
        ToDo todo = new ToDo();
        todo.setTitle(title);
        todo.setContent(content);
        todo.setSubTitle(subTitle);
        todo.setStartDate(startDate);
        todo.setEndDate(endDate);
    }

    @Override
    public void onBackPressed() {
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
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void showDateDialog(final TextView dateTxt, final String type, Activity activity) {
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

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

                        if (type.equalsIgnoreCase("startDate")) {
                            String formatted_startDate = formatter.format(dtDob);
                            startDate_webServiceFormat = formatted_startDate;
                        } else if (type.equalsIgnoreCase("endDate")) {
                            String formatted_endDate = formatter.format(dtDob);
                            endDate_webServiceFormat = formatted_endDate;
                        }
                        String DateStr = DateFormat.format("EEEE, d MMMM", dtDob).toString();
                        dateTxt.setText(DateStr);
                    }
                }, today.year, today.month, today.monthDay);
        DatePicker datePicker = dateDlg.getDatePicker();
        datePicker.setCalendarViewShown(false);
        datePicker.setMinDate(System.currentTimeMillis() - 1000);
        dateDlg.show();
    }

}
