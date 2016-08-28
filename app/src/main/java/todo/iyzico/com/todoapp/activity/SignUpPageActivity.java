package todo.iyzico.com.todoapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import todo.iyzico.com.todoapp.R;
import todo.iyzico.com.todoapp.models.BaseResponse;
import todo.iyzico.com.todoapp.models.User;
import todo.iyzico.com.todoapp.tools.ActivityAnimator;
import todo.iyzico.com.todoapp.tools.MessageDialog;
import todo.iyzico.com.todoapp.tools.ProgressDialogTool;
import todo.iyzico.com.todoapp.webservice.WebServiceBuilder;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by emrealkan on 25/08/16.
 */
public class SignUpPageActivity extends Activity {

    private Button signUp;
    private EditText email, password, username;
    private ActivityAnimator activityAnimator;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Gotham-Rounded-Book_21018.ttf")
                .setFontAttrId(R.attr.fontPath).build());

        activityAnimator = new ActivityAnimator();
        activityAnimator.flipHorizontalAnimation(this);

        signUp = (Button) findViewById(R.id.signup_btn_signUp);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        username = (EditText) findViewById(R.id.name);

        signUp.setOnClickListener(signUpButtonListener);

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    signUpButtonListener.onClick(v);
                    handled = true;
                }
                return handled;
            }
        });
    }

    View.OnClickListener signUpButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String str_email = email.getText().toString().trim();
            String str_password = password.getText().toString();
            String str_username = username.getText().toString().trim();

            if (str_email.equals("") || str_password.equals("") || str_username.equals("")) {
                MessageDialog.showDialogWithoutActions(SignUpPageActivity.this, "Please fill empty fields");
            } else {
                sendSignUpRequest(str_username, str_password, str_email);
            }
        }
    };

    private void sendSignUpRequest(final String username, final String password, final String email) {
        final ProgressDialog progressDialog = ProgressDialogTool.createAndShow(SignUpPageActivity.this);
        WebServiceBuilder.WebService webService = WebServiceBuilder.getInstance();
        webService.signup(username, password, email,
                new Callback<BaseResponse<User>>() {
                    @Override
                    public void success(final BaseResponse baseResponse, Response response) {
                        ProgressDialogTool.dismiss(progressDialog);
                        if (baseResponse != null) {
                            if (baseResponse.isSuccess()) {
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(SignUpPageActivity.this);
                                dlgAlert.setMessage("You signed in successfully.");
                                dlgAlert.setCancelable(true);
                                dlgAlert.setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                User.setUser((User) baseResponse.getData());
                                                startActivity(new Intent(SignUpPageActivity.this, MainPageActivity.class));
                                            }
                                        });
                                dlgAlert.create().show();
                            } else {
                                MessageDialog.showDialogWithoutActions(SignUpPageActivity.this, baseResponse.getMessage());
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        ProgressDialogTool.dismiss(progressDialog);
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(SignUpPageActivity.this);
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
                                        sendSignUpRequest(username, password, email);
                                    }
                                });
                        dlgAlert.create().show();
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        activityAnimator.flipHorizontalAnimation(this);
    }


}
