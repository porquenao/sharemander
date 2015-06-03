package mobi.porquenao.andaction;

import android.app.Activity;

import mobi.porquenao.andaction.type.ActionSend;

public abstract class Actions {

    public static Builder with(Activity activity) {
        return new Builder(activity);
    }

    public static class Builder {

        private Activity mActivity;

        public Builder(Activity activity) {
            mActivity = activity;
        }

        public ActionSend send() {
            return new ActionSend(mActivity);
        }

    }

}
