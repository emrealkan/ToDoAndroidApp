package todo.iyzico.com.todoapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import todo.iyzico.com.todoapp.tools.ActivityAnimator;
import todo.iyzico.com.todoapp.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * Created by emrealkan on 25/08/16.
 */
public class LoginPageActivity extends Activity {

    public final static int KILL_LOGIN_PAGE = 99, REQUEST_CODE = 98;

    private Button loginButton, signUpButton;
    private EditText username, password;
    private TextView txt_result_message;
   // private User user;

    private Dialog dialog_forgotPassword, dialog_anonLogin;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Gotham-Rounded-Book_21018.ttf")
                .setFontAttrId(R.attr.fontPath).build());

        ActivityAnimator activityAnimator = new ActivityAnimator();
        activityAnimator.PullRightPushLeft(this);

        loginButton = (Button) findViewById(R.id.loginButton);

        username = (EditText) findViewById(R.id.userNameText);
        password = (EditText) findViewById(R.id.passwordText);
        txt_result_message = (TextView) findViewById(R.id.login_txt_result_message);
        signUpButton = (Button) findViewById(R.id.btn_signUp);

        findViewById(R.id.login_layout_root).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard(LoginPageActivity.this);
                return false;
            }
        });

        loginButton.setOnClickListener(loginButtonListener);

        password.setOnEditorActionListener(new TextView.OnEditorActionListener(){
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
        //startActivity(intent);
        startActivityForResult(intent, REQUEST_CODE);
    }


    private View.OnClickListener loginButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            loginButton.setClickable(false);
            openMainPage();
            String str_username = username.getText().toString().trim();
            String str_password = password.getText().toString().trim();

            if (str_username.equals("") || str_password.equals("")) {
                //txt_result_message.setText(getResources().getString(R.string.login_empty_message));
                loginButton.setClickable(true);
            }
            else {
                txt_result_message.setText("");
                //new LongRunningGetIO().execute(str_username, str_password);
            }

            hideSoftKeyboard(LoginPageActivity.this);
        }
    };

    private static void hideSoftKeyboard(Activity activity) {
        if(activity != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if(inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    /*private void saveAccesToken() {
        if (!user.getAccessToken().equals("")) {
            SavedDatas.saveToPreferences(this, SplashActivity.isLogged, true);
            SavedDatas.saveToPreferences(this, SplashActivity.savedAccessToken, user.getAccessToken());
        }
    }*/

    private void openMainPage()
    {
        //User user = User.getInstance();

        Intent intent = new Intent(LoginPageActivity.this, MainActivity.class);
        startActivity(intent);

        /*if(user.getFirstName() != null) {
            saveAccesToken();
        }*/

        finish();
    }


    private void showAlertDialog(String message, boolean showRetryButton, final String email) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(LoginPageActivity.this);
        dlgAlert.setMessage(message);
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton(R.string.ok_text,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();

                    }
                });

        if(showRetryButton) {
            dlgAlert.setNegativeButton(R.string.retry_text, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                   // new RequestForForgotPassword().execute(email);
                }
            });
        }
        dlgAlert.create().show();
    }

}
