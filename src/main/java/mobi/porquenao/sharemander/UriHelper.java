package mobi.porquenao.sharemander;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.support.v4.content.FileProvider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

public class UriHelper {

    public static Uri file(@NonNull Context context, @NonNull String name, @NonNull File file) throws Exception {
        File cacheFile = CacheManager.cacheFile(context, file, name);
        return FileProvider.getUriForFile(context, context.getPackageName() + ".sharemander", cacheFile);
    }

    public static Uri uri(@NonNull Context context, @NonNull String name, @NonNull Uri uri) throws Exception {
        return file(context, name, new File(uri.toString()));
    }

    public static Uri stream(@NonNull Context context, @NonNull String name, @NonNull InputStream inputStream) throws Exception {
        File cacheFile = CacheManager.cacheInputStream(context, inputStream, name);
        return FileProvider.getUriForFile(context, context.getPackageName() + ".sharemander", cacheFile);
    }

    public static Uri asset(@NonNull Context context, @NonNull String name, @NonNull String asset) throws Exception {
        return stream(context, name, context.getAssets().open(asset));
    }

    public static Uri raw(@NonNull Context context, @NonNull String name, @RawRes int raw) throws Exception {
        return stream(context, name, context.getResources().openRawResource(raw));
    }

    public static Uri bytes(@NonNull Context context, @NonNull String name, @NonNull byte[] bytes) throws Exception {
        return stream(context, name, new ByteArrayInputStream(bytes));
    }

    public static Uri bitmap(@NonNull Context context, @NonNull String name, Bitmap bitmap) throws Exception {
        return bitmap(context, name, Bitmap.CompressFormat.PNG, 100, bitmap);
    }

    public static Uri bitmap(@NonNull Context context, @NonNull String name, int quality, Bitmap bitmap) throws Exception {
        return bitmap(context, name, Bitmap.CompressFormat.JPEG, quality, bitmap);
    }

    public static Uri bitmap(@NonNull Context context, @NonNull String name, @NonNull Bitmap.CompressFormat compressFormat, int quality, Bitmap bitmap) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(compressFormat, quality, byteArrayOutputStream);
        return bytes(context, name, byteArrayOutputStream.toByteArray());
    }

    public static Uri drawable(@NonNull Context context, @NonNull String name, @DrawableRes int drawable) throws Exception {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawable);
        return bitmap(context, name, bitmap);
    }

}
