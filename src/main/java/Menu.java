import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 */
public class Menu {

  private static Scanner SCANNER = null;

  private static void checkScanner() {
    if (SCANNER == null) {
      SCANNER = new Scanner(System.in);
    }
  }

  public static String promptLine(String prompt) {
    System.out.print(prompt + ": ");
    checkScanner();
    return SCANNER.nextLine();
  }

  public static String promptString(String prompt) {
    System.out.print(prompt + ": ");
    checkScanner();
    return SCANNER.next();
  }

  public static int promptInt(String prompt) {
    System.out.println(prompt + ": ");
    checkScanner();
    return SCANNER.nextInt();
  }

  public static char promptSelection(String prompt, Function<Character, Boolean> allowed) {
    String next = promptString(prompt);
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
    String cityCode = promptLine("City");
    if (cityCode.length() != 2) {
      throw new IllegalArgumentException("City code must be 2 characters exactly");
    }
    return cityCode;
  }

  public static String prompt_State() {
    return promptString("State code: ");
  }

  public static int prompt_Zip() {
    try {
      int zip = promptInt("Zip code");
      if (zip >= 0 && zip < 99999) {
        return zip;
      }
    } catch (InputMismatchException ignored) {}
    throw new IllegalArgumentException("Invalid zip code - must only contain 5 numeric characters");
  }

  // a more competently-written application would check if the prompted phone numbers and email addresses are valid,
  // but their specifications are too complex for this project

  public static String prompt_Phone() {
    return promptString("Phone number");
  }

  public static String prompt_Email() {
    return promptString("Email address");
  }

  public static void loadFromFile(AddressBook addressBook) {
    String fileName = promptLine("File name");
    try {
      addressBook.readFromFile(fileName);
    } catch (IOException e) {
      System.out.println("ERROR: " + e.getMessage());
    }
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
      System.out.println("ERROR: " + e.getMessage());
    }
  }

  public static void removeEntry(AddressBook addressBook) {
    boolean continueSearching = true;
    while (continueSearching) {
      String query = promptLine("Last name query: ");
      List<AddressEntry> entries = addressBook.find(query);
      AtomicInteger index = new AtomicInteger('a');
      entries.stream().limit(10).forEachOrdered(e -> {
        System.out.println(index.get() + ") " + e.toString());
      });
      if (entries.size() > 10) {
        System.out.println("... (more than 10 entries were found)");
      }
      System.out.println("1) Do another search");
      System.out.println("2) Do not remove contact");
      try {
        char selection = promptSelection("Select last name", c -> (c >= 'a' && c < 'a' + entries.size() && c <= 'j') || c == '1' || c == '2');
        if (selection == '2') {
          continueSearching = false;
        } else if (selection != '1') {
          AddressEntry entry = entries.get(selection - 'a');
          if (addressBook.remove(entry.getLastName())) {
            System.out.println("Contact was removed from your book");
          } else {
            // shouldn't happen!
            System.out.println("[SHOULD NOT SEE THIS] Could not find contact with last name " + entry.getLastName());
          }
          continueSearching = false;
        }
      } catch (IllegalArgumentException e) {
        System.out.println("ERROR: " + e.getMessage());
      }
    }
  }

  public static void findContacts(AddressBook addressBook) {
    String lastNameQuery = promptLine("Last name query");
    List<AddressEntry> entries = addressBook.find(lastNameQuery);
    if (entries.size() > 0) {
      entries.stream().limit(10).forEachOrdered(e -> {
        System.out.println(e.toString());
      });
    } else {
      System.out.println("No contacts found");
    }
  }

}
