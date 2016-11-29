package mobi.porquenao.sharemander.send;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.porquenao.sharemander.CustomUriAction;

public class EmailSend extends CustomUriAction {

    private String mType;
    private CharSequence mText;
    private String mSubject;
    private ArrayList<Uri> mUris = new ArrayList<>();

    public CharSequence text() {
        return mText;
    }

    public String subject() {
        return mSubject;
    }

    public String type() {
        return mType;
    }

    public List<Uri> uris() {
        return mUris;
    }

    public EmailSend text(CharSequence text) {
        mText = text;
        return this;
    }

    public EmailSend subject(String subject) {
        mSubject = subject;
        return this;
    }

    public EmailSend uri(@NonNull String type, @NonNull Uri... uris) {
        Collections.addAll(mUris, uris);
        mType = type;
        return this;
    }

    @Override
    public Uri[] getUris() {
        return new Uri[] { Uri.parse("mailto:") };
    }

    @Override
    protected String getAction() {
        return Intent.ACTION_SENDTO;
    }

    @Override
    public Intent getIntent(Intent intent) {
        if (mUris.size() > 0) {
            intent.setAction(Intent.ACTION_SEND);
            intent.removeExtra(Intent.EXTRA_STREAM);
        }
        if (mUris.size() > 1) {
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, mUris);
        } else {
            intent.setAction(Intent.ACTION_SEND);
            if (mUris.size() == 1) {
                intent.putExtra(Intent.EXTRA_STREAM, mUris.get(0));
            }
        }
        if (mText != null) {
            intent.putExtra(Intent.EXTRA_TEXT, mText);
        }
        if (mSubject != null) {
            intent.putExtra(Intent.EXTRA_SUBJECT, mSubject);
        }
        if (mType != null) {
            intent.setType(mType);
        }
        return intent;
    }

}
