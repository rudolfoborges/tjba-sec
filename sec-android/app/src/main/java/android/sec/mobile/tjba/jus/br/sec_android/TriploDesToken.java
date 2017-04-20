package android.sec.mobile.tjba.jus.br.sec_android;

/**
 * Created by rudolfoborges on 20/04/17.
 */
public class TriploDesToken {

    private String timestamp;

    private String ip;

    private String platform;

    public TriploDesToken(String timestamp,
                          String ip,
                          String platform) {
        this.timestamp = timestamp;
        this.ip = ip;
        this.platform = platform;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getIp() {
        return ip;
    }

    public String getPlatform() {
        return platform;
    }
}
