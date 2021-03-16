import address.Utils;
import address.data.Address;
import address.data.AddressEntry;
import address.data.Name;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for {@link Utils}
 * @author Corneilious Eanes
 * @since March 15, 2021
 */
class UtilsTest {

  @Test
  void isEmailValid() {
    assertTrue(Utils.isEmailValid("john@example.com"));
    assertFalse(Utils.isEmailValid("@example.com"));
    assertFalse(Utils.isEmailValid("john@.com"));
    assertFalse(Utils.isEmailValid("john@example."));
    assertFalse(Utils.isEmailValid("john"));
    assertFalse(Utils.isEmailValid("johnexample.com"));
    assertFalse(Utils.isEmailValid(""));
  }

  @Test
  void isPhoneNumberValid() {
    // technically speaking, none of these are valid numbers in a real-world setting, but that's too complicated to
    // test for
    assertTrue(Utils.isPhoneNumberValid("555-555-1234"));
    assertTrue(Utils.isPhoneNumberValid("+1 555 555-1234"));
    assertTrue(Utils.isPhoneNumberValid("+44 555 5555 5555"));
    assertTrue(Utils.isPhoneNumberValid("5555551234"));
    assertFalse(Utils.isPhoneNumberValid("555-1234"));
    assertFalse(Utils.isPhoneNumberValid("5551234"));
    assertFalse(Utils.isPhoneNumberValid("1234"));
    assertFalse(Utils.isPhoneNumberValid(""));
    assertFalse(Utils.isPhoneNumberValid("123456789"));
  }

  @Test
  void validateAddressEntry() {
    AddressEntry entry = new AddressEntry(UUID.fromString("53cdeb31-d68d-4a2b-8be6-cdacff90e315"), new Name("John", "Smith"),
      new Address("123 Main Street", "San Francisco", "CA", 12345), "555-555-1234", "john.smith@example.com");
    // base state should be valid
    assertDoesNotThrow(() -> Utils.validateAddressEntry(entry));

    // first name cannot be empty
    entry.getName().setFirstName("");
    assertThrows(IllegalArgumentException.class, () -> Utils.validateAddressEntry(entry));
    entry.getName().setFirstName("John");
    assertDoesNotThrow(() -> Utils.validateAddressEntry(entry));

    // last name cannot be empty
    entry.getName().setLastName("");
    assertThrows(IllegalArgumentException.class, () -> Utils.validateAddressEntry(entry));
    entry.getName().setLastName("Smith");
    assertDoesNotThrow(() -> Utils.validateAddressEntry(entry));

    // street address cannot be empty
    entry.getAddress().setStreet("");
    assertThrows(IllegalArgumentException.class, () -> Utils.validateAddressEntry(entry));
    entry.getAddress().setStreet("123 Main Street");
    assertDoesNotThrow(() -> Utils.validateAddressEntry(entry));

    // city name cannot be empty
    entry.getAddress().setCity("");
    assertThrows(IllegalArgumentException.class, () -> Utils.validateAddressEntry(entry));
    entry.getAddress().setCity("San Francisco");
    assertDoesNotThrow(() -> Utils.validateAddressEntry(entry));

    // state code must be 2 characters exactly
    entry.getAddress().setState("California");
    assertThrows(IllegalArgumentException.class, () -> Utils.validateAddressEntry(entry));
    entry.getAddress().setState("A");
    assertThrows(IllegalArgumentException.class, () -> Utils.validateAddressEntry(entry));
    entry.getAddress().setState("CA");
    assertDoesNotThrow(() -> Utils.validateAddressEntry(entry));

    // zip code must be 5 digits exactly
    entry.getAddress().setZip(100000);
    assertThrows(IllegalArgumentException.class, () -> Utils.validateAddressEntry(entry));
    entry.getAddress().setZip(99999);
    assertDoesNotThrow(() -> Utils.validateAddressEntry(entry));
    entry.getAddress().setZip(10000);
    assertDoesNotThrow(() -> Utils.validateAddressEntry(entry));
    entry.getAddress().setZip(9999);
    assertThrows(IllegalArgumentException.class, () -> Utils.validateAddressEntry(entry));
    entry.getAddress().setZip(12345);
    assertDoesNotThrow(() -> Utils.validateAddressEntry(entry));

    // phone number must contain at least 10 digits
    entry.setPhone("");
    assertThrows(IllegalArgumentException.class, () -> Utils.validateAddressEntry(entry));
    entry.setPhone("555-555-1234");
    assertDoesNotThrow(() -> Utils.validateAddressEntry(entry));

    // email must have at least 1 character in each part of the address
    entry.setEmail("");
    assertThrows(IllegalArgumentException.class, () -> Utils.validateAddressEntry(entry));
    entry.setEmail("@example.com");
    assertThrows(IllegalArgumentException.class, () -> Utils.validateAddressEntry(entry));
    entry.setEmail("john@example");
    assertThrows(IllegalArgumentException.class, () -> Utils.validateAddressEntry(entry));
    entry.setEmail("john@.com");
    assertThrows(IllegalArgumentException.class, () -> Utils.validateAddressEntry(entry));
    entry.setEmail("");
    assertThrows(IllegalArgumentException.class, () -> Utils.validateAddressEntry(entry));
    entry.setEmail("johnsmith@example.com");
    assertDoesNotThrow(() -> Utils.validateAddressEntry(entry));
  }

}