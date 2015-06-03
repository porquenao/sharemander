package mobi.porquenao.andaction.type;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Parcelable;

import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import mobi.porquenao.andaction.ActionsDefaults;
import mobi.porquenao.andaction.CustomPackageAction;
import mobi.porquenao.andaction.test.BaseInstrumentationTestCase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ActionSendTest extends BaseInstrumentationTestCase {

    @Mock private Activity mActivity;
    @Mock private PackageManager mPackageManager;

    private ActionSend mActionSend;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ActionsDefaults.clearBlacklist();
        mActionSend = new ActionSend(mActivity);
        when(mActivity.getPackageManager()).thenReturn(mPackageManager);
    }

    public void testType() {
        assertThat(mActionSend.type()).isNotNull();
        mActionSend.type("text/plain");
        assertThat(mActionSend.type()).isEqualTo("text/plain");
    }

    public void testText() {
        assertThat(mActionSend.text()).isNull();
        mActionSend.text("text");
        assertThat(mActionSend.text()).isEqualTo("text");
    }

    public void testSubject() {
        assertThat(mActionSend.subject()).isNull();
        mActionSend.subject("subject");
        assertThat(mActionSend.subject()).isEqualTo("subject");
    }

    public void testUris() {
        assertThat(mActionSend.uris()).isEmpty();
        Uri uri = Uri.parse("http://porquenao.mobi");
        mActionSend.uri("text/plain", uri);
        assertThat(mActionSend.uris()).isNotEmpty();
        assertThat(mActionSend.uris().get(0)).isEqualTo(uri);
    }

    public void testActions() {
        assertThat(mActionSend.actions()).isEmpty();
        CustomPackageAction customPackageAction = new CustomPackageAction() {
            @Override
            public List<String> getPackages(Context context) {
                return null;
            }

            @Override
            public Intent getIntent(Intent intent) {
                return null;
            }
        };
        mActionSend.customize(customPackageAction);
        assertThat(mActionSend.actions()).isNotEmpty();
        assertThat(mActionSend.actions().get(0)).isEqualTo(customPackageAction);
    }

    public void testBuildIntent() {
        Intent intent = mActionSend.buildIntent();
        matchIntent(intent, Intent.ACTION_SEND, mActionSend.type(), null, null);

        mActionSend.type("type");
        intent = mActionSend.buildIntent(intent);
        matchIntent(intent, Intent.ACTION_SEND, "type", null, null);

        mActionSend.subject("subject");
        intent = mActionSend.buildIntent(intent);
        matchIntent(intent, Intent.ACTION_SEND, "type", "subject", null);

        mActionSend.text("text");
        intent = mActionSend.buildIntent(intent);
        matchIntent(intent, Intent.ACTION_SEND, "type", "subject", "text");
    }

    public void testBuildIntent_Multiple() {
        Uri uri = mock(Uri.class);
        mActionSend.uri("text/plain", uri, uri);
        Intent intent = mActionSend.buildIntent();
        matchIntent(intent, Intent.ACTION_SEND_MULTIPLE, "text/plain", null, null);
    }

    public void testGetFilteredIntents() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        mockPackageManagerIntents(mPackageManager, 3);
        assertThat(mActionSend.getFilteredIntents(intent).size()).isEqualTo(3);
    }

    public void testGetFilteredIntents_Blacklist() {
        ActionsDefaults.addBlacklist("mobi.porquenao.andaction0");
        ActionsDefaults.addBlacklist("mobi.porquenao.andaction1");
        Intent intent = new Intent(Intent.ACTION_SEND);
        mockPackageManagerIntents(mPackageManager, 3);
        assertThat(mActionSend.getFilteredIntents(intent).size()).isEqualTo(1);
    }

    public void testGetFilteredIntents_Customize() {
        mockPackageManagerIntents(mPackageManager, 1);
        mActionSend.customize(new CustomPackageAction() {
            @Override
            public List<String> getPackages(Context context) {
                return new ArrayList<String>() {{ add("mobi.porquenao.andaction0"); }};
            }

            @Override
            public Intent getIntent(Intent intent) {
                return intent.putExtra(Intent.EXTRA_SUBJECT, "Custom Subject");
            }
        });
        List<Intent> intents = mActionSend.getFilteredIntents(mActionSend.buildIntent());
        Intent intent = intents.get(0);
        assertThat(intent).isNotNull();
        assertThat(intent.getStringExtra(Intent.EXTRA_SUBJECT)).isEqualTo("Custom Subject");
    }

    public void testGetChooserIntent() {
        final Intent intent1 = new Intent("action1");
        final Intent intent2 = new Intent("action2");
        final Intent intent3 = new Intent("action3");
        List<Intent> intents = new ArrayList<Intent>() {{
            add(intent1);
            add(intent2);
            add(intent3);
        }};
        Intent intent = mActionSend.getChooserIntent(intents);
        assertThat(intent.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION).isGreaterThan(0);
        Parcelable[] parcelables = intent.getParcelableArrayExtra(Intent.EXTRA_INITIAL_INTENTS);
        assertThat(intent.getParcelableExtra(Intent.EXTRA_INTENT)).isEqualTo(intent3);
        assertThat(parcelables.length).isEqualTo(2);
    }

    public void testShow() {
        mockPackageManagerIntents(mPackageManager, 2);
        mActionSend.show();
        verify(mActivity, atLeastOnce()).startActivityForResult(any(Intent.class), anyInt());
    }

    private void matchIntent(Intent intent, String action, String type, String subject, String text) {
        assertThat(intent.getAction()).isEqualTo(action);
        assertThat(intent.getType()).isEqualTo(type);
        assertThat(intent.getStringExtra(Intent.EXTRA_SUBJECT)).isEqualTo(subject);
        assertThat(intent.getStringExtra(Intent.EXTRA_TEXT)).isEqualTo(text);
    }

}
