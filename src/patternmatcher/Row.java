package patternmatcher;

import javax.swing.*;

public class Row extends JPanel {

    private JTextField word;
    private JCheckBox synonyms;
    private JCheckBox forms;

    Row(int num) {
        this.word = new JTextField(15);
        this.synonyms = new JCheckBox();
        this.forms = new JCheckBox();

        this.add(new JLabel(String.valueOf(num)));
        this.add(word);
        this.add(synonyms);
        this.add(forms);
    }

    Word getConfig() {
        return new Word(word.getText(),
                synonyms.isSelected(),
                forms.isSelected());
    }
}
