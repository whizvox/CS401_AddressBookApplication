import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * A collection of helper methods for use in the application's console menu system.
 * @author Corneilious Eanes
 * @since February 22, 2021
 */
public class Menu {

  // singleton model of a Scanner object
  private static Scanner SCANNER = null;

  // initializes the static SCANNER object if it hasn't been initialized already
  private static void checkScanner() {
    if (SCANNER == null) {
      SCANNER = new Scanner(System.in);
    }
  }

  /**
   * Using {@link System#in} and {@link System#out}, this will print a prompt for the user, and will return the next
   * link of whatever they typed.
   * @param prompt The prompt to display to the user. Will be formatted as "${prompt}: " (no quotations)
   * @return The next line of whatever the user typed in
   */
  public static String promptLine(String prompt) {
    System.out.print(prompt + ": ");
    checkScanner();
    return SCANNER.nextLine();
  }

  /**
   * Will do the same operation as {@link #promptLine(String)}, but will immediately attempt to parse and return the
   * given input as an integer.
   * @param prompt The prompt to display to the user. Will be formatted as "${prompt}: " (no quotations)
   * @return The next line of whatever the user typed in, parsed as an integer
   * @throws NumberFormatException if the typed user input cannot be parsed as an integer
   * @see #promptLine(String)
   */
  public static int promptInt(String prompt) {
    return Integer.parseInt(promptLine(prompt));
  }

  /**
   * Will do the same operation as {@link #promptLine(String)}, but will only accept single-character responses in the
   * form of <code>char</code>. Additionally, you can pass a function that will check the typed <code>char</code>
   * selection to see if it is "allowed".
   * @param prompt The prompt to display to the user. Will be formatted as "${prompt}: " (no quotations)
   * @param allowed A function to decide whether or not the selection is valid. Returning true means the selection is
   *                valid, return false means the selection is invalid, and thus an {@link IllegalArgumentException}
   *                will be thrown.
   * @return A <code>char</code> representing the user's selection
   * @throws IllegalArgumentException If either the user typed in a sequence that was not 1 character in length, or if
   * the specified <code>allowed</code> function evaluated to false.
   * @see #promptLine(String)
   */
  public static char promptSelection(String prompt, Function<Character, Boolean> allowed) {
    String next = promptLine(prompt);
    if (next.length() != 1) {
      throw new IllegalArgumentException("Selection must be a single character");
    }
    char c = next.charAt(0);
    if (!allowed.apply(c)) {
      throw new IllegalArgumentException("Invalid selection");
    }
    return c;
  }

  public static String prompt_FirstName() {
    return promptLine("First name");
  }

  public static String prompt_LastName() {
    return promptLine("Last name");
  }

  public static String prompt_Street() {
    return promptLine("Street");
  }

  public static String prompt_City() {
    return promptLine("City");
  }

  public static String prompt_State() {
    String stateCode = promptLine("State code");
    if (stateCode.length() != 2) {
      throw new IllegalArgumentException("State code must be 2 characters only");
    }
    return stateCode;
  }

  // Will throw an IllegalArgumentException if the user inputted something that either could not be parsed as an
  // integer or if the resulting integer is not exactly 5 digits
  public static int prompt_Zip() {
    try {
      int zip = promptInt("Zip code");
      if (zip >= 0 && zip < 99999) {
        return zip;
      }
    } catch (InputMismatchException ignored) {}
    throw new IllegalArgumentException("Invalid zip code - must only contain 5 numeric characters");
  }

  // A more competently-written application would check if the prompted phone numbers and email addresses are valid,
  // but their specifications are too complex for this project and will thus be ignored.

  public static String prompt_Phone() {
    return promptLine("Phone number");
  }

  public static String prompt_Email() {
    return promptLine("Email address");
  }

  public static void loadFromFile(AddressBook addressBook) {
    String fileName = promptLine("File name");
    try {
      addressBook.readFromFile(fileName);
    } catch (IOException e) {
      // don't let the parent process deal with the exception
      System.out.println("ERROR: " + e.getMessage());
    }
    System.out.printf("%d entries read from file%n", addressBook.count());
  }

  public static void addEntry(AddressBook addressBook) {
    try {
      AddressEntry entry = new AddressEntry();
      entry.setFirstName(prompt_FirstName());
      entry.setLastName(prompt_LastName());
      Address address = new Address();
      address.setStreet(prompt_Street());
      address.setCity(prompt_City());
      address.setState(prompt_State());
      address.setZip(prompt_Zip());
      entry.setAddress(address);
      entry.setPhone(prompt_Phone());
      entry.setEmail(prompt_Email());
      addressBook.add(entry);
      System.out.println("The following contact has been added to your book:");
      System.out.println(entry);
    } catch (IllegalArgumentException e) {
      // don't let the parent process deal with the exception
      System.out.println("ERROR: " + e.getMessage());
    }
  }

