package pl.edu.agh.ki.frazeusz.gui.pm;

import pl.edu.agh.ki.frazeusz.utilities.Word;

import javax.swing.*;

public class Row extends JPanel {

    private JTextField word;
    private JCheckBox synonyms;
    private JCheckBox forms;

    private Word.Builder wordBuilder;

    Row(int num) {
        this.word = new JTextField(15);
        this.synonyms = new JCheckBox();
        this.forms = new JCheckBox();

        this.wordBuilder = Word.builder();

        this.add(new JLabel(String.valueOf(num)));
        this.add(word);
        this.add(synonyms);
        this.add(forms);
    }

    Word getConfig() {
        wordBuilder.setWord(word.getText());
        wordBuilder.withForms(forms.isSelected());
        wordBuilder.withSynonyms(synonyms.isSelected());
        return new Word(this.wordBuilder);
    }
}
