package pl.edu.agh.ki.frazeusz.gui.pm;

import pl.edu.agh.ki.frazeusz.gui.GuiComponent;
import pl.edu.agh.ki.frazeusz.utilities.Word;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by matwoosh on 16/01/2017.
 */
public class PatternMatcherGui extends JPanel implements GuiComponent<List<Word>>, ActionListener {

    public static JPanel getGUI() {
        return new PatternMatcherGui();
    }

    public List<Word> getConfiguration() {
        //TODO implement method
        return null;
    }

    public void actionPerformed(ActionEvent e) {
        //TODO implement method
    }
}
