package address.gui;

import address.AddressBookApplication;
import address.data.AddressEntry;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Dialog window allowing the user to search through the locally stored {@link AddressEntry} objects in
 * {@link AddressBookApplication#getBook()}.
 * @author Corneilious Eanes
 * @author Jenny Vo
 * @since March 15, 2021
 */
public class FindContactDialog extends JDialog {

  private MainPanel parent;
  private JPanel main;
  private JTextField findField;
  private JButton searchButton;
  private JButton cancelButton;
  private JList<String> displayList;
  private JTextArea contactInfoArea;

  private List<AddressEntry> listData;

  public FindContactDialog(MainPanel parent) {
    this.parent = parent;
    main = new JPanel();

    displayList = new JList<>();
    displayList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JScrollPane displayPane = new JScrollPane(displayList);
    contactInfoArea = new JTextArea();
    contactInfoArea.setEditable(false);
    JScrollPane contactInfoPane = new JScrollPane(contactInfoArea);
    JLabel queryLabel = new JLabel("Lookup last name:");
    findField = new JTextField();
    searchButton = new JButton("Search");
    cancelButton = new JButton("Cancel");

    listData = new ArrayList<>();
    displayList.addListSelectionListener(e -> updateContactInfoArea());
    searchButton.addActionListener(e -> searchForContacts());
    cancelButton.addActionListener(e -> closeDialog());

    GroupLayout layout = new GroupLayout(main);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    layout.setHorizontalGroup(layout.createParallelGroup()
      .addGroup(layout.createSequentialGroup()
        .addComponent(displayPane, 50, 150, 200)
        .addComponent(contactInfoPane, 100, 200, 500)
      )
      .addComponent(queryLabel)
      .addGroup(layout.createSequentialGroup()
        .addComponent(findField)
        .addComponent(searchButton)
        .addComponent(cancelButton)
      )
    );
    layout.setVerticalGroup(layout.createSequentialGroup()
      .addGroup(layout.createParallelGroup()
        .addComponent(displayPane)
        .addComponent(contactInfoPane)
      )
      .addComponent(queryLabel)
      .addGroup(layout.createParallelGroup()
        .addComponent(findField)
        .addComponent(searchButton)
        .addComponent(cancelButton)
      )
    );

    main.setLayout(layout);
    setContentPane(main);
    pack();
    setTitle("Find contact");
    setVisible(true);
  }

  private void updateContactInfoArea() {
    int selected = displayList.getSelectedIndex();
    if (selected != -1) {
      AddressEntry entry = listData.get(selected);
      contactInfoArea.setText(entry.toString().replace("\t", ""));
    }
  }

  private void searchForContacts() {
    // check to see if input last name matches any of the entries
    String lastNameQuery = findField.getText();
    if (!lastNameQuery.isEmpty()) {
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
