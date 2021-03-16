package address.data;

import java.util.Objects;

/**
 * A simple class for storing the first and last name of an entity.
 * @author Corneilious Eanes
 * @since March 15, 2021
 */
public class Name implements Comparable<Name> {

  /** The first name */
  private String firstName;
  /** The last name */
  private String lastName;

  /**
   * The "fill-all" constructor for this object. Will initialize the first and last name to the specified arguments.
   * @param firstName The first name for this name
   * @param lastName The last name for this name
   */
  public Name(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  /**
   * The default constructor for this object. Will initialize the first and last names to both be empty strings.
   * @see #Name(String, String)
   */
  public Name() {
    this("", "");
  }

  /**
   * Creates a string representation for this object.
   * @return A string formatted as <pre>{lastName}, {firstName}</pre>
   */
  @Override
  public String toString() {
    return lastName + ", " + firstName;
  }

  /**
   * Checks whether this object is equal to a specified object. Tests for both reference and value equality.
   * @param obj The object to compare against
   * @return True if the specified object is an instance of {@link Name}, and both this object's first and last name,
   * and the comparing object's first and last name match. Returns false otherwise.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Name) {
      Name other = (Name) obj;
      return Objects.equals(other.firstName, firstName) && Objects.equals(other.lastName, lastName);
    }
    return false;
  }

  /**
   * lexicographically compare the last name, then first name of this and another object.
   * @param other The name object to compare against
   * @return A positive integer if this name is lexicographically greater than the argument, zero if both names are
   *         identical, or a negative integer if this name is lexicographically less than the argument.
   * @see String#compareTo(String)
   */
  @Override
  public int compareTo(Name other) {
    return (lastName + firstName).compareTo(other.lastName + other.firstName);
  }

  /**
   * Get the first name of this name object
   * @return This object's first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Set the first name of this name object
   * @param firstName The new first name for this object
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Get the last name of this name object
   * @return This object's last name
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Set the last name of this name object
   * @param lastName The new last name for this object
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

}
