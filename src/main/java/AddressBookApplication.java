import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class AddressBookApplication {

  private static AddressBook ab;

  public static void main(String[] args) throws Exception{
    ab = new AddressBook();
    init("AddressInputDataFile.txt");
  }

  public static void init(String fileName) throws IOException {
    try (FileInputStream in = new FileInputStream(fileName)) {
      Scanner scanner = new Scanner(in);
      while (scanner.hasNext()) {
        String firstName = scanner.nextLine();
        String lastName = scanner.nextLine();
        String street = scanner.nextLine();
        String city = scanner.nextLine();
        String state = scanner.nextLine();
        int zip = scanner.nextInt();
        scanner.nextLine();
        String email = scanner.nextLine();
        String phone = scanner.nextLine();
        Address address = new Address(street, city, state, zip);
        AddressEntry entry = new AddressEntry(firstName, lastName, address, phone, email);
        ab.add(entry);
        System.out.println("Address entry added to book:");
        System.out.println(entry);
        System.out.println();
      }
    }
  }

}
