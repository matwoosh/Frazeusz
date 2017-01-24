package pl.edu.agh.ki.frazeusz.gui.pm;

import pl.edu.agh.ki.frazeusz.gui.GuiComponent;
import pl.edu.agh.ki.frazeusz.utilities.Word;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class PatternMatcherGui extends JPanel implements GuiComponent<List<Word>>, ActionListener {

    private JPanel rows;
    private int rowCount;
    private JButton addWord;

    private static String ADD_WORD_COMMAND = "addWord";

    public PatternMatcherGui() {
        this.addWord = new JButton("Add word");
        addWord.addActionListener(this);
        addWord.setActionCommand(PatternMatcherGui.ADD_WORD_COMMAND);


        this.rows = new JPanel();
        this.rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        this.rowCount = 0;

        this.addLabels();
        this.addRow();

        this.setLayout(new BorderLayout());
        this.add(rows, BorderLayout.CENTER);
        this.add(addWord, BorderLayout.PAGE_END);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(ADD_WORD_COMMAND)) {
            addRow();
            this.revalidate();
            this.repaint();
        }
    }

    public void addRow() {
        this.rowCount++;
        rows.add(new Row(rowCount));
    }

    public List<Word> getConfiguration() {
        List<Word> result = new LinkedList<Word>();
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

}
