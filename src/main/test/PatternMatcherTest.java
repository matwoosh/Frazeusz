import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import pl.edu.agh.ki.frazeusz.model.nlp.INLProcessor;
import pl.edu.agh.ki.frazeusz.model.nlp.NLProcessor;
import pl.edu.agh.ki.frazeusz.model.ploter.Ploter;
import pl.edu.agh.ki.frazeusz.model.ploter.Result;
import pl.edu.agh.ki.frazeusz.model.pm.PatternMatcher;
import pl.edu.agh.ki.frazeusz.utilities.Word;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by bartoszszafran on 24/01/2017.
 */
public class PatternMatcherTest {

    @Mock private NLProcessor mocked_nlp;
    @Mock private Ploter mocked_ploter;

    private LinkedList<Word> words;
    private LinkedList<String> sentences;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Before
    public void init() {
        this.words = new LinkedList<>();
        this.sentences = new LinkedList<>();
    }

    @Test
    public void test_without_synonyms_forms_should_match() {
        // when
        String url = "http://google.pl";
        String sentence = "To zdanie jest do dopasowania";
        String word1 = "zdanie";
        String word2 = "dopasowania";

        this.words.add(new Word(word1, false, false));
        this.words.add(new Word(word2, false, false));
        this.sentences.add(sentence);

        // given
        PatternMatcher pm = new PatternMatcher(mocked_nlp, mocked_ploter, words);
        pm.processSentences(sentences, url);

        // then
        ArgumentCaptor<Result> argument = ArgumentCaptor.forClass(Result.class);
        verify(mocked_nlp, never()).getForms(any(String.class));
        verify(mocked_nlp, never()).getSynonyms(any(String.class));
        verify(mocked_ploter).addResult(argument.capture());
        assertEquals(url, argument.getValue().URL);
        assertEquals(sentence, argument.getValue().sentence);
        assertEquals(word1 + " " + word2 + " ", argument.getValue().matchedPhrase);
    }

    @Test
    public void test_without_synonyms_forms_should_partially_match() {
         // when
        String url = "http://google.pl";
        String sentence1 = "To zdanie jest do dopasowania";
        String sentence2 = "To zdanie jest do niedopasowania";
        String word1 = "zdanie";
        String word2 = "dopasowania";

        this.words.add(new Word(word1, false, false));
        this.words.add(new Word(word2, false, false));
        this.sentences.add(sentence1);
        this.sentences.add(sentence2);

        // given
        PatternMatcher pm = new PatternMatcher(mocked_nlp, mocked_ploter, words);
        pm.processSentences(sentences, url);

        // then
        ArgumentCaptor<Result> argument = ArgumentCaptor.forClass(Result.class);
        verify(mocked_nlp, never()).getForms(any(String.class));
        verify(mocked_nlp, never()).getSynonyms(any(String.class));
        verify(mocked_ploter).addResult(argument.capture());
        assertEquals(url, argument.getValue().URL);
        assertEquals(sentence1, argument.getValue().sentence);
        assertEquals(word1 + " " + word2 + " ", argument.getValue().matchedPhrase);
        verifyNoMoreInteractions(mocked_ploter);
    }

    @Test
    public void test_with_forms_synonyms_should_match() {
        // when
        String[] nlContent1 = {"auto", "bryka"};
        String[] nlContent2 = {"samochodzie", "samochodami"};
        String url = "http://google.pl";
        String sentence1 = "Auto to inaczej bryka";
        String sentence2 = "Bede czekal w samochodzie";
        String word = "samochod";

        this.words.add(new Word(word, true, true));
        this.sentences.add(sentence1);
        this.sentences.add(sentence2);

        LinkedList<String> nlResult1 = new LinkedList<>(Arrays.asList(nlContent1));
        LinkedList<String> nlResult2 = new LinkedList<>(Arrays.asList(nlContent2));
        when(mocked_nlp.getSynonyms(word)).thenReturn(nlResult1);
        when(mocked_nlp.getForms(word)).thenReturn(nlResult2);

        // given
        PatternMatcher pm = new PatternMatcher(mocked_nlp, mocked_ploter, words);
        pm.processSentences(sentences, url);

        // then
        ArgumentCaptor<Result> argument = ArgumentCaptor.forClass(Result.class);
        verify(mocked_nlp, times(1)).getForms(word);
        verify(mocked_nlp, times(1)).getSynonyms(word);

        verify(mocked_ploter, times(2)).addResult(argument.capture());
        verifyNoMoreInteractions(mocked_ploter);

        List<Result> capturedResults = argument.getAllValues();
        assertEquals(url, capturedResults.get(0).URL);
        assertEquals(sentence1, capturedResults.get(0).sentence);
        assertEquals(word + " ", capturedResults.get(0).matchedPhrase);

        assertEquals(url, capturedResults.get(1).URL);
        assertEquals(sentence2, capturedResults.get(1).sentence);
        assertEquals(word + " ", capturedResults.get(1).matchedPhrase);
    }
}
