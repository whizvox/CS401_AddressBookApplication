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

  /**
   * Prints out a formatted message to {@link System#out} in the following format:
   * <pre>[{level}] {message}</pre>
   * @param level The level of importance of the message. More of an aesthetic choice than anything
   * @param pattern The pattern of the formatted message utilizing the rules of
   *                {@link String#format(String, Object...)} (i.e. using <code>%s</code> for strings and
   *                <code>%d</code> for integers). If you choose not to use the formatting system, then this
   *                essentially acts as the message itself.
   * @param args Arguments referred to by the pattern string
   * @see #info(String, Object...)
   * @see #warn(String, Object...)
   * @see #warn(Throwable, String, Object...)
   * @see #error(String, Object...)
   * @see #error(Throwable, String, Object...)
   */
  public static void log(String level, String pattern, Object... args) {
    System.out.printf("[%s] %s%n", level, String.format(pattern, args));
  }

  /**
   * Logs a not-particular-important information message to the console.
   * @param pattern The pattern string
   * @param args The arguments referred to by the pattern string
   * @see #log(String, String, Object...)
   */
  public static void info(String pattern, Object... args) {
    log(LEVEL_INFO, pattern, args);
  }

  /**
   * Logs an important warning message to the console.
   * @param pattern The pattern string
   * @param args The arguments referred to by the pattern string
   * @see #info(String, Object...)
   * @see #log(String, String, Object...)
   */
  public static void warn(String pattern, Object... args) {
    log(LEVEL_WARN, pattern, args);
  }

  /**
   * Logs an important warning message to the console, then prints the stacktrace of a specified {@link Throwable}
   * object afterwards.
   * @param t The throwable object whose stacktrace is printed after the log message
   * @param pattern The pattern string
   * @param args The arguments referred to by the pattern string
   * @see #warn(String, Object...)
   * @see #log(String, String, Object...)
   */
  public static void warn(Throwable t, String pattern, Object... args) {
    warn(pattern, args);
    t.printStackTrace(System.out);
  }

  /**
   * Logs an important error message to the console.
   * @param pattern The pattern string
   * @param args The argument referred to by the pattern string
   * @see #error(Throwable, String, Object...)
   * @see #log(String, String, Object...)
   */
  public static void error(String pattern, Object... args) {
    log(LEVEL_ERROR, pattern, args);
  }

  /**
   * Logs an important error message to the console then prints the stacktrace of a specified {@link Throwable} object
   * afterwards.
   * @param t The throwable object whose stacktrace is printed out after the message
   * @param pattern The pattern string
   * @param args The arguments referred to by the pattern string
   * @see #error(String, Object...)
   * @see #log(String, String, Object...)
   */
  public static void error(Throwable t, String pattern, Object... args) {
    error(pattern, args);
    t.printStackTrace(System.out);
  }

  // simple, but gets the job done. essentially checks if there is at least 1 character for each part of the email
  private static final Pattern EMAIL_PATTERN = Pattern.compile("^.+@.+\\..+$");

  /**
   * Checks whether or not the specified email address is a valid one. It's not an exhaustive check, but it'll conform
   * to most resolvable email addresses encountered in a production environment.
   * @param email The email address to validate
   * @return Whether the email address has at least 1 character for each part (the local-part, the domain name, and the
   * domain extension).
   */
  public static boolean isEmailValid(String email) {
    return EMAIL_PATTERN.matcher(email).matches();
  }

  /**
   * Checks whether the specified phone number is a valid one.
   * @param phone The phone number to validate
   * @return Whether the phone number contains at least 10 digits (0-9)
   */
  public static boolean isPhoneNumberValid(String phone) {
    int digitsCount = 0;
    for (int i = 0; i < phone.length(); i++) {
      if (Character.isDigit(phone.charAt(i))) {
        digitsCount++;
      }
    }
    return digitsCount >= 10;
  }

  /**
   * Used to validate the contents of an {@link AddressEntry}. An entry is considered invalid if one of the following
   * criteria are met:
   * <ul>
   *   <li>The first name is empty</li>
   *   <li>The last name is empty</li>
   *   <li>The street address is empty</li>
   *   <li>The city name is empty</li>
   *   <li>The state code is not 2 characters long</li>
   *   <li>The zip code is not 5 digits long</li>
   *   <li>The phone number fails {@link #isPhoneNumberValid(String)}</li>
   *   <li>The email address fails {@link #isEmailValid(String)}</li>
   * </ul>
   * @param entry The entry to validate
   * @throws IllegalArgumentException If the entry is considered invalid. The message will contain the reasoning as to
   *                                  why.
   */
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
    if (!isPhoneNumberValid(entry.getPhone())) {
      throw new IllegalArgumentException("Phone number must have at least 10 digits");
    }
    if (!isEmailValid(entry.getEmail())) {
      throw new IllegalArgumentException("Improperly formatted email address");
    }
  }

}
