package address.gui;

import address.AddressBookApplication;
import address.data.AddressEntry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

/**
 * The landing page of sorts for the application. Gives access to all other parts of the application.
 * @author Corneilious Eanes
 * @author Mike Langdon
 * @since March 15, 2021
 */
public class MainPanel extends JPanel {

  private JButton displayButton;
  private JButton newButton;
  private JButton updateButton;
  private JButton removeButton;
  private JButton findButton;
  private JButton exitButton;
  private JScrollPane displayPane;
  private JList<String> display;
  private ArrayList<UUID> entryIds;

  public MainPanel() {
    displayButton = new JButton("Display");
    newButton = new JButton("New");
    updateButton = new JButton("Update");
    findButton = new JButton("Find");
    removeButton = new JButton("Remove");
    exitButton = new JButton("Exit");

    display = new JList<>();
    display.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    displayPane = new JScrollPane(display);

    entryIds = new ArrayList<>();

    displayButton.addActionListener(e -> displayContacts());
    newButton.addActionListener(e -> new CreateContactDialog(this));
    updateButton.addActionListener(e -> updateContact());
    findButton.addActionListener(e -> new FindContactDialog(this));
    removeButton.addActionListener(e -> removeContact());
    exitButton.addActionListener(e -> exitApplication());

    GroupLayout layout = new GroupLayout(this);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    setLayout(layout);
    layout.setHorizontalGroup(layout.createSequentialGroup()
      .addGroup(layout.createParallelGroup()
        .addComponent(displayButton)
        .addComponent(newButton)
        .addComponent(updateButton)
        .addComponent(findButton)
        .addComponent(removeButton)
        .addComponent(exitButton)
      )
      .addComponent(displayPane)
    );
    layout.setVerticalGroup(layout.createParallelGroup()
      .addGroup(layout.createSequentialGroup()
        .addComponent(displayButton)
        .addComponent(newButton)
        .addComponent(updateButton)
        .addComponent(findButton)
        .addComponent(removeButton)
        .addComponent(exitButton)
      )
      .addComponent(displayPane)
    );
    setPreferredSize(new Dimension(750, 450));

    displayContacts();
  }

  public void displayContacts() {
    Vector<String> entryNames = new Vector<>();
    entryIds.clear();
    AddressBookApplication.getInstance().getBook().find("").forEach(entry -> {
      entryNames.add(entry.toString().replace("\n", " | ").replace("\t", ""));
      entryIds.add(entry.getId());
    });
    display.setListData(entryNames);
  }

  private void updateContact() {
    int selected = display.getSelectedIndex();
    if (selected != -1) {
      UUID selectedId = entryIds.get(selected);
      AddressEntry entry = AddressBookApplication.getInstance().getBook().get(selectedId);
      if (entry == null) {
        JOptionPane.showMessageDialog(this, "Selected entry does not exist!", "Could not remove contact!", JOptionPane.ERROR_MESSAGE);
      } else {
        new UpdateContactDialog(this, entry);
      }
    }
  }

  private void removeContact() {
    int selected = display.getSelectedIndex();
    if (selected != -1) {
      UUID selectedId = entryIds.get(selected);
      AddressEntry entry = AddressBookApplication.getInstance().getBook().get(selectedId);
      if (entry == null) {
        JOptionPane.showMessageDialog(this, "Selected entry does not exist!", "Could not remove contact!", JOptionPane.ERROR_MESSAGE);
      } else {
        int result = JOptionPane.showConfirmDialog(this, "Are you sure you wish to delete the contact information for " + entry.getName().toString() + "?", "Confirm deletion", JOptionPane.YES_NO_OPTION);
        if (result == 0) {
          AddressBookApplication.getInstance().removeContact(selectedId);
          JOptionPane.showMessageDialog(this, "Contact deleted", "Contact deleted", JOptionPane.INFORMATION_MESSAGE);
          displayContacts();
        }
      }
    }
  }

  private void exitApplication() {
    // Gracefully closes the window
    dispatchEvent(new WindowEvent(AddressBookApplication.getInstance().getFrame(), WindowEvent.WINDOW_CLOSING));
  }

}
