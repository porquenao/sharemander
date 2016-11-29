package mobi.porquenao.sharemander.send;

import android.content.Intent;
import android.net.Uri;

import mobi.porquenao.sharemander.test.BaseInstrumentationTestCase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class EmailSendTest extends BaseInstrumentationTestCase {

    private EmailSend mEmailSend;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mEmailSend = new EmailSend();
    }

    public void testText() {
        assertThat(mEmailSend.text()).isNull();
        mEmailSend.text("text");
        assertThat(mEmailSend.text()).isEqualTo("text");
    }

    public void testSubject() {
        assertThat(mEmailSend.subject()).isNull();
        mEmailSend.subject("subject");
        assertThat(mEmailSend.subject()).isEqualTo("subject");
    }

    public void testUris() {
        assertThat(mEmailSend.type()).isNull();
        assertThat(mEmailSend.uris()).isEmpty();
        Uri uri = Uri.parse("http://porquenao.mobi");
        mEmailSend.uri("text/plain", uri);
        assertThat(mEmailSend.type()).isEqualTo("text/plain");
        assertThat(mEmailSend.uris().get(0)).isEqualTo(uri);
    }

    public void testGetUris() {
        assertThat(mEmailSend.getUris()).isNotEmpty();
    }

    public void testGetAction() {
        assertThat(mEmailSend.getAction()).isEqualTo(Intent.ACTION_SENDTO);
    }

    public void testGetIntent() {
        Intent intent = mEmailSend.getIntent(new Intent());
        matchIntent(intent, Intent.ACTION_SEND, mEmailSend.type(), null, null);

        intent = mEmailSend.getIntent(intent);
        matchIntent(intent, Intent.ACTION_SEND, null, null, null);

        mEmailSend.subject("subject");
        intent = mEmailSend.getIntent(intent);
        matchIntent(intent, Intent.ACTION_SEND, null, "subject", null);

        mEmailSend.text("text");
        intent = mEmailSend.getIntent(intent);
        matchIntent(intent, Intent.ACTION_SEND, null, "subject", "text");
    }

    public void testGetIntent_Uris() {
        Intent intent = new Intent();

        Uri uri1 = mock(Uri.class);
        mEmailSend.uri("text/plain", uri1);
        intent = mEmailSend.getIntent(intent);
        matchIntent(intent, Intent.ACTION_SEND, "text/plain", null, null);
        assertThat(intent.getParcelableExtra(Intent.EXTRA_STREAM)).isEqualTo(uri1);

        Uri uri2 = mock(Uri.class);
        mEmailSend.uri("text/plain", uri2);
        intent = mEmailSend.getIntent(intent);
        matchIntent(intent, Intent.ACTION_SEND_MULTIPLE, "text/plain", null, null);
        assertThat(intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM).get(1)).isEqualTo(uri2);
    }

    private void matchIntent(Intent intent, String action, String type, String subject, String text) {
        assertThat(intent.getAction()).isEqualTo(action);
        assertThat(intent.getType()).isEqualTo(type);
        assertThat(intent.getStringExtra(Intent.EXTRA_SUBJECT)).isEqualTo(subject);
        assertThat(intent.getStringExtra(Intent.EXTRA_TEXT)).isEqualTo(text);
    }

}
