import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Stores a list of {@link AddressEntry} objects and several helper methods to manipulate this list, retrieve data from
 * it, or populate it from reading a file.
 */
public class AddressBook {

  private List<AddressEntry> addressEntryList;

  /**
   * Constructor for AddressBook. The internal list of address entries defaults to having zero entries.
   */
  public AddressBook() {
    addressEntryList = new ArrayList<>();
  }

  /**
   * Prints all address entries present in the internal list to System.out.
   */
  public void list() {
    addressEntryList.forEach(System.out::println);
  }

  /**
   * Removes an address entry based on a matching last name.
   * @param lastName The last name to search for when attempting to remove an address entry. This is case-insensitive.
   */
  public boolean remove(String lastName) {
    return addressEntryList.removeIf(e -> e.getLastName().equalsIgnoreCase(lastName));
  }

  /**
   * Adds a new address entry to the internal list, overwriting any currently stored with a matching (case-insensitive)
   * last name.
   * @param entry The address entry to add to the book. If a matching (case in-sensitive) last name is found, then the
   *              matching stored entry is first removed.
   */
  public void add(AddressEntry entry) {
    remove(entry.getLastName());
    addressEntryList.add(entry);
    addressEntryList.sort(Comparator.comparing(AddressEntry::getLastName));
  }

  /**
   * Populates the book with data read from a file denoted by the <code>fileName</code>. The data MUST be stored
   * according to the following specifications:
   * <ul>
   *   <li>File must be stored in a UTF-8-friendly encoding.</li>
   *   <li>Each address entry is consisted of 7 lines, each ending with a line-separating character: <ol>
   *     <li><code>${firstName}</code></li>
   *     <li><code>${lastName}</code></li>
   *     <li><code>${street}</code></li>
   *     <li><code>${city}</code></li>
   *     <li><code>${state} ${zip}</code></li>
   *     <li><code>${phone}</code></li>
   *     <li><code>${email}</code></li>
   *   </ol></li>
   * </ul>
   * @param fileName The path to the file to be read.
   * @throws IOException If a problem was encounted during the reading process. This could include the
   * <code>fileName</code> resolving to a non-existent file, the file is write-only, or the file cannot be accessed
   * with given permissions.
   */
  public void readFromFile(String fileName) throws IOException {
    try (FileInputStream fileIn = new FileInputStream(fileName)) {
      Scanner scanner = new Scanner(fileIn);
      AddressEntry entry = new AddressEntry();
      entry.setFirstName(scanner.nextLine());
      entry.setLastName(scanner.nextLine());
      Address address = new Address();
      address.setStreet(scanner.nextLine());
      address.setCity(scanner.nextLine());
      address.setState(scanner.next());
      address.setZip(scanner.nextInt());
      entry.setAddress(address);
      entry.setPhone(scanner.nextLine());
      entry.setEmail(scanner.nextLine());
      add(entry);
    }
    System.out.println("Successfully loaded in " + addressEntryList.size() + " entries");
  }

  public List<AddressEntry> find(String startOfLastName) {
    return addressEntryList.stream().filter(entry -> entry.getLastName().startsWith(startOfLastName)).collect(Collectors.toList());
  }

}
