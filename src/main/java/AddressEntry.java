import java.util.Objects;

/**
 * A data class for storing elements of an address entry.
 *
 * @author Corneilious Eanes
 * @since February 22, 2021
 */
public class AddressEntry {

  private String firstName;
  private String lastName;
  private Address address;
  private String phone;
  private String email;

  /**
   * The "fill-all" constructor of the address entry class. Every passed argument will initialize the corresponding
   * field as-is.
   * @param firstName The first name
   * @param lastName The last name
   * @param address The address
   * @param phone The phone number
   * @param email The e-mail address
   */
  public AddressEntry(String firstName, String lastName, Address address, String phone, String email) {
    this.firstName = firstName;
    this.lastName = lastName;
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
    this("", "", new Address(), "", "");
  }

  /**
   * Creates a string representation of all the data points in this entry. The output will be formatted like this:
   * <pre>
   * ${lastName}, ${firstName}
   *     ${street}
   *     ${city}, ${state} ${zip}
   *     ${email}
   *     ${phone}
   * </pre>
   * Example:
   * <pre>
   * Smith, John
   *     123 Main Street
   *     San Francisco, CA 12345
   *     john.smith@example.com
   *     555-555-1234
   * </pre>
   * @return A fully string-formatted address entry.
   * @see Address#toString()
   */
  @Override
  public String toString() {
    return lastName + ", " + firstName + '\n' +
      '\t' + address.getStreet() + '\n' +
      '\t' + address.getCity() + ", " + address.getState() + " " + address.getZip() + '\n' +
      '\t' + email + '\n' +
      '\t' + phone;
  }

  /**
   * To test for value or reference equality. Will return true if all member fields equals that of the other object,
   * assuming it's of the same instance type.
   * @param o The object to test against.
   * @return Whether the specified object refers to the same reference as this object or if all of its fields equals
   * that of this object.
   */
  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof AddressEntry) {
      AddressEntry entry = (AddressEntry) o;
      return Objects.equals(entry.firstName, firstName) && Objects.equals(entry.lastName, lastName) &&
        Objects.equals(entry.address, address) && Objects.equals(entry.phone, phone) &&
        Objects.equals(entry.email, email);
    }
    return false;
  }

  /**
   * Get the first name of this contact.
   * @return This contact's first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Set the first name of this contact.
   * @param firstName The new first name for this contact
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Get the last name of this contact.
   * @return This contact's last name
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Set the last name of this contact.
   * @param lastName The new last name for this contact
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
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

}
