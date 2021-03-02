import javax.swing.*;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;
import java.util.UUID;

/**
 * The main class for the address book application.
 * @author Corneilious Eanes
 * @since March 2, 2021
 */
public class AddressBookApplication {

  /**
   * The main entry point into the address book application.
   * @param args This is ignored, please don't use it
   */
  public static void main(String[] args) {
    try {
      Utils.info("Checking SQL driver...");
      Class.forName("oracle.jdbc.OracleDriver");
      Utils.info("Starting address book application...");
      new AddressBookApplication();
    } catch (Exception e) {
      System.out.println("FATAL ERROR OCCURRED, IMMEDIATELY EXITING APPLICATION");
      System.out.println("[ERROR] " + e.getMessage());
      e.printStackTrace(System.out);
    }
    Utils.info("Setup complete");
  }

  private AddressBook book;
  private Connection conn;

  public AddressBookApplication() throws RuntimeException {
    instance = this;
    book = new AddressBook();

    String username;
    String password;
    try (FileReader reader = new FileReader("credentials.txt")) {
      Scanner scanner = new Scanner(reader);
      username = scanner.nextLine();
      password = scanner.nextLine();
    } catch (IOException e) {
      throw new RuntimeException("Could not read credentials", e);
    }

    try {
      Utils.info("Establishing connection to database server...");
      conn = DriverManager.getConnection("jdbc:oracle:thin:@adcsdb01.csueastbay.edu:1521:mcspdb.ad.csueastbay.edu",
        username, password);
      Utils.info("Connection successful");
    } catch (SQLException e) {
      throw new RuntimeException("Could not establish connection to database server", e);
    }
    Utils.info("Initializing contacts in address book...");
    refreshContactsList();
    Utils.info("Read {0} contacts", book.count());

    Utils.info("Starting Swing application...");
    JFrame frame = new JFrame("Address Book Application");
    frame.setContentPane(new MainPanel());
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }

  public AddressBook getBook() {
    return book;
  }

  public Connection getConnection() {
    return conn;
  }

  public void refreshContactsList() {
    try {
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM ADDRESSENTRYTABLE");
      while (rs.next()) {
        AddressEntry contact = new AddressEntry(
          UUID.fromString(rs.getString("ID")),
          new Name(
            rs.getString("FIRSTNAME"),
            rs.getString("LASTNAME")
          ),
          new Address(
            rs.getString("STREET"),
            rs.getString("CITY"),
            rs.getString("STATE"),
            rs.getInt("ZIP")
          ),
          rs.getString("PHONE"),
          rs.getString("EMAIL")
        );
        book.add(contact);
        System.out.printf("INFO - Contact added: %s%n", contact.getName());
      }
    } catch (SQLException e) {
      throw new RuntimeException("Could not initialize contacts list", e);
    }
  }

  public UUID addContact(AddressEntry contact) {
    UUID id = UUID.randomUUID();
    try {
      PreparedStatement stmt = conn.prepareStatement("INSERT INTO ADDRESSENTRYTABLE VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
      stmt.setString(1, id.toString());
      stmt.setString(2, contact.getName().getFirstName());
      stmt.setString(3, contact.getName().getLastName());
      stmt.setString(4, contact.getAddress().getStreet());
      stmt.setString(5, contact.getAddress().getCity());
      stmt.setString(6, contact.getAddress().getState());
      stmt.setInt(7, contact.getAddress().getZip());
      stmt.setString(8, contact.getPhone());
      stmt.setString(9, contact.getEmail());
      stmt.execute();
      contact.setId(id);
      if (!book.add(contact)) {
        Utils.warn("Cache mismatch: Could not add contact to internal cache: {0} ({1})", contact.getId(), contact.getName());
      }
      Utils.info("Added new contact to database: {0} ({1})", id, contact.getName());
    } catch (SQLException e) {
      throw new RuntimeException("Could not add contact", e);
    }
    return id;
  }

  public void removeContact(UUID id) {
    try {
      PreparedStatement stmt = conn.prepareStatement("DELETE FROM ADDRESSENTRYTABLE WHERE id=?");
      stmt.setString(1, id.toString());
      stmt.execute();
      if (!book.remove(id)) {
        Utils.warn("Cache mismatch: attempted to remove entry from internal cache: {0}", id);
      }
      Utils.info("Removed contact from database: {0}", id);
    } catch (SQLException e) {
      throw new RuntimeException("Could not remove contact", e);
    }
  }

  private static AddressBookApplication instance;

  public static AddressBookApplication getInstance() {
    return instance;
  }

}
