package jwt;

import java.util.concurrent.TimeUnit;

public class Application {

  public static void main(String[] args) throws InterruptedException {
    var jwtUtil = new JwtUtil("p@55w0rd");
    var authService = new AuthorizationService(jwtUtil);

    var token = authService.login("shams");
    System.out.println("Got token: " + token);

    TimeUnit.SECONDS.sleep(5);

    authService.doSomething(token);

    TimeUnit.SECONDS.sleep(5);

    authService.doSomething(token);

    TimeUnit.SECONDS.sleep(5);

    System.out.println("Logging out: " + token);
    authService.logout(token);

    TimeUnit.SECONDS.sleep(5);

    authService.doSomething(token);

    Thread.currentThread().join(TimeUnit.MINUTES.toMillis(5));
  }
}
