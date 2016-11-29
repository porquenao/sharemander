package mobi.porquenao.sharemander.test;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.test.InstrumentationTestCase;

import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

public class BaseInstrumentationTestCase extends InstrumentationTestCase {

    protected Context mContext;
    protected Context mTargetContext;

    private CountDownLatch mSignal = new CountDownLatch(1);
    private boolean mAwake;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getInstrumentation().getContext();
        mTargetContext = getInstrumentation().getTargetContext();
        System.setProperty("dexmaker.dexcache", mContext.getCacheDir().getPath());
        MockitoAnnotations.initMocks(this);
    }

    protected void wait(int seconds) throws Exception {
        mSignal.await(seconds, TimeUnit.SECONDS);
        assertTrue("Should have a callback return.", mAwake);
    }

    protected void wake() {
        mAwake = true;
        mSignal.countDown();
    }

    protected void mockPackageManagerIntents(PackageManager packageManager, int amount) {
        List<ResolveInfo> resolveInfos = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            ActivityInfo activityInfo = new ActivityInfo();
            activityInfo.packageName = "mobi.porquenao.sharemander" + i;
            activityInfo.name = "Sharemander-Normal-" + i;
            activityInfo.nonLocalizedLabel = activityInfo.name;

            ResolveInfo resolveInfo = new ResolveInfo();
            resolveInfo.activityInfo = activityInfo;
            resolveInfos.add(resolveInfo);
        }
        when(packageManager.queryIntentActivities(any(Intent.class), anyInt())).thenReturn(resolveInfos);
    }

}
