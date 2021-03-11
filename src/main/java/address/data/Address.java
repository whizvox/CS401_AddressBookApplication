package address.data;

import java.util.Objects;

/**
 * The address class, used to store address-related fields such as street address, city name, state code, and zip code.
 * @author Corneilious Eanes
 * @since March 11, 2021
 */
public class Address {

  /** Street address */
  private String street;
  /** City name */
  private String city;
  /** State code (length of 2) */
  private String state;
  /** ZIP code (5 digits) */
  private int zip;

  /**
   * The "fill-all" constructor for this class. Each passed argument will initialize each corresponding field as-is.
   * @param street The street address
   * @param city The city name
   * @param state The state code (length 2, not enforced)
   * @param zip The zip code (5 digits, not enforced)
   * @see #Address()
   */
  public Address(String street, String city, String state, int zip) {
    this.street = street;
    this.city = city;
    this.state = state;
    this.zip = zip;
  }

  /**
   * Default constructor, which initializes all string-based fields to an empty string.
   * @see #Address(String, String, String, int)
   */
  public Address() {
    this("", "", "", 0);
  }

  /**
   * Creates a fully-formatted string representation of address. Will be formatted as such:
   * <pre>
   *   $street
   *   $city, $state $zip
   * </pre>
   * Example:
   * <pre>
   *   123 Main Street
   *   San Francisco, CA 12345
   * </pre>
   * @return A formatted string representation of this address.
   */
  @Override
  public String toString() {
    return street + '\n' +
      city + ", " +
      state + ' ' +
      zip;
  }

  /**
   * Used to test for either reference or value equality.
   * @param obj The object to compare against.
   * @return True if either the specified object refers to the same reference as <code>this</code>, or if the specified
   * object's values of <code>street</code>, <code>city</code>, <code>state</code>, and <code>zip</code> equals that of
   * this object's. Will return false otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Address) {
      Address address = (Address) obj;
      return Objects.equals(address.street, street) && Objects.equals(address.city, city) &&
        Objects.equals(address.state, state) && address.zip == zip;
    }
    return false;
  }

  /**
   * Get the street address of this address.
   * @return This address's street address
   */
  public String getStreet() {
    return street;
  }

  /**
   * Set the street address of this address.
   * @param street The new street address for this address
   */
  public void setStreet(String street) {
    this.street = street;
  }

  /**
   * Get the city name of this address.
   * @return This address's city name
   */
  public String getCity() {
    return city;
  }

  /**
   * Set the city name of this address.
   * @param city The new city name for this address
   */
  public void setCity(String city) {
    this.city = city;
  }

  /**
   * Get the state code of this address.
   * @return This address's state code
   */
  public String getState() {
    return state;
  }

  /**
   * Set the state code of this address
   * @param state The new state code for this address
   */
  public void setState(String state) {
    this.state = state;
  }

  /**
   * Get the ZIP code for this address
   * @return This address' ZIP code
   */
  public int getZip() {
    return zip;
  }

  /**
   * Set the ZIP code of this address
   * @param zip The new ZIP code for this address
   */
  public void setZip(int zip) {
    this.zip = zip;
  }
}