  // the most complex method in this project
  public static void removeEntry(AddressBook addressBook) {
    boolean continueSearching = true;
    // will continue to loop through asking the user for their selection unless
    // 1. an entry is removed
    // 2. the user asks to stop searching
    // 3. the user asks to cancel their removal of a contact
    while (continueSearching) {
      String query = promptLine("Last name query");
      List<AddressEntry> entries = addressBook.find(query);
      if (entries.isEmpty()) {
        System.out.println("No contacts found");
      } else {
        // anonymous functions can't manipulate primitives, use an atomic object as a wrapper
        AtomicInteger index = new AtomicInteger('a');
        // don't print more than 10 entries to the screen. don't want to clutter it up too much. also make sure they're
        // ordered, as we want what's displayed with what the user wants to remove
        entries.stream().limit(10).forEachOrdered(e -> {
          // equivalent to doing index++, but in atomic form
          System.out.println((char) index.getAndIncrement() + ") " + e.toString());
        });
        if (entries.size() > 10) {
          System.out.println("... (more than 10 entries were found)");
        }
      }
      // the end result will be formatted like the following:
      // a) Smith, John
      //     123 Main Street
      //     San Francisco, CA 12345
      //     john.smith@example.com
      //     555-555-1234
      // b) Smitt, Hugh
      //     456 Washington Way
      //     Seattle, WA 67890
      //     shugh@mywebsite.org
      //     555-555-4321
      System.out.println("1) Do another search");
      System.out.println("2) Do not remove contact");
      try {
        // only chars 'a' through 'j' (limited by entries.size()), '1', or '2' are considered valid
        char selection = promptSelection("Selection", c -> (c >= 'a' && c < 'a' + entries.size() && c <= 'j') || c == '1' || c == '2');
        // choosing options a-j or 2 will result in the parent while loop terminating
        if (selection == '2') {
          // quit out of this menu
          continueSearching = false;
        } else if (selection != '1') {
          // remember to subtract the 'a' offset
          AddressEntry entry = entries.get(selection - 'a');
          // final confirmation before attempting to remove the contact
          System.out.println("Are you sure you wish to remove this contact?");
          System.out.println(entry);
          String answer = promptLine("Yes/no").toLowerCase();
          if (answer.equals("yes")) {
            if (addressBook.remove(entry)) {
              System.out.println("Contact was removed from your book");
            } else {
              // shouldn't happen!
              System.out.println("ERROR: Could not remove contact as it was not found");
            }
          // anything other than "yes" will be treated as a no
          } else {
            System.out.println("Removal of contact has been canceled");
          }
          continueSearching = false;
        }
      } catch (IllegalArgumentException e) {
        // don't let the parent process deal with the exception
        System.out.println("ERROR: " + e.getMessage());
      }
    }
  }

  public static void findContacts(AddressBook addressBook) {
    String lastNameQuery = promptLine("Last name query");
    List<AddressEntry> entries = addressBook.find(lastNameQuery);
    if (entries.size() > 0) {
      // like the removeEntry method, limit the number of listed contacts to 10 to keep screen clutter to a minimum
      entries.stream().limit(10).forEachOrdered(e -> {
        System.out.println(e.toString());
      });
      if (entries.size() > 10) {
        System.out.println("... (more than 10 entries were found)");
      }
    } else {
      System.out.println("No contacts found");
    }
  }

  // the core of the entire application. handles displaying the main menu and handling selection input that will be
  // delegated to other helper methods.
  public static void showRootMenu(AddressBook ab) {
    boolean run = true;
    // will continue to loop, asking the user for their main menu selection until the user selects the final option,
    // which is to quit
    while (run) {
      System.out.println("Please enter your menu selection:");
      System.out.println("a) Load contacts from file");
      System.out.println("b) Add new contact");
      System.out.println("c) Remove contact");
      System.out.println("d) Find contact information");
      System.out.println("e) List all contacts");
      System.out.println("f) Quit program");
      System.out.println("---------------------------------");
      try {
        // only chars 'a' through 'f' are considered valid
        char selection = promptSelection("Selection", (c) -> c >= 'a' && c <= 'f');
        switch (selection) {
          case 'a':
            loadFromFile(ab);
            break;
          case 'b':
            addEntry(ab);
            break;
          case 'c':
            removeEntry(ab);
            break;
          case 'd':
            findContacts(ab);
            break;
          case 'e':
            ab.list();
            System.out.printf("Total of %d contacts%n", ab.count());
            break;
          case 'f':
            // gracefully break from the loop
            run = false;
            break;
        }
      } catch (Exception e) {
        // catch any unhandled exceptions and print them in a user-friendly way
        System.out.println("ERROR: " + e.getMessage());
      }
      // add an additional newline for clarity
      System.out.println();
    }
    // final message
    System.out.println("Thank you for using this program.");
  }

}
