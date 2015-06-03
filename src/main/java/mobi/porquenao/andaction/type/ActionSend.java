package mobi.porquenao.andaction.type;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import mobi.porquenao.andaction.ActionsDefaults;
import mobi.porquenao.andaction.CustomPackageAction;

public class ActionSend {

    private Activity mActivity;

    private String mType = "text/plain";
    private String mText;
    private String mSubject;
    private ArrayList<Uri> mUris = new ArrayList<>();

    private ArrayList<CustomPackageAction> mCustomPackageActions = new ArrayList<>();

    public ActionSend(Activity activity) {
        mActivity = activity;
    }

    public String type() {
        return mType;
    }

    public String text() {
        return mText;
    }

    public String subject() {
        return mSubject;
    }

    public ArrayList<Uri> uris() {
        return mUris;
    }

    public ArrayList<CustomPackageAction> actions() {
        return mCustomPackageActions;
    }

    public ActionSend type(@NonNull String type) {
        mType = type;
        return this;
    }

    public ActionSend text(String text) {
        mText = text;
        return this;
    }

    public ActionSend subject(String subject) {
        mSubject = subject;
        return this;
    }

    public ActionSend uri(@NonNull String type, @NonNull Uri... uris) {
        Collections.addAll(mUris, uris);
        mType = type;
        return this;

    }

    public ActionSend customize(@NonNull CustomPackageAction customPackageAction) {
        mCustomPackageActions.add(customPackageAction);
        return this;
    }

    public Intent buildIntent() {
        return buildIntent(new Intent());
    }

    public Intent buildIntent(@NonNull Intent intent) {
        if (mUris.size() > 1) {
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, mUris);
        } else {
            intent.setAction(Intent.ACTION_SEND);
            if (mUris.size() == 1) {
                intent.putExtra(Intent.EXTRA_STREAM, mUris.get(0));
            }
        }
        intent.putExtra(Intent.EXTRA_TEXT, mText);
        intent.putExtra(Intent.EXTRA_SUBJECT, mSubject);
        intent.setType(mType);
        return intent;
    }

    public List<Intent> getFilteredIntents(Intent intent) {
        HashMap<CharSequence, Intent> intentMap = new HashMap<>();

        List<ResolveInfo> resolveInfos = mActivity.getPackageManager().queryIntentActivities(intent, 0);

        for (ResolveInfo resolveInfo : resolveInfos) {
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            if (ActionsDefaults.getBlacklist().contains(activityInfo.packageName)) {
                continue;
            }

            intent = new Intent();
            intent.setPackage(activityInfo.packageName);
            intent.setComponent(new ComponentName(activityInfo.packageName, activityInfo.name));
            intent = buildIntent(intent);

            for (CustomPackageAction customPackageAction : mCustomPackageActions) {
                for (String packageName : customPackageAction.getPackages(mActivity)) {
                    if (activityInfo.packageName.equals(packageName)) {
                        intent = customPackageAction.getIntent(intent);
                    }
                }
            }

            intentMap.put(activityInfo.loadLabel(mActivity.getPackageManager()), intent);
        }

        List<Intent> intents = new ArrayList<>();
        SortedSet<CharSequence> keys = new TreeSet<>(intentMap.keySet());
        for (CharSequence key : keys) {
            intents.add(intentMap.get(key));
        }
        return intents;
    }

    public Intent getChooserIntent(List<Intent> intents) {
        Intent intent = Intent.createChooser(intents.remove(intents.size() - 1), ActionsDefaults.getSendDialogTitle());
        intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents.toArray(new Parcelable[intents.size()]));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }

    public void show() {
        Intent intent = buildIntent(new Intent());
        List<Intent> filteredIntents = getFilteredIntents(intent);
        if (!filteredIntents.isEmpty()) {
            Intent chooserIntent = getChooserIntent(filteredIntents);
            mActivity.startActivityForResult(chooserIntent, 0);
        }
    }

}
