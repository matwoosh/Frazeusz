package patternmatcher;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        // write your code here
        JFrame gui = new JFrame();
        gui.setSize(new Dimension(500, 500));
        gui.add(PatternMatcherGUI.getGUI());
        gui.setVisible(true);
    }
}
