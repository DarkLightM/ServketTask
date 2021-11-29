package profiles;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class AccountService {
    private static final Map<String, Profile> sessionIdToProfile;
    private static final Path homeDirectory = Paths.get("D:\\IntelliJ IDEA Community Edition 2020.3.2\\Users\\");

    static  {
        sessionIdToProfile = new HashMap<>();
    }

    public static Path getHomeDirectory(){ return homeDirectory; }

    public static Profile getUserBySessionId(String sessionId) {
        return sessionIdToProfile.get(sessionId);
    }

    public static void addSession(String sessionId, Profile profile) {
        sessionIdToProfile.put(sessionId, profile);
    }

    public static void deleteSession(String sessionId) {
        sessionIdToProfile.remove(sessionId);
    }
}
