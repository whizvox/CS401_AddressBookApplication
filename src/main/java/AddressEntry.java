public class AddressEntry {

  private String firstName;
  private String lastName;
  private Address address;
  private String phone;
  private String email;

  public AddressEntry(String firstName, String lastName, Address address, String phone, String email) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.address = address;
    this.phone = phone;
    this.email = email;
  }

  public AddressEntry() {
  }

  @Override
  public String toString() {
    return firstName + " " + lastName + '\n' +
      address.toString() + '\n' +
      email + '\n' +
      phone;
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

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

}
