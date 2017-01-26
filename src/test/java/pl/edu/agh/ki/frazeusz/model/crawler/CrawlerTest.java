package pl.edu.agh.ki.frazeusz.model.crawler;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import pl.edu.agh.ki.frazeusz.gui.crawler.CrawlerConfiguration;
import pl.edu.agh.ki.frazeusz.gui.pm.PatternMatcherGui;
import pl.edu.agh.ki.frazeusz.model.monitor.Monitor;
import pl.edu.agh.ki.frazeusz.model.nlp.NLProcessor;
import pl.edu.agh.ki.frazeusz.model.parser.IParser;
import pl.edu.agh.ki.frazeusz.model.parser.Parser;
import pl.edu.agh.ki.frazeusz.model.ploter.Ploter;
import pl.edu.agh.ki.frazeusz.model.pm.PatternMatcher;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by matwoosh on 26/01/2017.
 */
public class CrawlerTest {
    private Crawler crawler;
    private Parser parser;
    private CrawlerConfiguration.Builder builder;
    Constructor<CrawlerConfiguration> constructor;

    public CrawlerTest() {
        List<PatternMatcher> pmList = new LinkedList<>();
        pmList.add(new PatternMatcher(new NLProcessor(), new Ploter(), new PatternMatcherGui().getConfiguration()));

        parser = new Parser(pmList);
        crawler = new Crawler(parser, new Monitor());
        builder = new CrawlerConfiguration.Builder();

        constructor = (Constructor<CrawlerConfiguration>) CrawlerConfiguration.class.getDeclaredConstructors()[1];
        constructor.setAccessible(true);
    }

    private ArrayList<String> getLinks() {
        ArrayList<String> urls = new ArrayList<>();
        for (int i = 2; i < 102; i++) {
            urls.add("https://forum.xda-developers.com/galaxy-s3/orig-development/rom-cyanogenmod-13-nightlies-i9300-t3272811/page" + i);
        }
        return urls;
    }

    /**
     * Testing execution time of crawling with given 100 links with 10 posts per link.
     * Time should not exceed 15 minutes. Crawling should not be working after this time.
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    @Test(timeout = 15 * 60 * 1000)     // 15 min
    public void testCrawlingTime() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        // given
        builder.setNestingDepth(1)
                .setThreadsNumber(100)
                .setUrlsToCrawl(getLinks())
                .build();

        CrawlerConfiguration conf = constructor.newInstance(builder);

        // when
        crawler.start(conf);

        // then
        assertEquals(crawler.isCrawling(), false);
    }

    @Test(expected = Exception.class)
    public void testExtractingContentException() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // given
        Downloader downloader = new Downloader(crawler, parser);
        Method method = Downloader.class.getDeclaredMethod("extractUrlData", null);
        method.setAccessible(true);

        // when
        Url url_1 = new Url("aaa", 1);
        Url url_2 = new Url("http://onet.pl", 1);
        Url url_3 = new Url("http://abccc.pl", 1);
        Url url_4 = new Url("https://github.com/500", 1);

        method.invoke(downloader, url_1);
        method.invoke(downloader, url_2);
        method.invoke(downloader, url_3);
        method.invoke(downloader, url_4);

        // then
        assertEquals(crawler.getUnprocessedUrls().contains(url_1), true);
        assertEquals(crawler.getUnprocessedUrls().contains(url_2), false);
        assertEquals(crawler.getUrlsToProcess().contains(url_2), true);
        assertEquals(crawler.getUnprocessedUrls().contains(url_3), true);
        assertEquals(crawler.getUnprocessedUrls().contains(url_4), true);
    }

    @Test
    public void testParsingContent() throws Exception {
        // given
        IParser iparser = Mockito.mock(IParser.class);
        Mockito.when(iparser.parseContent("", "", "http://abc.html")).thenReturn(null);

        Downloader downloader = new Downloader(crawler, iparser);
        Method method = Downloader.class.getDeclaredMethod("parseUrlContent", Url.class);
        method.setAccessible(true);

        // when
        Url url = new Url("http://abc.html", 1);
        method.invoke(downloader, url);

        // then
    }

}