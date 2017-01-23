package patternmatcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class PatternMatcherGUI extends JPanel implements ActionListener {

    private JPanel rows;
    private int rowCount;
    private JButton addWord;

    private JButton debug;
    private JTextArea debugText;

    private static String ADD_WORD_COMMAND = "addWord";
    private static String DEBUG = "debug";


    private PatternMatcherGUI() {
        this.addWord = new JButton("Add word");
        addWord.addActionListener(this);
        addWord.setActionCommand(PatternMatcherGUI.ADD_WORD_COMMAND);

        this.debug = new JButton("DEBUG");
        debug.setActionCommand(PatternMatcherGUI.DEBUG);

        this.rows = new JPanel();
        this.rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        this.rowCount = 0;

        this.debugText = new JTextArea();

        this.addLabels();
        this.addRow();

        this.setLayout(new BorderLayout());
        this.add(rows, BorderLayout.CENTER);
        this.add(addWord, BorderLayout.PAGE_END);

        // Debug purpose panels
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(debug);
        panel.add(debugText);
        this.add(panel, BorderLayout.WEST);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(ADD_WORD_COMMAND)) {
            addRow();
            this.revalidate();
            this.repaint();
            //DEBUG
        }
    }

    public JButton getDebug() {
        return debug;
    }

    public void addRow() {
        this.rowCount++;
        rows.add(new Row(rowCount));
    }

    public java.util.List<Word> getConfig() {
        LinkedList<Word> result = new LinkedList<Word>();
        for (Component row : rows.getComponents()) {
            if (row instanceof Row) {
                Word config = ((Row) row).getConfig();
                result.add(config);
            }

        }
        return result;
    }

    private void addLabels() {
        JPanel labels = new JPanel();
        labels.add(new JLabel("Word"));
        labels.add(new JLabel("Synonyms"));
        labels.add(new JLabel("Forms"));
        rows.add(labels);
    }

    public static JPanel getGUI() {
        return new PatternMatcherGUI();
    }
}
