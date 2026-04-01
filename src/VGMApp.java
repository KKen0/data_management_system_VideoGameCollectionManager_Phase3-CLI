/**
 * Kaveen Amin
 * CEN 3024C - Software Development I CRN: 23586
 * Date: April 1, 2026
 * Class: VGMApp
 *
 * This class contains the main method and acts as the user interface for the
 * Video Game Manager application. It displays the menu, processes user input,
 * and interacts with the GameManager class to perform operations such as
 * adding, removing, updating, and viewing games.
 *
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VGMApp extends JFrame {

    private static final Color BG = new Color(7, 7, 15);
    private static final Color PANEL = new Color(17, 19, 34);
    private static final Color PANEL_ALT = new Color(24, 27, 46);
    private static final Color PRIMARY = new Color(0, 255, 200);
    private static final Color SECONDARY = new Color(140, 82, 255);
    private static final Color ACCENT = new Color(234, 245, 255);
    private static final Color MUTED = new Color(145, 159, 196);
    private static final Color BORDER = new Color(67, 73, 118);
    private static final Color SUCCESS = new Color(0, 214, 143);
    private static final Color WARNING = new Color(255, 170, 64);
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 28);
    private static final Font SUBTITLE_FONT = new Font("SansSerif", Font.PLAIN, 13);

    private GameManager manager;
    private final GameTableModel tableModel;
    private final JTable table;
    private final JLabel fileLabel;
    private final JTextField searchField;
    private final JButton selectFileButton;
    private final JButton addButton;
    private final JButton updateButton;
    private final JButton removeButton;
    private final JButton backlogButton;
    private final JButton refreshButton;
    private final TableRowSorter<GameTableModel> sorter;

    public VGMApp() {
        super("Gamer Vault - Video Game Collection Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1180, 700));
        setLocationRelativeTo(null);

        tableModel = new GameTableModel();
        table = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        fileLabel = new JLabel("No save file loaded");
        searchField = new JTextField(20);
        searchField.setBackground(new Color(10, 13, 23));
        searchField.setForeground(ACCENT);
        searchField.setCaretColor(PRIMARY);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(SECONDARY, 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
        selectFileButton = createPrimaryButton("Load Save File");
        addButton = createSecondaryButton("Add Game");
        updateButton = createSecondaryButton("Update Game");
        removeButton = createSecondaryButton("Delete Game");
        backlogButton = createSecondaryButton("Export Backlog");
        refreshButton = createSecondaryButton("Refresh Library");

        buildUi();
        bindActions();
        setControlsEnabled(false);
    }

    /**
     * method: buildUi
     * purpose: creates and organizes the main user interface components for the application window
     * parameters: none
     * return: void
     */
    private void buildUi() {
        JPanel root = new JPanel(new BorderLayout(16, 16));
        root.setBorder(new EmptyBorder(16, 16, 16, 16));
        root.setBackground(BG);
        setContentPane(root);

        JPanel headerCard = new JPanel(new BorderLayout(16, 12));
        headerCard.setBackground(PANEL);
        headerCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(SECONDARY, 2, true),
                new EmptyBorder(16, 18, 16, 18)
        ));

        JLabel title = new JLabel("GAMER VAULT");
        title.setFont(TITLE_FONT);
        title.setForeground(PRIMARY);

        JLabel subtitle = new JLabel("Track your backlog, sessions, and platform library like a true player.");
        subtitle.setFont(SUBTITLE_FONT);
        subtitle.setForeground(MUTED);

        fileLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        fileLabel.setForeground(ACCENT);

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(subtitle);
        titlePanel.add(Box.createVerticalStrut(8));
        titlePanel.add(fileLabel);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);
        JLabel searchLabel = new JLabel("Search Library");
        searchLabel.setForeground(ACCENT);
        rightPanel.add(searchLabel);
        searchField.setPreferredSize(new Dimension(240, 38));
        rightPanel.add(searchField);
        rightPanel.add(selectFileButton);

        headerCard.add(titlePanel, BorderLayout.WEST);
        headerCard.add(rightPanel, BorderLayout.EAST);

        JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonBar.setOpaque(false);
        buttonBar.add(addButton);
        buttonBar.add(updateButton);
        buttonBar.add(removeButton);
        buttonBar.add(backlogButton);
        buttonBar.add(refreshButton);

        JPanel topSection = new JPanel(new BorderLayout(0, 12));
        topSection.setOpaque(false);
        topSection.add(headerCard, BorderLayout.NORTH);
        topSection.add(buttonBar, BorderLayout.SOUTH);
        root.add(topSection, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(PANEL_ALT);
        centerPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(PRIMARY, 2, true),
                new EmptyBorder(10, 10, 10, 10)
        ));

        configureTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(11, 13, 24));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        root.add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * method: configureTable
     * purpose: configures the JTable appearance, column behavior, and custom renderers for displaying game data
     * parameters: none
     * return: void
     */
    private void configureTable() {
        table.setRowHeight(30);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(new Color(43, 49, 78));
        table.setShowGrid(true);
        table.setBackground(new Color(11, 13, 24));
        table.setForeground(ACCENT);
        table.setSelectionBackground(new Color(56, 29, 108));
        table.setSelectionForeground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(18, 21, 37));
        header.setForeground(PRIMARY);
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        header.setBorder(new LineBorder(SECONDARY, 1));
        header.setReorderingAllowed(false);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);

        table.getColumnModel().getColumn(0).setPreferredWidth(55);
        table.getColumnModel().getColumn(0).setMaxWidth(65);
        table.getColumnModel().getColumn(1).setPreferredWidth(75);
        table.getColumnModel().getColumn(5).setCellRenderer(center);
        table.getColumnModel().getColumn(6).setCellRenderer(center);
        table.getColumnModel().getColumn(7).setCellRenderer(new StatusCellRenderer());
        table.getColumnModel().getColumn(8).setCellRenderer(center);
    }

    /**
     * method: bindActions
     * purpose: connects buttons and search field events to their corresponding application actions
     * parameters: none
     * return: void
     */
    private void bindActions() {
        selectFileButton.addActionListener(e -> chooseFile());
        addButton.addActionListener(e -> showGameDialog(null));
        updateButton.addActionListener(e -> updateSelectedGame());
        removeButton.addActionListener(e -> removeSelectedGames());
        backlogButton.addActionListener(e -> exportBacklog());
        refreshButton.addActionListener(e -> refreshTable());
        searchField.getDocument().addDocumentListener(new SimpleDocumentListener(this::applySearchFilter));
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(PRIMARY);
        button.setForeground(new Color(7, 7, 15));
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(0, 180, 142), 1, true),
                new EmptyBorder(10, 16, 10, 16)
        ));
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(36, 27, 61));
        button.setForeground(ACCENT);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(SECONDARY, 1, true),
                new EmptyBorder(10, 16, 10, 16)
        ));
        return button;
    }

    private void setControlsEnabled(boolean enabled) {
        addButton.setEnabled(enabled);
        updateButton.setEnabled(enabled);
        removeButton.setEnabled(enabled);
        backlogButton.setEnabled(enabled);
        refreshButton.setEnabled(enabled);
        searchField.setEnabled(enabled);
        table.setEnabled(enabled);
    }

    /**
     * method: chooseFile
     * purpose: allows the user to select or create a text file and loads the game data from that file
     * parameters: none
     * return: void
     */
    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Open or create a game data text file");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(new FileNameExtensionFilter("Text Files (*.txt)", "txt"));

        int result = chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();
        String lowerName = file.getName().toLowerCase(Locale.US);
        if (!lowerName.endsWith(".txt")) {
            file = new File(file.getAbsolutePath() + ".txt");
        }

        try {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not open or create that text file.", "File Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        manager = new GameManager(file.getAbsolutePath());
        fileLabel.setText("Loaded save file: " + file.getName());
        refreshTable();
        setControlsEnabled(true);
    }

    /**
     * method: refreshTable
     * purpose: reloads the table with the most current list of games from the game manager
     * parameters: none
     * return: void
     */
    private void refreshTable() {
        if (manager == null) {
            return;
        }
        tableModel.setGames(manager.getAllGames());
        clearSelections();
    }

    private void clearSelections() {
        table.clearSelection();
        tableModel.clearCheckedRows();
    }

    /**
     * method: applySearchFilter
     * purpose: filters the table rows based on the text entered in the search field
     * parameters: none
     * return: void
     */
    private void applySearchFilter() {
        String text = searchField.getText().trim();
        if (text.isEmpty()) {
            sorter.setRowFilter(null);
            return;
        }

        sorter.setRowFilter(new RowFilter<>() {
            @Override
            public boolean include(Entry<? extends GameTableModel, ? extends Integer> entry) {
                String search = text.toLowerCase(Locale.US);
                for (int i = 1; i < entry.getValueCount(); i++) {
                    Object value = entry.getValue(i);
                    if (value != null && value.toString().toLowerCase(Locale.US).contains(search)) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    /**
     * method: showGameDialog
     * purpose: displays the dialog used to add a new game or update an existing game
     * parameters: existingGame - the game being updated, or null if adding a new game
     * return: void
     */
    private void showGameDialog(Game existingGame) {
        JTextField idField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField platformField = new JTextField();
        JTextField genreField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField hoursField = new JTextField();
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"Backlog", "Finished"});
        JTextField yearField = new JTextField();

        if (existingGame != null) {
            idField.setText(String.valueOf(existingGame.getGameId()));
            titleField.setText(existingGame.getTitle());
            platformField.setText(existingGame.getPlatform());
            genreField.setText(existingGame.getGenre());
            priceField.setText(String.valueOf(existingGame.getPurchasePrice()));
            hoursField.setText(String.valueOf(existingGame.getHoursPlayed()));
            statusBox.setSelectedItem(existingGame.isCompleted() ? "Finished" : "Backlog");
            yearField.setText(String.valueOf(existingGame.getReleaseYear()));
        }

        JPanel panel = new JPanel(new GridLayout(0, 2, 8, 8));
        panel.add(new JLabel("Game ID:"));
        panel.add(idField);
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Platform:"));
        panel.add(platformField);
        panel.add(new JLabel("Genre:"));
        panel.add(genreField);
        panel.add(new JLabel("Purchase Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Hours Played:"));
        panel.add(hoursField);
        panel.add(new JLabel("Release Year:"));
        panel.add(yearField);
        panel.add(new JLabel("Play Status:"));
        panel.add(statusBox);

        styleDialogPanel(panel);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                existingGame == null ? "Add Game" : "Update Game",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            int id = Integer.parseInt(idField.getText().trim());
            String title = titleField.getText().trim();
            String platform = platformField.getText().trim();
            String genre = genreField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            double hours = Double.parseDouble(hoursField.getText().trim());
            boolean completed = "Finished".equals(statusBox.getSelectedItem());
            int year = Integer.parseInt(yearField.getText().trim());

            if (title.isEmpty() || platform.isEmpty() || genre.isEmpty()) {
                throw new IllegalArgumentException("Text fields cannot be empty.");
            }
            if (id <= 0 || price < 0 || hours < 0 || year < 1950 || year > 2100) {
                throw new IllegalArgumentException("Please enter valid values.");
            }

            if (existingGame == null) {
                if (!manager.addGame(new Game(id, title, platform, genre, price, hours, completed, year))) {
                    JOptionPane.showMessageDialog(this, "Game ID already exists.", "Add Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                int currentId = existingGame.getGameId();
                if (!manager.updateGameId(currentId, id)) {
                    JOptionPane.showMessageDialog(this, "Could not update Game ID. It may already exist.", "Update Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                manager.updateGameTitle(id, title);
                manager.updateGamePlatform(id, platform);
                manager.updateGameGenre(id, genre);
                manager.updateGamePrice(id, price);
                manager.updateGameHours(id, hours);
                manager.updateGameCompleted(id, completed);
                manager.updateGameYear(id, year);
            }

            refreshTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid values for all fields.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * method: updateSelectedGame
     * purpose: retrieves the selected game from the table and opens the update dialog for that game
     * parameters: none
     * return: void
     */
    private void updateSelectedGame() {
        int selectedViewRow = table.getSelectedRow();
        if (selectedViewRow < 0) {
            JOptionPane.showMessageDialog(this, "Select one game row to update.", "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedViewRow);
        Game selectedGame = tableModel.getGameAt(modelRow);
        showGameDialog(selectedGame);
    }

    /**
     * method: removeSelectedGames
     * purpose: removes one or more selected games from the application after user confirmation
     * parameters: none
     * return: void
     */
    private void removeSelectedGames() {
        List<Integer> selectedIds = tableModel.getCheckedGameIds();
        if (selectedIds.isEmpty()) {
            int selectedViewRow = table.getSelectedRow();
            if (selectedViewRow >= 0) {
                int modelRow = table.convertRowIndexToModel(selectedViewRow);
                selectedIds.add(tableModel.getGameAt(modelRow).getGameId());
            }
        }

        if (selectedIds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Check at least one row or select one row to remove.", "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete selected game(s) from this library?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        for (Integer id : selectedIds) {
            manager.removeGame(id);
        }

        refreshTable();
    }

    /**
     * method: exportBacklog
     * purpose: exports the unfinished games backlog report to a user-selected file format
     * parameters: none
     * return: void
     */
    private void exportBacklog() {
        String[] formats = {"csv", "txt"};
        String format = (String) JOptionPane.showInputDialog(
                this,
                "Export backlog report in which format?",
                "Backlog Report",
                JOptionPane.QUESTION_MESSAGE,
                null,
                formats,
                formats[0]
        );

        if (format == null) {
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save Backlog Report");
        chooser.setSelectedFile(new File("backlog_report." + format));
        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File target = chooser.getSelectedFile();
        String path = target.getAbsolutePath();
        if (!path.toLowerCase(Locale.US).endsWith("." + format)) {
            path += "." + format;
        }

        if (manager.exportBacklog(path, format)) {
            JOptionPane.showMessageDialog(this, "Backlog report exported successfully.", "Export Complete", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Could not export backlog report.", "Export Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleDialogPanel(JComponent component) {
        component.setBackground(PANEL);
        component.setForeground(ACCENT);
        for (Component child : component.getComponents()) {
            child.setBackground(PANEL);
            child.setForeground(ACCENT);
            if (child instanceof JTextField field) {
                field.setBackground(new Color(10, 13, 23));
                field.setForeground(ACCENT);
                field.setCaretColor(PRIMARY);
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(SECONDARY, 1, true),
                        new EmptyBorder(6, 8, 6, 8)
                ));
            } else if (child instanceof JComboBox<?> box) {
                box.setBackground(new Color(10, 13, 23));
                box.setForeground(ACCENT);
            } else if (child instanceof JComponent jc) {
                styleDialogPanel(jc);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VGMApp app = new VGMApp();
            app.setVisible(true);
        });
    }

    private static class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            String text = value == null ? "" : value.toString();
            label.setText(text);
            if (!isSelected) {
                if ("Finished".equals(text)) {
                    label.setForeground(SUCCESS);
                } else {
                    label.setForeground(WARNING);
                }
                label.setBackground(new Color(11, 13, 24));
            }
            return label;
        }
    }

    private static class GameTableModel extends AbstractTableModel {
        private final String[] columns = {"Select", "Game ID", "Title", "Platform", "Genre", "Price", "Hours", "Status", "Year"};
        private List<Game> games = new ArrayList<>();
        private List<Boolean> checkedRows = new ArrayList<>();

        public void setGames(List<Game> games) {
            this.games = new ArrayList<>(games);
            this.checkedRows = new ArrayList<>();
            for (int i = 0; i < games.size(); i++) {
                checkedRows.add(false);
            }
            fireTableDataChanged();
        }

        public Game getGameAt(int row) {
            return games.get(row);
        }

        public List<Integer> getCheckedGameIds() {
            List<Integer> ids = new ArrayList<>();
            for (int i = 0; i < games.size(); i++) {
                if (Boolean.TRUE.equals(checkedRows.get(i))) {
                    ids.add(games.get(i).getGameId());
                }
            }
            return ids;
        }

        public void clearCheckedRows() {
            for (int i = 0; i < checkedRows.size(); i++) {
                checkedRows.set(i, false);
            }
            if (!checkedRows.isEmpty()) {
                fireTableRowsUpdated(0, checkedRows.size() - 1);
            }
        }

        @Override
        public int getRowCount() {
            return games.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) {
                return Boolean.class;
            }
            if (columnIndex == 1 || columnIndex == 8) {
                return Integer.class;
            }
            if (columnIndex == 5 || columnIndex == 6) {
                return Double.class;
            }
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 0;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Game game = games.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> checkedRows.get(rowIndex);
                case 1 -> game.getGameId();
                case 2 -> game.getTitle();
                case 3 -> game.getPlatform();
                case 4 -> game.getGenre();
                case 5 -> game.getPurchasePrice();
                case 6 -> game.getHoursPlayed();
                case 7 -> game.isCompleted() ? "Finished" : "Backlog";
                case 8 -> game.getReleaseYear();
                default -> null;
            };
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                checkedRows.set(rowIndex, (Boolean) aValue);
                fireTableCellUpdated(rowIndex, columnIndex);
            }
        }
    }

    @FunctionalInterface
    private interface ChangeAction {
        void run();
    }

    private static class SimpleDocumentListener implements javax.swing.event.DocumentListener {
        private final ChangeAction action;

        public SimpleDocumentListener(ChangeAction action) {
            this.action = action;
        }

        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            action.run();
        }

        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            action.run();
        }

        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            action.run();
        }
    }
}
