package address;

import address.data.AddressEntry;

import java.util.regex.Pattern;

/**
 * A collection of utility methods used throughout this application.
 * @author Corneilious Eanes
 * @since March 15, 2021
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

  public static void warn(Throwable t, String pattern, Object... args) {
    warn(pattern, args);
    t.printStackTrace(System.out);
  }

  public static void error(String pattern, Object... args) {
    log(LEVEL_ERROR, pattern, args);
  }

  public static void error(Throwable t, String pattern, Object... args) {
    error(pattern, args);
    t.printStackTrace(System.out);
  }

  // simple, but gets the job done. essentially checks if there is at least 1 character for each part of the email
  private static final Pattern EMAIL_PATTERN = Pattern.compile("^.+@.+\\..+$");

  public static boolean isEmailValid(String email) {
    return EMAIL_PATTERN.matcher(email).matches();
  }

  // TODO maybe figure this out later? feel free to remove if deemed not important enough
  public static boolean isPhoneNumberValid(String phone) {
    return true;
  }

  public static void validateAddressEntry(AddressEntry entry) throws IllegalArgumentException {
    if (entry.getName().getFirstName().isEmpty()) {
      throw new IllegalArgumentException("First name cannot be empty");
    }
    if (entry.getName().getLastName().isEmpty()) {
      throw new IllegalArgumentException("Last name cannot be empty");
    }
    if (entry.getAddress().getStreet().isEmpty()) {
      throw new IllegalArgumentException("Street address cannot be empty");
    }
    if (entry.getAddress().getCity().isEmpty()) {
      throw new IllegalArgumentException("City name cannot be empty");
    }
    if (entry.getAddress().getState().length() != 2) {
      throw new IllegalArgumentException("State code must be 2 characters");
    }
    int zip = entry.getAddress().getZip();
    if (zip < 10000 || zip > 99999) {
      throw new IllegalArgumentException("Zip code must be 5 characters");
    }
    if (entry.getPhone().length() < 10) {
      throw new IllegalArgumentException("Invalid phone number, must be at least 10 numbers");
    }
    if (!isPhoneNumberValid(entry.getPhone())) {
      throw new IllegalArgumentException("Improperly formatted phone number");
    }
    if (!isEmailValid(entry.getEmail())) {
      throw new IllegalArgumentException("Improperly formatted email");
    }
  }

}
