package etc;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class SHA {
    public static String encrypt(String password) {
        return Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
    }
}