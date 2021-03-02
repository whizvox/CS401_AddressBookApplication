import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author Corneilious Eanes
 * @since March 1, 2021
 */
public class MainPanel extends JPanel {

  private JButton displayButton;
  private JButton newButton;
  private JButton removeButton;
  private JButton readButton;
  private JScrollPane displayPane;
  private JTextArea display;

  public MainPanel() {
    displayButton = new JButton("Display");
    newButton = new JButton("New");
    removeButton = new JButton("Remove");
    readButton = new JButton("Read from file");

    display = new JTextArea();
    display.setEditable(false);
    displayPane = new JScrollPane(display);

    displayButton.addActionListener(e -> displayContacts());
    newButton.addActionListener(e -> new CreateContactDialog());
    removeButton.addActionListener(e -> removeContact());
    readButton.addActionListener(e -> readEntriesFromFile());

    GroupLayout layout = new GroupLayout(this);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    setLayout(layout);
    layout.setHorizontalGroup(layout.createSequentialGroup()
      .addGroup(layout.createParallelGroup()
        .addComponent(displayButton)
        .addComponent(newButton)
        .addComponent(removeButton)
        .addComponent(readButton)
      )
      .addComponent(displayPane)
    );
    layout.setVerticalGroup(layout.createParallelGroup()
      .addGroup(layout.createSequentialGroup()
        .addComponent(displayButton)
        .addComponent(newButton)
        .addComponent(removeButton)
        .addComponent(readButton)
      )
      .addComponent(displayPane)
    );
    setPreferredSize(new Dimension(450, 600));
  }

  private void displayContacts() {
    display.setText("");
    AddressBookApplication.getAddressBook().find("").forEach(entry -> {
      display.append(entry.toString());
      display.append("\n");
    });
  }

  private void removeContact() {

  }

  private void readEntriesFromFile() {
    JFileChooser fileChooser = new JFileChooser(new File(".").getAbsolutePath());
    int result = fileChooser.showOpenDialog(MainPanel.this);
    if (result == JFileChooser.APPROVE_OPTION) {
      try {
        AddressBook ab = AddressBookApplication.getAddressBook();
        int prev = ab.count();
        ab.readFromFile(fileChooser.getSelectedFile().getAbsolutePath());
        JOptionPane.showMessageDialog(MainPanel.this, (ab.count() - prev) + " entries read from file");
      } catch (IOException ex) {
        JOptionPane.showMessageDialog(MainPanel.this, ex.getMessage(), "Could not read file!", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
      }
    }
  }

}
