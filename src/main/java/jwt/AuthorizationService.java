package jwt;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AuthorizationService {

  private final ConcurrentHashMap<Date, String> tokenBlockList = new ConcurrentHashMap<>();
  private final ScheduledExecutorService executorService =
      Executors.newSingleThreadScheduledExecutor();

  private final JwtUtil jwtUtil;

  public AuthorizationService(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
    executorService.scheduleAtFixedRate(blockListCleaner(), 1, 10, TimeUnit.SECONDS);
  }

  public String login(String username) {
    return jwtUtil.create(username);
  }

  public void logout(String token) {
    var decodedToken = jwtUtil.decode(token);
    tokenBlockList.put(decodedToken.getExpiresAt(), token);
  }

  public void doSomething(String token) {
    if (tokenBlockList.containsValue(token)) {
      System.err.println("Token is blocked");
    } else if (jwtUtil.validate(token)) {
      System.out.println("Work is done");
    } else {
      System.err.println("Token is expired");
    }
  }

  private Runnable blockListCleaner() {
    return () -> {
      var now = new Date();
      var keySet = tokenBlockList.keySet();
      keySet.removeIf(key -> key.before(now));
      System.out.println("Blocked key count: " + tokenBlockList.size());
    };
  }
}
