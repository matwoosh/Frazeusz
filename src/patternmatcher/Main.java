package patternmatcher;

import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {
        // write your code here
        /*
        JFrame gui = new JFrame();
        gui.setSize(new Dimension(500, 500));
        gui.add(PatternMatcherGUI.getGUI());
        gui.setVisible(true);
*/


        LinkedList<Word> list = new LinkedList<Word>();
        list.add(new Word("ala", false, false));
        list.add(new Word("ma", true, false));

        PatternMatcher pa = new PatternMatcher(list);
        System.out.println(pa.isMatching("aLa   ma kota"));
    }
}
