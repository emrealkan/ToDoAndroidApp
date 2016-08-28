package todo.iyzico.com.todoapp.tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by emrealkan on 27/08/16.
 */
public class MessageDialog {

    public static void showDialogWithoutActions(Activity activity, String message) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(activity);
        dlgAlert.setMessage(message);
        dlgAlert.setCancelable(true);
        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        dlgAlert.create().show();
    }

}
