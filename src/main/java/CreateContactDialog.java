import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * @author Corneilious Eanes
 * @since March 1, 2021
 */
public class CreateContactDialog extends JDialog {

  private JPanel main;
  private JTextField firstNameField;
  private JTextField lastNameField;
  private JTextField streetField;
  private JTextField cityField;
  private JTextField stateField;
  private JTextField zipField;
  private JTextField emailField;
  private JTextField phoneField;
  private JButton addButton;
  private JButton cancelButton;

  public CreateContactDialog() {
    main = new JPanel();
    main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
    main.add(new JLabel("First name"));
    main.add(firstNameField = new JTextField());
    main.add(new JLabel("Last name"));
    main.add(lastNameField = new JTextField());
    main.add(new JLabel("Street address"));
    main.add(streetField = new JTextField());
    main.add(new JLabel("City name"));
    main.add(cityField = new JTextField());
    main.add(new JLabel("State code"));
    main.add(stateField = new JTextField());
    main.add(new JLabel("Zip code"));
    main.add(zipField = new JTextField());
    main.add(new JLabel("Email address"));
    main.add(emailField = new JTextField());
    main.add(new JLabel("Phone number"));
    main.add(phoneField = new JTextField());
    main.add(addButton = new JButton("Add"));
    // Normally, I'd wrap these buttons in a JPanel and give it a FlowLayout to make them look nicer. However, there's
    // some weird bug where doing that causes the JLabels to badly center themselves.
    addButton.addActionListener(e -> addContact());
    main.add(cancelButton = new JButton("Cancel"));
    cancelButton.addActionListener(e -> closeDialog());
    main.setPreferredSize(new Dimension(300, 400));

    setContentPane(main);
    pack();
    setVisible(true);
  }

  private void closeDialog() {
    dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
  }

  private void addContact() {
    try {
      AddressEntry contact = createContact();
      if (contact.getFirstName().isEmpty() || contact.getLastName().isEmpty() ||
          contact.getAddress().getStreet().isEmpty() || contact.getAddress().getCity().isEmpty() ||
          contact.getAddress().getState().isEmpty() || contact.getPhone().isEmpty() || contact.getEmail().isEmpty()) {
        throw new IllegalArgumentException("All fields must be filled in");
      }
      if (contact.getAddress().getState().length() != 2) {
        throw new IllegalArgumentException("State code must be 2 characters");
      }
      if (contact.getAddress().getZip() < 10000 || contact.getAddress().getZip() > 99999) {
        throw new IllegalArgumentException("Zip code must be a 5-digit number");
      }

      AddressBookApplication.getAddressBook().add(createContact());
      closeDialog();
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this, "Invalid ZIP code: must be a 5-digit number", "Could not add contact!", JOptionPane.ERROR_MESSAGE);
    } catch (IllegalArgumentException e) {
      JOptionPane.showMessageDialog(this, e.getMessage(), "Could not add contact!", JOptionPane.ERROR_MESSAGE);
    }
  }

  private AddressEntry createContact() {
    return new AddressEntry(
      firstNameField.getText(),
      lastNameField.getText(),
      new Address(
        streetField.getText(),
        cityField.getText(),
        stateField.getText(),
        zipField.getText().isEmpty() ? 0 : Integer.parseInt(zipField.getText())
      ),
      phoneField.getText(),
      emailField.getText()
    );
  }

}
