package address.gui;

import address.AddressBook;
import address.AddressBookApplication;
import address.Utils;
import address.data.Address;
import address.data.AddressEntry;
import address.data.Name;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

/**
 * Dialog window allowing the user to update pre-existing entries in the remote database. Any successful updates will
 * be updated in the parent window.
 * @author Corneilious Eanes
 * @since March 15, 2021
 */
public class UpdateContactDialog extends JDialog {

  private MainPanel parent;
  private JPanel main;
  private AddressEntry origEntry;
  private JTextField firstNameField;
  private JTextField lastNameField;
  private JTextField streetField;
  private JTextField cityField;
  private JTextField stateField;
  private JTextField zipField;
  private JTextField emailField;
  private JTextField phoneField;
  private JButton updateButton;
  private JButton cancelButton;

  public UpdateContactDialog(MainPanel parent, AddressEntry entry) {
    this.parent = parent;
    this.origEntry = entry;
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
    main.add(updateButton = new JButton("Update"));
    main.add(cancelButton = new JButton("Cancel"));
    main.setPreferredSize(new Dimension(300, 400));

    updateButton.addActionListener(e -> updateContactInformation());
    cancelButton.addActionListener(e -> closeDialog());

    firstNameField.setText(origEntry.getName().getFirstName());
    lastNameField.setText(origEntry.getName().getLastName());
    streetField.setText(origEntry.getAddress().getStreet());
    cityField.setText(origEntry.getAddress().getCity());
    stateField.setText(origEntry.getAddress().getState());
    zipField.setText(Integer.toString(origEntry.getAddress().getZip()));
    phoneField.setText(origEntry.getPhone());
    emailField.setText(origEntry.getEmail());

    setContentPane(main);
    pack();
    setVisible(true);
  }

  private void closeDialog() {
    dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
  }

  private void updateContactInformation() {
    AddressEntry updatedEntry;
    try {
      updatedEntry = createContact();
    } catch (IllegalArgumentException e) {
      JOptionPane.showMessageDialog(this, e.getMessage(), "Could not update contact", JOptionPane.ERROR_MESSAGE);
      return;
    }
    if (updatedEntry.equals(origEntry)) {
      JOptionPane.showMessageDialog(this, "No changes detected. Database has not been updated.", "No changes detected", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    try {
      AddressBookApplication.getInstance().updateContact(updatedEntry);
    } catch (SQLException e) {
      Utils.error(e, "Could not update contact information for %s (%s)", updatedEntry.getName(), updatedEntry.getId());
      JOptionPane.showMessageDialog(this, "Could not update contact in remote database! Check the console for more details.", "Could not update contact", JOptionPane.ERROR_MESSAGE);
      return;
    }
    AddressBook book = AddressBookApplication.getInstance().getBook();
    book.remove(updatedEntry.getId());
    book.add(updatedEntry);
    // update contacts list in parent
    parent.displayContacts();
    closeDialog();
  }

  private AddressEntry createContact() throws IllegalArgumentException {
    int zip;
    try {
      zip = Integer.parseInt(zipField.getText());
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Zip code must be 5 numerical digits");
    }
    AddressEntry entry = new AddressEntry(origEntry.getId(),
      new Name(
        firstNameField.getText(),
        lastNameField.getText()
      ),
      new Address(
        streetField.getText(),
        cityField.getText(),
        stateField.getText(),
        zip
      ),
      phoneField.getText(),
      emailField.getText()
    );
    Utils.validateAddressEntry(entry);
    return entry;
  }

}
