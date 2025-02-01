package mr.master.edu_click.auth;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.core.RedisTemplate;
import java.time.Duration;
@Component
public class RedisTokenStore {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String KEY_PREFIX = "jwt:";

    public RedisTokenStore(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void storeToken(String username, String token, Duration duration) {
        redisTemplate.opsForValue().set(
                KEY_PREFIX + username,
                token,
                duration
        );
    }

    public boolean validateToken(String username, String token) {
        String storedToken = (String) redisTemplate.opsForValue().get(KEY_PREFIX + username);
        return token.equals(storedToken);
    }

    public void invalidateToken(String username) {
        redisTemplate.delete(KEY_PREFIX + username);
    }



    private static final String REFRESH_PREFIX = "refresh:";

    public void storeRefreshToken(String username, String token, Duration duration) {
        redisTemplate.opsForValue().set(
                REFRESH_PREFIX + username,
                token,
                duration
        );
    }

    public boolean validateRefreshToken(String username, String token) {
        String storedToken = (String) redisTemplate.opsForValue().get(REFRESH_PREFIX + username);
        return token.equals(storedToken);
    }

    public void invalidateRefreshToken(String username) {
        redisTemplate.delete(REFRESH_PREFIX + username);
    }

}
