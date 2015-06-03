package mobi.porquenao.andaction;

import android.app.Activity;

import mobi.porquenao.andaction.test.BaseInstrumentationTestCase;
import mobi.porquenao.andaction.type.ActionSend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class ActionsTest extends BaseInstrumentationTestCase {

    public void testWith() {
        Activity activity = mock(Activity.class);
        Actions.Builder builder = Actions.with(activity);
        assertThat(builder).isInstanceOf(Actions.Builder.class);
    }

    public void testBuilderSend() {
        Activity activity = mock(Activity.class);
        ActionSend send = Actions.with(activity).send();
        assertThat(send).isInstanceOf(ActionSend.class);
    }

}
