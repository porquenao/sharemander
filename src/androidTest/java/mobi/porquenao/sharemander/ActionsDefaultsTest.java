package mobi.porquenao.sharemander;

import mobi.porquenao.sharemander.test.BaseInstrumentationTestCase;

import static org.assertj.core.api.Assertions.assertThat;

public class ActionsDefaultsTest extends BaseInstrumentationTestCase {

    public void testFileNamePrefix() {
        assertThat(ActionsDefaults.getFileNamePrefix()).isNotEmpty();
        String fileNamePrefix = "fileNamePrefix";
        ActionsDefaults.setFileNamePrefix(fileNamePrefix);
        assertThat(ActionsDefaults.getFileNamePrefix()).isEqualTo(fileNamePrefix);
    }

    public void testSendDialogTitle() {
        assertThat(ActionsDefaults.getSendDialogTitle()).isNull();
        String sendDialogTitle = "sendDialogTitle";
        ActionsDefaults.setSendDialogTitle(sendDialogTitle);
        assertThat(ActionsDefaults.getSendDialogTitle()).isEqualTo(sendDialogTitle);
    }

    public void testAddBlacklist() {
        assertThat(ActionsDefaults.getBlacklist()).isNotNull();
        assertThat(ActionsDefaults.getBlacklist()).isEmpty();
        String blacklistPackage = "blacklist.package";
        ActionsDefaults.addBlacklist(blacklistPackage);
        assertThat(ActionsDefaults.getBlacklist().size()).isEqualTo(1);
        assertThat(ActionsDefaults.getBlacklist().get(0)).isEqualTo(blacklistPackage);
    }

    public void testRemoveBlacklist() {
        String blacklistPackage = "blacklist.package";
        ActionsDefaults.addBlacklist(blacklistPackage);
        ActionsDefaults.removeBlacklist(blacklistPackage);
        assertThat(ActionsDefaults.getBlacklist()).isEmpty();
    }

    public void testClearBlacklist() {
        String blacklistPackage = "blacklist.package";
        ActionsDefaults.addBlacklist(blacklistPackage);
        ActionsDefaults.clearBlacklist();
        assertThat(ActionsDefaults.getBlacklist()).isEmpty();
    }

}
