package todo.iyzico.com.todoapp.tools;

/**
 * Created by emrealkan on 25/08/16.
 */

import android.app.Activity;

import todo.iyzico.com.todoapp.R;

public class ActivityAnimator {
    public void PullRightPushLeft(Activity a) {
        a.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    public void PullLeftPushRight(Activity a) {
        a.overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    public void flipHorizontalAnimation(Activity a) {
        a.overridePendingTransition(R.anim.flip_horizontal_in, R.anim.flip_horizontal_out);
    }

    public void flipVerticalAnimation(Activity a) {
        a.overridePendingTransition(R.anim.flip_vertical_in, R.anim.flip_vertical_out);
    }

    public void fadeAnimation(Activity a) {
        a.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void unzoomAnimation(Activity a) {
        a.overridePendingTransition(R.anim.unzoom_in, R.anim.unzoom_out);
    }

    public void pullTop(Activity a) {
        a.overridePendingTransition(R.anim.pull_in_top, R.anim.not_moving);
    }

    public void pullBottom(Activity a) {
        a.overridePendingTransition(R.anim.not_moving, R.anim.pull_in_bottom);
    }
}

