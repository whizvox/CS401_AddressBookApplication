package address.data;

import java.util.Objects;
import java.util.UUID;

/**
 * A data class for storing elements of an address entry.
 *
 * @author Corneilious Eanes
 * @since March 11, 2021
 */
public class AddressEntry implements Comparable<AddressEntry> {

  private UUID id;
  private Name name;
  private Address address;
  private String phone;
  private String email;

  /**
   * The "fill-all" constructor of the address entry class. Every passed argument will initialize the corresponding
   * field as-is.
   * // @param firstName The first name
   * // @param lastName The last name
   * @param address The address
   * @param phone The phone number
   * @param email The e-mail address
   */
  public AddressEntry(UUID id, Name name, Address address, String phone, String email) {
    this.id = id;
    this.name = name;
    this.address = address;
    this.phone = phone;
    this.email = email;
  }

  /**
   * Default constructor in which all string-based fields (<code>firstName</code>, <code>lastName</code>,
   * <code>address.street</code>, <code>address.city</code>, <code>address.state</code>, <code>phone</code>,
   * <code>email</code>) are initialized to an empty string, and all integer-based fields (<code>address.zip</code>)
   * are initialized to 0.
   */
  public AddressEntry() {
    this(null, new Name(), new Address(), "", "");
  }

  /**
   * Creates a string representation of all the data points in this entry. The output will be formatted like this:
   * <pre>
   *${lastName}, ${firstName}
   *    ${street}
   *    ${city}, ${state} ${zip}
   *    ${email}
   *    ${phone}
   * </pre>
   * Example:
   * <pre>
   *Smith, John
   *    123 Main Street
   *    San Francisco, CA 12345
   *    john.smith@example.com
   *    555-555-1234
   * </pre>
   * @return A fully string-formatted address entry.
   * @see Address#toString()
   */
  @Override
  public String toString() {
    return name.getLastName() + ", " + name.getFirstName() + '\n' +
      '\t' + address.getStreet() + '\n' +
      '\t' + address.getCity() + ", " + address.getState() + " " + address.getZip() + '\n' +
      '\t' + email + '\n' +
      '\t' + phone;
  }

  /**
   * To test for value or reference equality.
   * @param obj The object to test against.
   * @return Whether the specified object refers to the same reference as this object or if the specified object is an
   * instance of this object and its <code>firstName</code>, <code>lastName</code>, <code>address</code>,
   * <code>phone</code>, and <code>email</code> all equal that of this object. Will return false otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof AddressEntry) {
      AddressEntry entry = (AddressEntry) obj;
      return Objects.equals(entry.id, id) && Objects.equals(entry.name, name) &&
        Objects.equals(entry.address, address) && Objects.equals(entry.phone, phone) &&
        Objects.equals(entry.email, email);
    }
    return false;
  }

  public boolean isIdSet() {
    return id != null;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Name getName() {
    return name;
  }

  public void setName(Name name) {
    this.name = name;
  }

  /**
   * Get the address of this contact.
   * @return This contact's address
   */
  public Address getAddress() {
    return address;
  }

  /**
   * Set the address of this contact.
   * @param address The new address for this contact
   */
  public void setAddress(Address address) {
    this.address = address;
  }

  /**
   * Get the phone number of this contact.
   * @return This contact's phone number
   */
  public String getPhone() {
    return phone;
  }

  /**
   * Set the phone number of this contact.
   * @param phone The new phone number for this contact
   */
  public void setPhone(String phone) {
    this.phone = phone;
  }

  /**
   * Get the e-mail address of this contact
   * @return This contact's e-mail address
   */
  public String getEmail() {
    return email;
  }

  /**
   * Set the e-mail address of this contact
   * @param email The new e-mail address for this contact
   */
  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public int compareTo(AddressEntry other) {
    return name.compareTo(other.name);
  }

  public interface MyInterface{
    void method1();
    void method2();
  }

}
