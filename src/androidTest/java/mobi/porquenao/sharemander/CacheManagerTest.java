package mobi.porquenao.sharemander;

import java.io.File;
import java.io.InputStream;

import mobi.porquenao.sharemander.test.BaseInstrumentationTestCase;

import static org.assertj.core.api.Assertions.assertThat;

public class CacheManagerTest extends BaseInstrumentationTestCase {

    private boolean mCalled;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mCalled = false;
    }

    public void testCacheFile() throws Exception {
        File file = File.createTempFile(ActionsDefaults.getFileNamePrefix(), "name", mTargetContext.getCacheDir());
        final int filesCount = getFilesCount();
        assertThat(filesCount).isInstanceOf(Integer.class);
        CacheManager.cacheFile(mTargetContext, file, "name");
        File[] newFilesCount = mTargetContext.getCacheDir().listFiles();
        assertThat(newFilesCount.length).isGreaterThan(filesCount);
    }

    public void testCacheInputStream() throws Exception {
        final int filesCount = getFilesCount();
        assertThat(filesCount).isInstanceOf(Integer.class);
        InputStream inputStream = mTargetContext.getResources().openRawResource(mobi.porquenao.sharemander.test.R.raw.content);
        CacheManager.cacheInputStream(mTargetContext, inputStream, "name");
        File[] newFilesCount = mTargetContext.getCacheDir().listFiles();
        assertThat(newFilesCount.length).isGreaterThan(filesCount);
    }

    public void testClean() throws Exception {
        //noinspection ResultOfMethodCallIgnored
        File.createTempFile(ActionsDefaults.getFileNamePrefix(), "name", mTargetContext.getCacheDir());
        final int filesCount = getFilesCount();
        assertThat(filesCount).isInstanceOf(Integer.class);
        CacheManager.clean(mTargetContext);
        File[] newCacheFiles = mTargetContext.getCacheDir().listFiles();
        assertThat(newCacheFiles.length).isLessThan(filesCount);
    }

    public void testCleanAsync() throws Exception {
        //noinspection ResultOfMethodCallIgnored
        File.createTempFile(ActionsDefaults.getFileNamePrefix(), "name", mTargetContext.getCacheDir());
        final int filesCount = getFilesCount();
        assertThat(filesCount).isInstanceOf(Integer.class);
        CacheManager.cleanAsync(mTargetContext, new Runnable() {
            @Override
            public void run() {
                File[] newCacheFiles = mTargetContext.getCacheDir().listFiles();
                assertThat(newCacheFiles.length).isLessThan(filesCount);
                mCalled = true;
                wake();
            }
        });
        assertThat(mCalled).isFalse();
        wait(5);
        assertThat(mCalled).isTrue();
    }

    private int getFilesCount() {
        return mTargetContext.getCacheDir().listFiles().length;
    }

}
