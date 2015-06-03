package mobi.porquenao.andaction.send;

import android.content.Intent;
import android.net.Uri;

import mobi.porquenao.andaction.test.BaseInstrumentationTestCase;

import static org.assertj.core.api.Assertions.assertThat;

public class TwitterSendTest extends BaseInstrumentationTestCase {

    private TwitterSend mTwitterSend;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mTwitterSend = new TwitterSend();
    }

    public void testText() {
        assertThat(mTwitterSend.text()).isNull();
        mTwitterSend.text("text");
        assertThat(mTwitterSend.text()).isEqualTo("text");
    }

    public void testUri() {
        assertThat(mTwitterSend.uri()).isNull();
        Uri uri = Uri.parse("http://porquenao.mobi");
        mTwitterSend.uri(uri);
        assertThat(mTwitterSend.uri()).isEqualTo(uri);
    }

    public void testGetUris() {
        assertThat(mTwitterSend.getUris()).isNotEmpty();
    }

    public void testGetIntent_Text() {
        Uri uri = Uri.parse("http://porquenao.mobi");
        Intent intent = mTwitterSend.text("text").uri(uri).getIntent(new Intent());
        assertThat(intent.getStringExtra(Intent.EXTRA_TEXT)).isEqualTo("text");
        assertThat(intent.getParcelableExtra(Intent.EXTRA_STREAM)).isEqualTo(uri);
    }

    private void matchIntent(Intent intent, String action, String type, String subject, String text) {
        assertThat(intent.getAction()).isEqualTo(action);
        assertThat(intent.getType()).isEqualTo(type);
        assertThat(intent.getStringExtra(Intent.EXTRA_SUBJECT)).isEqualTo(subject);
        assertThat(intent.getStringExtra(Intent.EXTRA_TEXT)).isEqualTo(text);
    }

}
