package address;

/**
 * @author Corneilious Eanes
 * @since March 11, 2021
 */
public class Utils {

  private static final String
      LEVEL_INFO = "INFO",
      LEVEL_WARN = "WARN",
      LEVEL_ERROR = "ERROR";

  private static void log(String level, String pattern, Object... args) {
    System.out.printf("[%s] %s%n", level, String.format(pattern, args));
  }

  public static void info(String pattern, Object... args) {
    log(LEVEL_INFO, pattern, args);
  }

  public static void warn(String pattern, Object... args) {
    log(LEVEL_WARN, pattern, args);
  }

  public static void error(String pattern, Object... args) {
    log(LEVEL_ERROR, pattern, args);
  }

}
