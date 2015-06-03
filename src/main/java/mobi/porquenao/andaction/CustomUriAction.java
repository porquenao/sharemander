package mobi.porquenao.andaction;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomUriAction implements CustomPackageAction {

    public abstract Uri[] getUris();

    @Override
    public List<String> getPackages(Context context) {
        List<String> packages = getPackagesByActionAndUris(context, getAction(), getUris());
        if (shouldIgnoreBrowsers()) {
            Uri[] uris = new Uri[] { Uri.parse("http:") };
            packages.removeAll(getPackagesByActionAndUris(context, Intent.ACTION_VIEW, uris));
        }
        return packages;
    }

    protected boolean shouldIgnoreBrowsers() {
        return true;
    }

    protected String getAction() {
        return Intent.ACTION_VIEW;
    }

    public static List<String> getPackagesByActionAndUris(Context context, String action, Uri[] uris) {
        List<String> packages = new ArrayList<>();
        Intent intent;
        String packageName;
        List<ResolveInfo> resolveInfos;
        for (Uri uri : uris) {
            intent = new Intent(action, uri);
            resolveInfos = context.getPackageManager().queryIntentActivities(intent, 0);
            for (ResolveInfo resolveInfo : resolveInfos) {
                packageName = resolveInfo.activityInfo.packageName;
                if (!packages.contains(packageName)) {
                    packages.add(packageName);
                }
            }
        }
        return packages;
    }

}
