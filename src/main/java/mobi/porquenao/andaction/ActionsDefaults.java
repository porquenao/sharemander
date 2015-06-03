package mobi.porquenao.andaction;

import java.util.ArrayList;

public class ActionsDefaults {

    private static String sFileNamePrefix = "andaction-";
    private static String sSendDialogTitle;
    private static ArrayList<String> sBlacklist = new ArrayList<>();

    public static String getFileNamePrefix() {
        return sFileNamePrefix;
    }

    public static void setFileNamePrefix(String fileNamePrefix) {
        sFileNamePrefix = fileNamePrefix;
    }

    public static String getSendDialogTitle() {
        return sSendDialogTitle;
    }

    public static void setSendDialogTitle(String sendDialogTitle) {
        sSendDialogTitle = sendDialogTitle;
    }

    public static ArrayList<String> getBlacklist() {
        return sBlacklist;
    }

    public static void addBlacklist(String appPackage) {
        sBlacklist.add(appPackage);
    }

    public static void removeBlacklist(String appPackage) {
        sBlacklist.remove(appPackage);
    }

    public static void clearBlacklist() {
        sBlacklist.clear();
    }

}
