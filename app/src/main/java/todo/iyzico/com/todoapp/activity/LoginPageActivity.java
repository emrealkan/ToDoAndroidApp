package todo.iyzico.com.todoapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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


/**
 * Created by emrealkan on 25/08/16.
 */
public class LoginPageActivity extends Activity {

    private Button loginButton, signUpButton;
    private EditText username, password;

    private Dialog dialog_forgotPassword, dialog_anonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        ActivityAnimator activityAnimator = new ActivityAnimator();
        activityAnimator.PullRightPushLeft(this);

        loginButton = (Button) findViewById(R.id.loginButton);

        username = (EditText) findViewById(R.id.userNameText);
        password = (EditText) findViewById(R.id.passwordText);
        signUpButton = (Button) findViewById(R.id.btn_signUp);

        loginButton.setOnClickListener(loginButtonListener);

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    loginButtonListener.onClick(v);
                    handled = true;
                }
                return handled;
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignUpPage();
            }
        });

    }


    private void openSignUpPage() {
        Intent intent = new Intent(LoginPageActivity.this, SignUpPageActivity.class);
        startActivity(intent);
    }


    private View.OnClickListener loginButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String str_username = username.getText().toString().trim();
            String str_password = password.getText().toString().trim();

            if (str_username.equals("") || str_password.equals("")) {
                MessageDialog.showDialogWithoutActions(LoginPageActivity.this, "Please fill all fields.");
            } else {
                sendLoginRequest(str_username, str_password);
            }
        }
    };

    private void sendLoginRequest(final String username, final String password) {

        final ProgressDialog progressDialog = ProgressDialogTool.createAndShow(LoginPageActivity.this);
        WebServiceBuilder.WebService webService = WebServiceBuilder.getInstance();
        webService.login(username, password,
                new Callback<BaseResponse<User>>() {
                    @Override
                    public void success(BaseResponse<User> baseResponse, Response response) {
                        ProgressDialogTool.dismiss(progressDialog);
                        if (baseResponse != null) {
                            if (baseResponse.isSuccess()) {
                                User.setUser((User) baseResponse.getData());
                                startActivity(new Intent(LoginPageActivity.this, MainPageActivity.class));
                                finish();
                            } else {
                                MessageDialog.showDialogWithoutActions(LoginPageActivity.this, baseResponse.getMessage());
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        ProgressDialogTool.dismiss(progressDialog);
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginPageActivity.this);
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
                                        sendLoginRequest(username, password);
                                    }
                                });
                        dlgAlert.create().show();
                    }
                }
        );
    }

}
