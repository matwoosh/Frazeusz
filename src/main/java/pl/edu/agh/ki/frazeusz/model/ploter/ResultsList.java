package pl.edu.agh.ki.frazeusz.model.ploter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class ResultsList {

    private DefaultTableModel resultTableModel;

    private Object[][] data = {};
    private String[] columns = {"Adres", "Fraza", "Zdanie"};

    public ResultsList() {
        this.resultTableModel = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public JScrollPane createResultTable() {

        JTable table = new JTable(this.resultTableModel);

        TableColumnModel columnModel = table.getColumnModel();

        columnModel.getColumn(0).setPreferredWidth(200);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(400);

        final ListSelectionModel listModel = table.getSelectionModel();
        listModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2){
                    int selectedRow = listModel.getMinSelectionIndex();
                    try {
                        openWebPage(new URL(resultTableModel.getValueAt(selectedRow, 0).toString()));
                    } catch (MalformedURLException exc) {
                        JOptionPane.showMessageDialog(null, "Zły adres, taka strona nie istnieje");
                    }
                }
            }
        });


        table.setRowHeight(24);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(750, 400));
        return scrollPane;
    }

    public void addRow(Result result) {
        this.resultTableModel.addRow(new Object[] {result.URL, result.matchedPhrase, result.sentence});
    }

    private static void openWebPage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void openWebPage(URL url) {
        try {
            openWebPage(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

}