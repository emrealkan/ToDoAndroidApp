package todo.iyzico.com.todoapp.activity;

import android.app.Activity;
import android.content.Context;
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
public class SignUpPageActivity extends Activity{

    private Button signUp;
    private EditText email, password, name;
    private TextView result;

    private ActivityAnimator activityAnimator;
    //private User user;

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
        name = (EditText) findViewById(R.id.name);
        result = (TextView) findViewById(R.id.resultSignUp);

        findViewById(R.id.signup_layout_root).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard(SignUpPageActivity.this);
                return false;
            }
        });

        signUp.setOnClickListener(signUpButtonListener);

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO)
                {
                    signUpButtonListener.onClick(v);
                    handled = true;
                }
                return handled;
            }
        });
    }

    View.OnClickListener signUpButtonListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            signUp.setClickable(false);

            String str_email = email.getText().toString().trim();
            String str_password = password.getText().toString();
            String str_name = name.getText().toString().trim();

            if(str_email.equals("") || str_password.equals("") || str_name.equals("")) {
                result.setText(getResources().getString(R.string.login_empty_message));
                signUp.setClickable(true);
            }
            else {
                result.setText("");
                //new LongRunningGetIO().execute(str_email, str_password, str_name, str_surname, str_phoneNumber);
            }
            hideSoftKeyboard(SignUpPageActivity.this);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //animate the slide back navigation
        activityAnimator.flipHorizontalAnimation(this);
    }

    private static void hideSoftKeyboard(Activity activity) {
        if(activity != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if(inputMethodManager != null)
            {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    private void openMainPage() {
        Intent intent = new Intent(SignUpPageActivity.this, MainActivity.class);
        startActivity(intent);

        //saveAccesToken();

        setResult(LoginPageActivity.KILL_LOGIN_PAGE);
        finish();
    }

}
