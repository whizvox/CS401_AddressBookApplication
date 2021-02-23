import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Test cases for {@link AddressEntry}
 * @author Corneilious Eanes
 * @since February 22, 2021
 */
class AddressEntryTest {

  private static AddressEntry createA() {
    return new AddressEntry("John", "Smith", new Address("123 Main Street", "San Francisco", "CA", 12345),
      "555-555-1234", "john.smith@example.com");
  }

  private static AddressEntry createB() {
    return new AddressEntry("Jane", "Doe", new Address("456 Elm Avenue", "Seattle", "WA", 67890), "555-123-4567",
      "janedoe@website.org");
  }

  @Test
  void testToString() {
    assertEquals(createA().toString(), "Smith, John\n\t123 Main Street\n\tSan Francisco, CA 12345\n\tjohn.smith@example.com\n\t555-555-1234");
    assertEquals(createB().toString(), "Doe, Jane\n\t456 Elm Avenue\n\tSeattle, WA 67890\n\tjanedoe@website.org\n\t555-123-4567");
  }

  @Test
  void testEquals() {
    AddressEntry entryA = createA();
    assertEquals(entryA, entryA);
    assertEquals(entryA, createA());
    AddressEntry entryB = createB();
    assertEquals(entryB, entryB);
    assertEquals(entryB, createB());
    assertNotEquals(entryA, entryB);
    assertNotEquals(entryB, entryA);
  }

  @Test
  void testGetFirstName() {
    assertEquals(createA().getFirstName(), "John");
    assertEquals(createB().getFirstName(), "Jane");
  }

  @Test
  void testSetFirstName() {
    AddressEntry entry = createA();
    final String newFirstName1 = "Michael";
    entry.setFirstName(newFirstName1);
    assertEquals(entry.getFirstName(), newFirstName1);
    final String newFirstName2 = "Johnathan";
    entry.setFirstName(newFirstName2);
    assertEquals(entry.getFirstName(), newFirstName2);
  }

  @Test
  void testGetLastName() {
    assertEquals(createA().getLastName(), "Smith");
    assertEquals(createB().getLastName(), "Doe");
  }

  @Test
  void testSetLastName() {
    AddressEntry entry = createA();
    final String newLastName1 = "Doe";
    entry.setLastName(newLastName1);
    assertEquals(entry.getLastName(), newLastName1);
    final String newLastName2 = "Washington";
    entry.setLastName(newLastName2);
    assertEquals(entry.getLastName(), newLastName2);
  }

  @Test
  void testGetAddress() {
    assertEquals(createA().getAddress(), new Address("123 Main Street", "San Francisco", "CA", 12345));
    assertEquals(createB().getAddress(), new Address("456 Elm Avenue", "Seattle", "WA", 67890));
  }

  @Test
  void testSetAddress() {
    AddressEntry entry = createA();
    final Address newAddress1 = new Address("456 Elm Avenue", "Seattle", "WA", 67890);
    entry.setAddress(newAddress1);
    assertEquals(entry.getAddress(), newAddress1);
    final Address newAddress2 = new Address("789 Woodward Place", "Philadelphia", "PA", 65432);
    entry.setAddress(newAddress2);
    assertEquals(entry.getAddress(), newAddress2);
  }

  @Test
  void testGetPhone() {
    assertEquals(createA().getPhone(), "555-555-1234");
    assertEquals(createB().getPhone(), "555-123-4567");
  }

  @Test
  void testSetPhone() {
    AddressEntry entry = createA();
    final String newPhone1 = "555-555-9876";
    entry.setPhone(newPhone1);
    assertEquals(entry.getPhone(), newPhone1);
    final String newPhone2 = "999-888-7777";
    entry.setPhone(newPhone2);
    assertEquals(entry.getPhone(), newPhone2);
  }

  @Test
  void testGetEmail() {
    assertEquals(createA().getEmail(), "john.smith@example.com");
    assertEquals(createB().getEmail(), "janedoe@website.org");
  }

  @Test
  void testSetEmail() {
    AddressEntry entry = createA();
    final String newEmail1 = "johnsmith@example.net";
    entry.setEmail(newEmail1);
    assertEquals(entry.getEmail(), newEmail1);
    final String newEmail2 = "smith.john@mywebsite.us";
    entry.setEmail(newEmail2);
    assertEquals(entry.getEmail(), newEmail2);
  }

}