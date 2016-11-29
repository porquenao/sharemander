package mobi.porquenao.sharemander;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import org.mockito.Mock;

import java.util.List;

import mobi.porquenao.sharemander.test.BaseInstrumentationTestCase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class CustomUriActionTest extends BaseInstrumentationTestCase {

    @Mock private Activity mActivity;
    @Mock private PackageManager mPackageManager;

    private CustomUriAction mCustomUriAction;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mCustomUriAction = new FakeCustomUriAction();
        when(mActivity.getPackageManager()).thenReturn(mPackageManager);
    }

    public void testGetPackages() {
        mockPackageManagerIntents(mPackageManager, 1);
        assertThat(mCustomUriAction.getPackages(mActivity)).isNotEmpty();
    }

    public void testShouldIgnoreBrowsers() {
        assertThat(mCustomUriAction.shouldIgnoreBrowsers()).isNotNull();
    }

    public void testGetAction() {
        assertThat(mCustomUriAction.getAction()).isNotEmpty();
    }

    public void testGetPackagesByActionAndUris() {
        mockPackageManagerIntents(mPackageManager, 1);
        Uri[] uris = new Uri[] { Uri.parse("mailto:") };
        List<String> packages = CustomUriAction.getPackagesByActionAndUris(mActivity, Intent.ACTION_SEND, uris);
        assertThat(packages).isNotEmpty();
    }

    private class FakeCustomUriAction extends CustomUriAction {

        @Override
        public Uri[] getUris() {
            return new Uri[] { Uri.parse("mailto:") };
        }

        @Override
        protected boolean shouldIgnoreBrowsers() {
            return false;
        }

        @Override
        public Intent getIntent(Intent intent) {
            intent.putExtra(Intent.EXTRA_TEXT, "text");
            return intent;
        }

    }

}
