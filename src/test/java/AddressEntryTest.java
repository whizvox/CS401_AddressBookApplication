import address.data.Address;
import address.data.AddressEntry;
import address.data.Name;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for {@link AddressEntry}
 * @author Corneilious Eanes
 * @since March 11, 2021
 */
class AddressEntryTest {

  private static AddressEntry createA() {
    return new AddressEntry(UUID.fromString("53cdeb31-d68d-4a2b-8be6-cdacff90e315"), new Name("John", "Smith"),
      new Address("123 Main Street", "San Francisco", "CA", 12345), "555-555-1234", "john.smith@example.com");
  }

  private static AddressEntry createB() {
    return new AddressEntry(UUID.fromString("e1052539-c5e0-457a-bf10-cbe591c2c53f"), new Name("Jane", "Doe"),
      new Address("456 Elm Avenue", "Seattle", "WA", 67890), "555-123-4567", "janedoe@website.org");
  }

  @Test
  void _toString() {
    assertEquals(createA().toString(), "Smith, John\n\t123 Main Street\n\tSan Francisco, CA 12345\n\tjohn.smith@example.com\n\t555-555-1234");
    assertEquals(createB().toString(), "Doe, Jane\n\t456 Elm Avenue\n\tSeattle, WA 67890\n\tjanedoe@website.org\n\t555-123-4567");
  }

  @Test
  void _equals() {
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
  void isIdSet() {
    AddressEntry a = createA();
    assertTrue(a.isIdSet());
    a.setId(null);
    assertFalse(a.isIdSet());
    a.setId(UUID.randomUUID());
    assertTrue(a.isIdSet());
  }

  @Test
  void getId() {
    assertEquals(createA().getId(), UUID.fromString("53cdeb31-d68d-4a2b-8be6-cdacff90e315"));
    assertEquals(createB().getId(), UUID.fromString("e1052539-c5e0-457a-bf10-cbe591c2c53f"));
  }

  @Test
  void setId() {
    AddressEntry a = createA();
    final UUID n0 = UUID.fromString("5336cafc-80d8-43b2-9fdb-98e3acebf137");
    a.setId(n0);
    assertEquals(a.getId(), n0);
    final UUID n1 = UUID.fromString("475d6103-85af-4378-934b-01c4a6bf724a");
    a.setId(n1);
    assertEquals(a.getId(), n1);
  }

  @Test
  void getName() {
    assertEquals(createA().getName(), new Name("John", "Smith"));
    assertEquals(createB().getName(), new Name("Jane", "Doe"));
  }

  @Test
  void setName() {
    AddressEntry entry = createA();
    final Name n0 = new Name("Alexander", "Hamilton");
    entry.setName(n0);
    assertEquals(entry.getName(), n0);
    final Name n1 = new Name("Jonathan", "Joestar");
    entry.setName(n1);
    assertEquals(entry.getName(), n1);
  }

  @Test
  void getAddress() {
    assertEquals(createA().getAddress(), new Address("123 Main Street", "San Francisco", "CA", 12345));
    assertEquals(createB().getAddress(), new Address("456 Elm Avenue", "Seattle", "WA", 67890));
  }

  @Test
  void setAddress() {
    AddressEntry entry = createA();
    final Address newAddress1 = new Address("456 Elm Avenue", "Seattle", "WA", 67890);
    entry.setAddress(newAddress1);
    assertEquals(entry.getAddress(), newAddress1);
    final Address newAddress2 = new Address("789 Woodward Place", "Philadelphia", "PA", 65432);
    entry.setAddress(newAddress2);
    assertEquals(entry.getAddress(), newAddress2);
  }

  @Test
  void getPhone() {
    assertEquals(createA().getPhone(), "555-555-1234");
    assertEquals(createB().getPhone(), "555-123-4567");
  }

  @Test
  void setPhone() {
    AddressEntry entry = createA();
    final String newPhone1 = "555-555-9876";
    entry.setPhone(newPhone1);
    assertEquals(entry.getPhone(), newPhone1);
    final String newPhone2 = "999-888-7777";
    entry.setPhone(newPhone2);
    assertEquals(entry.getPhone(), newPhone2);
  }

  @Test
  void getEmail() {
    assertEquals(createA().getEmail(), "john.smith@example.com");
    assertEquals(createB().getEmail(), "janedoe@website.org");
  }

  @Test
  void setEmail() {
    AddressEntry entry = createA();
    final String newEmail1 = "johnsmith@example.net";
    entry.setEmail(newEmail1);
    assertEquals(entry.getEmail(), newEmail1);
    final String newEmail2 = "smith.john@mywebsite.us";
    entry.setEmail(newEmail2);
    assertEquals(entry.getEmail(), newEmail2);
  }

}