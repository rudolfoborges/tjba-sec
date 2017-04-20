package android.sec.mobile.tjba.jus.br.sec_android;

/**
 * Created by rudolfoborges on 20/04/17.
 */

public class Session {

    private String sessionId;

    private String triploDesKey;

    private String createdAt;

    private String expires;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getTriploDesKey() {
        return triploDesKey;
    }

    public void setTriploDesKey(String triploDesKey) {
        this.triploDesKey = triploDesKey;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    @Override
    public String toString() {
        return "Session{" +
                "sessionId='" + sessionId + '\'' +
                ", triploDesKey='" + triploDesKey + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", expires='" + expires + '\'' +
                '}';
    }
}
