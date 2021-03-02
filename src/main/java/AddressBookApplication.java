import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * The main class for the address book application.
 * @author Corneilious Eanes
 * @since March 1, 2021
 */
public class AddressBookApplication {

  private static AddressBook ab;

  public static AddressBook getAddressBook() {
    return ab;
  }

  /**
   * The main entry point into the address book application.
   * @param args This is ignored, please don't use it
   */
  public static void main(String[] args) {
    ab = new AddressBook();
    //Menu.showRootMenu(ab);
    JFrame frame = new JFrame("Address Book Application");
    frame.setContentPane(new MainPanel());
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }

  /**
   * Initializes the static-bound address book instance with entries read in from a file. Will print out all read
   * entries to the console.
   * @param fileName The file name pointing to the file to be read
   * @throws java.io.FileNotFoundException If the file name does not point to an existing file
   * @throws IOException If some other I/O-related problem occurred during reading, such as insufficient permissions
   */
  public static void init(String fileName) throws IOException {
    // Java 7 structure, will close the input stream automatically when block completes
    try (FileInputStream in = new FileInputStream(fileName)) {
      Scanner scanner = new Scanner(in);
      // keep reading from the scanner until it can't read anything else
      while (scanner.hasNext()) {
        // every data point is line-separated
        String firstName = scanner.nextLine();
        String lastName = scanner.nextLine();
        String street = scanner.nextLine();
        String city = scanner.nextLine();
        String state = scanner.nextLine();
        // could technically do Scanner#nextInt() but we then have to skip the additional empty string
        int zip = Integer.parseInt(scanner.nextLine());
        String email = scanner.nextLine();
        String phone = scanner.nextLine();
        Address address = new Address(street, city, state, zip);
        AddressEntry entry = new AddressEntry(firstName, lastName, address, phone, email);
        ab.add(entry);
        // now that the entry has been added, print out some info for the user
        System.out.println("Address entry added to book:");
        System.out.println(entry);
        System.out.println();
      }
    }
  }

}
