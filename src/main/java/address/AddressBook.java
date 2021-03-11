package address;

import address.data.AddressEntry;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Stores a list of {@link AddressEntry} objects and several helper methods to manipulate this list, retrieve data from
 * it, or populate it from reading a file.
 * @author Corneilious Eanes
 * @since March 11, 2021
 */
public class AddressBook {

  /**
   * Collection of all stored {@link AddressEntry}s, stored as a list.
   */
  private Map<UUID, AddressEntry> addressEntryList;

  /**
   * Constructor for this class. The internal list of address entries defaults to having zero entries.
   */
  public AddressBook() {
    addressEntryList = new HashMap<>();
  }

  /**
   * Prints all address entries present in the internal list to System.out.
   */
  public void list() {
    // equivalent to doing addressEntryList.forEach(entry -> System.out.println(entry))
    addressEntryList.values().forEach(System.out::println);
  }

  /**
   * Returns the number of stored contacts in this address book.
   * @return The number of stored contacts
   */
  public int count() {
    return addressEntryList.size();
  }

  public AddressEntry get(UUID id) {
    return addressEntryList.get(id);
  }

  /**
   * Removes an address entry based on a matching last name. Warning: this will remove <strong>all</strong> entries
   * that match the query.
   * @param lastName The last name to search for when attempting to remove an address entry. This is case-insensitive
   * @return Whether or not any entries were removed
   * @see #remove(AddressEntry)
   */
  public boolean remove(String lastName) {
    return addressEntryList.entrySet().removeIf(entry -> entry.getValue().getName().getLastName().equals(lastName));
  }

  /**
   * Removes an entry based on an equality check with another given {@link AddressEntry} instance. Usually, this would
   * be used after performing a call to {@link #find(String)}, and you would pass an instance returned from that.
   * @param entry The instance to check against
   * @return Whether or not any entries were removed
   * @see #remove(String)
   */
  public boolean remove(AddressEntry contact) {
    return addressEntryList.entrySet().removeIf(entry -> entry.getValue().equals(contact));
  }

  public boolean remove(UUID id) {
    return addressEntryList.remove(id) != null;
  }

  /**
   * Adds a new address entry to the internal list.
   * @param entry The address entry to add to the book.
   */
  public boolean add(AddressEntry entry) {
    if (!addressEntryList.containsKey(entry.getId())) {
      addressEntryList.put(entry.getId(), entry);
      return true;
    }
    return false;
  }

  /**
   * A way to query this address book's contacts list. This will find and return all contacts that match the specified
   * entry.
   * @param startOfLastName The query to send. Will match any entry whose last name starts with this (case insensitive)
   * @return A list of all entries that matched the specified query. The list will be empty if none were found.
   */
  public List<AddressEntry> find(String startOfLastName) {
    return addressEntryList.values().stream().sorted()
      .filter(entry -> entry.getName().getLastName().regionMatches(true, 0, startOfLastName, 0, startOfLastName.length()))
      .collect(Collectors.toList());
  }

  public void clear() {
    addressEntryList.clear();
  }

}
