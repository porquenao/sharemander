package mobi.porquenao.sharemander;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class CacheManager {

    public static File cacheFile(Context context, File input, String name) throws IOException {
        return cacheInputStream(context, new FileInputStream(input), name);
    }

    public static File cacheInputStream(Context context, InputStream inputStream, String name) throws IOException {
        if (name != null) {
            name = "-" + name;
        }

        File cacheDir = new File(context.getCacheDir(), "sharemander");
        cacheDir.mkdirs();

        File output = File.createTempFile(ActionsDefaults.getFileNamePrefix(), name, cacheDir);
        FileOutputStream outputStream = new FileOutputStream(output);
        byte[] buf = new byte[2048];
        int len;
        while ((len = inputStream.read(buf)) > 0) {
            outputStream.write(buf, 0, len);
        }
        inputStream.close();
        outputStream.close();
        return output;
    }

    public static void clean(@NonNull Context context) {
        File[] files = new File(context.getCacheDir(), "sharemander").listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().contains(ActionsDefaults.getFileNamePrefix())) {
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
