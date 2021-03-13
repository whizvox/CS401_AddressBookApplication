package address.gui;

import address.AddressBookApplication;
import address.data.AddressEntry;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Corneilious Eanes, Jenny Vo
 * @since March 12, 2021
 */
public class FindContactDialog extends JDialog {

  private MainPanel parent;
  private JPanel main;
  private JTextField findField;
  private JButton searchButton;
  private JButton cancelButton;
  private JList<String> displayList;

  private List<AddressEntry> listData;

  public FindContactDialog(MainPanel parent) {
    this.parent = parent;
    main = new JPanel();
    main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
    main.add(new JLabel("Lookup Last Name: "));
    main.add(findField = new JTextField());
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(searchButton = new JButton("Search"));
    buttonPanel.add(cancelButton = new JButton("Cancel"));
    main.add(buttonPanel);
    listData = new ArrayList<>();
    displayList = new JList<>();
    main.add(new JScrollPane(displayList));
    searchButton.addActionListener(e -> searchForContacts());
    cancelButton.addActionListener(e -> closeDialog());

    setContentPane(main);
    pack();
    setVisible(true);
  }

  private void searchForContacts() {
    // check to see if input last name matches any of the entries
    String lastNameQuery = findField.getText();
    if (!findField.getText().isEmpty()) {
      List<AddressEntry> contacts = AddressBookApplication.getInstance().getBook().find(lastNameQuery);
      listData.clear();
      listData.addAll(contacts);
      displayList.setListData(contacts.stream().map(entry -> entry.getName().toString()).toArray(String[]::new));
    }
  }

  private void closeDialog() {
    dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
  }

}
