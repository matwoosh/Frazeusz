package pl.edu.agh.ki.frazeusz.model.nlp;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by kamil on 15.12.2016.
 */
public class WordDatabase {
    private static WordDatabase instance = new WordDatabase();
    private LinkedList<LinkedList<String>> inflections = new LinkedList<LinkedList<String>>();
    private LinkedList<LinkedList<String>> synonyms = new LinkedList<LinkedList<String>>();
    private String inflectionsFileName;
    private String synonymsFileName;
    private boolean startedLoading;
    private Thread t1;

    private WordDatabase() {
        inflectionsFileName = "odmiany-demo.txt";
        synonymsFileName = "synonimy.txt";
        startedLoading = false;
        t1 = new Thread();
    }

    private void loadWords(String inflectionsFile, String synonymsFile)  {

        BufferedReader br = null;
        String line = "";

        try{

            br = new BufferedReader(new FileReader(inflectionsFile));
            while ((line = br.readLine()) != null) {

                String[] words = line.split(", ");
                LinkedList<String> wordsList = new LinkedList<String>(Arrays.asList(words));




                inflections.add(wordsList);
            }

            br = new BufferedReader(new FileReader(synonymsFile));
            while ((line = br.readLine()) != null) {

                String[] words = line.split(";");
                LinkedList<String> wordsList = new LinkedList<String>(Arrays.asList(words));

                synonyms.add(wordsList);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void startLoading() {
        if(!instance.startedLoading) {
            instance.startedLoading = true;

                instance.t1 = new Thread(new Runnable() {
                    public void run() {
                        instance.loadWords(instance.inflectionsFileName, instance.synonymsFileName);
                    }
                });
            instance.t1.start();

        }

    }

    static LinkedList<LinkedList<String>> getForms(){
        if(instance.t1.isAlive()) try {
            instance.t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return instance.inflections;
    }
    static LinkedList<LinkedList<String>> getSynonyms(){
        if(instance.t1.isAlive()) try {
            instance.t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return instance.synonyms;
    }




}
