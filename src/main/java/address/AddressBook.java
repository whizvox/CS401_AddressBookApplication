package address;

import address.data.AddressEntry;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Stores a list of {@link AddressEntry} objects and several helper methods to manipulate this list, or retrieve data
 * from it.
 * @author Corneilious Eanes
 * @since March 16, 2021
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

  /**
   * Gets a single contact based on the specified ID.
   * @param id The ID of the contact
   * @return The contact corresponding to the specified ID, or <code>null</code> if no contact was found with that ID.
   */
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
   * @param contact The instance to check against
   * @return Whether or not any entries were removed
   * @see #remove(String)
   * @see #remove(UUID)
   */
  public boolean remove(AddressEntry contact) {
    return addressEntryList.entrySet().removeIf(entry -> entry.getValue().equals(contact));
  }

  /**
   * Removes an entry based on its ID.
   * @param id The ID of the contact to remove
   * @return True if a contact with the specified ID was found and was removed, or false if no contact was found with that ID.
   * @see #remove(String)
   * @see #remove(AddressEntry)
   */
  public boolean remove(UUID id) {
    return addressEntryList.remove(id) != null;
  }

  /**
   * Adds a new address entry to the internal list.
   * @param entry The address entry to add to the book.
   * @return True if the entry did not encounter an internal ID conflict and was successfully added, false otherwise
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

  /**
   * Clears out all locally-stored contacts. Only use if you really know what you're doing.
   * @see AddressBookApplication#refreshContactsList()
   */
  public void clear() {
    addressEntryList.clear();
  }

}
