package todo.iyzico.com.todoapp.tools;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * Created by emrealkan on 27/08/16.
 */
public class ProgressDialogTool {

    public static ProgressDialog createAndShow(Activity activity) {
        ProgressDialog pg = new ProgressDialog(activity);
        pg.setCancelable(false);
        pg.setMessage("Please wait..");
        pg.show();

        return pg;
    }

    public static void dismiss(ProgressDialog progressDialog) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
