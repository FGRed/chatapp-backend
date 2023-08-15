package net.chatapp.util;

public class DeviceUtil {

    public static String getBrowserInfo(String information) {
        String browserName = "";
        String browserVersion = "";
        String browser = information;
        if (browser.contains("MSIE")) {
            String subsString = browser.substring(browser.indexOf("MSIE"));
            String info[] = (subsString.split(";")[0]).split(" ");
            browserName = info[0];
            browserVersion = info[1];
        } else if (browser.contains("Firefox")) {

            String subsString = browser.substring(browser.indexOf("Firefox"));
            String info[] = (subsString.split(" ")[0]).split("/");
            browserName = info[0];
            browserVersion = info[1];
        } else if (browser.contains("Chrome")) {

            String subsString = browser.substring(browser.indexOf("Chrome"));
            String info[] = (subsString.split(" ")[0]).split("/");
            browserName = info[0];
            browserVersion = info[1];
        } else if (browser.contains("Opera")) {

            String subsString = browser.substring(browser.indexOf("Opera"));
            String info[] = (subsString.split(" ")[0]).split("/");
            browserName = info[0];
            browserVersion = info[1];
        } else if (browser.contains("Safari")) {

            String subsString = browser.substring(browser.indexOf("Safari"));
            String info[] = (subsString.split(" ")[0]).split("/");
            browserName = info[0];
            browserVersion = info[1];
        }
        return browserName + "-" + browserVersion;
    }

}
