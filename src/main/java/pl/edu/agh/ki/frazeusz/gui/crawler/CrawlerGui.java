package pl.edu.agh.ki.frazeusz.gui.crawler;

import pl.edu.agh.ki.frazeusz.gui.GuiComponent;

import javax.swing.*;

/**
 * Created by matwoosh on 14/01/2017.
 */
public class CrawlerGui extends JPanel implements GuiComponent<CrawlerConfiguration> {

    public static JPanel getGUI() {
        return new CrawlerGui();
    }

    public CrawlerConfiguration getConfiguration() {
        //TODO implement method
        return null;
    }
}
