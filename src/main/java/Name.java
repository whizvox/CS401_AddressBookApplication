import java.util.Objects;

/**
 * @author Corneilious Eanes
 * @since March 2, 2021
 */
public class Name {

  private String firstName;
  private String lastName;

  public Name(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public Name() {
    this("", "");
  }

  @Override
  public String toString() {
    return lastName + ", " + firstName;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Name) {
      Name other = (Name) obj;
      return Objects.equals(other.firstName, firstName) && Objects.equals(other.lastName, lastName);
    }
    return false;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

}
