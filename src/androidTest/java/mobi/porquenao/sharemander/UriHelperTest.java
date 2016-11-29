package mobi.porquenao.sharemander;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import mobi.porquenao.sharemander.test.BaseInstrumentationTestCase;

import static org.assertj.core.api.Assertions.assertThat;

public class UriHelperTest extends BaseInstrumentationTestCase {

    private static final String NAME = "NAME";
    private static final String CONTENT = "Content.\n";
    private static final int DRAWABLE = mobi.porquenao.sharemander.test.R.drawable.black;
    private static final int RAW = mobi.porquenao.sharemander.test.R.raw.content;

    public void testFile() throws Exception {
        File file = createFile(mTargetContext);
        Uri uri = UriHelper.file(mContext, NAME, file);
        String content = readUri(mContext, uri);
        assertThat(content).isEqualTo(CONTENT);
    }

    public void testUri() throws Exception {
        String packageName = mTargetContext.getPackageName();
        Uri uri = Uri.parse("android.resource://" + packageName + "/" + mobi.porquenao.sharemander.test.R.raw.content);
        String content = readUri(mContext, uri);
        assertThat(content).isEqualTo(CONTENT);
    }

    public void testStream() throws Exception {
        InputStream inputStream = mTargetContext.getResources().openRawResource(RAW);
        Uri uri = UriHelper.stream(mTargetContext, NAME, inputStream);
        String content = readUri(mTargetContext, uri);
        assertThat(content).isEqualTo(CONTENT);
    }

    public void testAssets() throws Exception {
        Uri uri = UriHelper.asset(mTargetContext, NAME, "content.txt");
        String content = readUri(mTargetContext, uri);
        assertThat(content).isEqualTo(CONTENT);
    }

    public void testRaw() throws Exception {
        Uri uri = UriHelper.raw(mTargetContext, NAME, RAW);
        String content = readUri(mTargetContext, uri);
        assertThat(content).isEqualTo(CONTENT);
    }

    public void testBytes() throws Exception {
        byte[] bytes = readBytes(mTargetContext.getResources().openRawResource(RAW));
        Uri uri = UriHelper.bytes(mTargetContext, NAME, bytes);
        String content = readUri(mTargetContext, uri);
        assertThat(content).isEqualTo(CONTENT);
    }

    public void testBitmap() throws Exception {
        Bitmap bitmap = createBitmap();
        Uri uri = UriHelper.bitmap(mTargetContext, NAME, bitmap);
        assertThat(readFirstPixelColor(mTargetContext, uri)).isEqualTo(Color.BLACK);
    }

    public void testBitmapQuality() throws Exception {
        Bitmap bitmap = createBitmap();
        Uri uri = UriHelper.bitmap(mTargetContext, NAME, 50, bitmap);
        assertThat(readFirstPixelColor(mTargetContext, uri)).isEqualTo(Color.BLACK);
    }

    public void testBitmapFormatQuality() throws Exception {
        Bitmap bitmap = createBitmap();
        Uri uri = UriHelper.bitmap(mTargetContext, NAME, Bitmap.CompressFormat.JPEG, 50, bitmap);
        assertThat(readFirstPixelColor(mTargetContext, uri)).isEqualTo(Color.BLACK);
    }

    public void testDrawable() throws Exception {
        Uri uri = UriHelper.drawable(mTargetContext, NAME, DRAWABLE);
        assertThat(readFirstPixelColor(mTargetContext, uri)).isEqualTo(Color.BLACK);
    }

    // Helper

    private int readFirstPixelColor(Context context, Uri uri) throws Exception {
        assertThat(uri).isNotNull();
        Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        return bitmap.getPixel(0, 0);
    }

    private String readUri(Context context, Uri uri) throws Exception {
        assertThat(uri).isNotNull();
        return readInputStream(context.getContentResolver().openInputStream(uri));
    }

    private String readInputStream(InputStream is) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }

    private byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private Bitmap createBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.BLACK);
        return bitmap;
    }

    private File createFile(Context context) throws IOException {
        File file = File.createTempFile(ActionsDefaults.getFileNamePrefix(), "content.txt", context.getCacheDir());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        byte[] contentInBytes = CONTENT.getBytes();
        fileOutputStream.write(contentInBytes);
        fileOutputStream.flush();
        fileOutputStream.close();
        return file;
    }

}
