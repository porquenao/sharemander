package mobi.porquenao.sharemander;

import android.content.Context;
import android.content.Intent;

import java.util.List;

public interface CustomPackageAction {

    List<String> getPackages(Context context);

    Intent getIntent(Intent intent);

}
