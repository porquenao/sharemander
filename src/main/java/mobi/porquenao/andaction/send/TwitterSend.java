package mobi.porquenao.andaction.send;

import android.content.Intent;
import android.net.Uri;

import mobi.porquenao.andaction.CustomUriAction;

public class TwitterSend extends CustomUriAction {

    private String mText;
    private Uri mUri;

    public String text() {
        return mText;
    }

    public Uri uri() {
        return mUri;
    }

    public TwitterSend text(String text) {
        mText = text;
        return this;
    }

    public TwitterSend uri(Uri uri) {
        mUri = uri;
        return this;
    }

    @Override
    public Uri[] getUris() {
        return new Uri[] { Uri.parse("http://twitter.com/") };
    }

    @Override
    public Intent getIntent(Intent intent) {
        if (mText != null) {
            intent.putExtra(Intent.EXTRA_TEXT, mText);
        }
        if (mUri != null) {
            intent.putExtra(Intent.EXTRA_STREAM, mUri);
        }
        return intent;
    }

}
