package mobi.porquenao.andaction;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CacheManager {

    public static File cacheFile(Context context, File input, String name) throws IOException {
        return cacheInputStream(context, new FileInputStream(input), name);
    }

    public static File cacheInputStream(Context context, InputStream inputStream, String name) throws IOException {
        if (name != null) {
            name = "-" + name;
        }
        File output = File.createTempFile(ActionsDefaults.getFileNamePrefix(), name, context.getCacheDir());
        if (output.setReadable(true, false)) {
            FileOutputStream outputStream = new FileOutputStream(output);
            byte[] buf = new byte[2048];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            inputStream.close();
            outputStream.close();
        } else {
            throw new IOException("Could not set cache file to be world readable");
        }
        return output;
    }

    public static void clean(@NonNull Context context) {
        File[] files = context.getCacheDir().listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().contains(ActionsDefaults.getFileNamePrefix())) {
                    //noinspection ResultOfMethodCallIgnored
                    file.delete();
                }
            }
        }
    }

    public static void cleanAsync(@NonNull final Context context, @Nullable final Runnable runnable) {
        new Thread() {
            @Override
            public void run() {
                clean(context);
                if (runnable != null) runnable.run();
            }
        }.start();
    }

}
