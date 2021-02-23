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
 * @author Corneilious Eanes
 * @since February 22, 2021
 */
public class AddressBook {

  /**
   * Collection of all stored {@link AddressEntry}s, stored as a list.
   */
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
    // equivalent to doing addressEntryList.forEach(entry -> System.out.println(entry))
    addressEntryList.forEach(System.out::println);
  }

  /**
   * Returns the number of stored contacts in this address book.
   * @return The number of stored contacts
   */
  public int count() {
    return addressEntryList.size();
  }

  /**
   * Removes an address entry based on a matching last name. Warning: this will remove <strong>all</strong> entries
   * that match the query.
   * @param lastName The last name to search for when attempting to remove an address entry. This is case-insensitive
   * @return Whether or not any entries were removed
   * @see #remove(AddressEntry)
   */
  public boolean remove(String lastName) {
    return addressEntryList.removeIf(e -> e.getLastName().equalsIgnoreCase(lastName));
  }

  /**
   * Removes an entry based on an equality check with another given {@link AddressEntry} instance. Usually, this would
   * be used after performing a call to {@link #find(String)}, and you would pass an instance returned from that.
   * @param entry The instance to check against
   * @return Whether or not any entries were removed
   * @see #remove(String)
   */
  public boolean remove(AddressEntry entry) {
    return addressEntryList.remove(entry);
  }

  /**
   * Adds a new address entry to the internal list.
   * @param entry The address entry to add to the book.
   */
  public void add(AddressEntry entry) {
    addressEntryList.add(entry);
    // immediately sort the list after adding a new entry. this could be better streamlined if addressEntryList were
    // instead something like a map or a heap
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
   * @throws IOException If a problem was encountered during the reading process. This could include the
   * <code>fileName</code> resolving to a non-existent file, the file is write-only, or the file cannot be accessed
   * with given permissions.
   */
  public void readFromFile(String fileName) throws IOException {
    try (FileInputStream fileIn = new FileInputStream(fileName)) {
      Scanner scanner = new Scanner(fileIn);
      while (scanner.hasNext()) {
        AddressEntry entry = new AddressEntry();
        entry.setFirstName(scanner.nextLine());
        entry.setLastName(scanner.nextLine());
        Address address = new Address();
        address.setStreet(scanner.nextLine());
        address.setCity(scanner.nextLine());
        address.setState(scanner.nextLine());
        // It's supposed to be good practice to instead use Scanner#nextInt(), but it doesn't clear out the remaining
        // chars that exist in the buffer, and the next call of Scanner#next<something>() would result in an
        // empty string, which is dumb.
        address.setZip(Integer.parseInt(scanner.nextLine()));
        entry.setAddress(address);
        entry.setPhone(scanner.nextLine());
        entry.setEmail(scanner.nextLine());
        add(entry);
      }
    }
  }

  /**
   * A way to query this address book's contacts list. This will find and return all contacts that match the specified
   * entry.
   * @param startOfLastName The query to send. Will match any entry whose last name starts with this (case insensitive)
   * @return A list of all entries that matched the specified query. The list will be empty if none were found.
   */
  public List<AddressEntry> find(String startOfLastName) {
    return addressEntryList.stream()
      .filter(entry -> entry.getLastName().regionMatches(true, 0, startOfLastName, 0, startOfLastName.length()))
      .collect(Collectors.toList());
  }

}
