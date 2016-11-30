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
        File file = createTempFile();
        int filesCount = getFilesCount();
        CacheManager.cacheFile(mTargetContext, file, "name");
        assertThat(getFilesCount()).isGreaterThan(filesCount);
    }

    public void testCacheInputStream() throws Exception {
        int filesCount = getFilesCount();
        InputStream inputStream = mTargetContext.getResources().openRawResource(mobi.porquenao.sharemander.test.R.raw.content);
        CacheManager.cacheInputStream(mTargetContext, inputStream, "name");
        assertThat(getFilesCount()).isGreaterThan(filesCount);
    }

    public void testClean() throws Exception {
        createTempFile();
        int filesCount = getFilesCount();
        CacheManager.clean(mTargetContext);
        assertThat(getFilesCount()).isLessThan(filesCount);
    }

    public void testCleanAsync() throws Exception {
        createTempFile();
        final int filesCount = getFilesCount();
        CacheManager.cleanAsync(mTargetContext, new Runnable() {
            @Override
            public void run() {
                assertThat(getFilesCount()).isLessThan(filesCount);
                mCalled = true;
                wake();
            }
        });
        assertThat(mCalled).isFalse();
        wait(5);
        assertThat(mCalled).isTrue();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private File createTempFile() throws Exception {
        File cacheDir = new File(mTargetContext.getCacheDir(), "sharemander");
        cacheDir.mkdirs();
        return File.createTempFile(ActionsDefaults.getFileNamePrefix(), "name", cacheDir);
    }

    private int getFilesCount() {
        return new File(mTargetContext.getCacheDir(), "sharemander").listFiles().length;
    }

}
