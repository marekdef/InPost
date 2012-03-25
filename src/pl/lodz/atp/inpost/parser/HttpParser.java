package pl.lodz.atp.inpost.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HttpParser {

    private static final String CSS_WIDTH = "width";
    private static final String CSS_CLASS = "class";
    private static final String CSS_RESULT_CLASS = "sledz-result contenttable";
    private static final String CSS_ERROR_CLASS = "sledzenie-error";
    private static final String PAGE_ENCODING = "utf-8";
    private static final String NO_INFORMATION = "Brak informacji";

    public HttpParser() {
    }

    public String parseHtml( String inpostPage ) {
        Document parse = Jsoup.parse(inpostPage, PAGE_ENCODING);
        Elements elementsByAttributeValue = parse.getElementsByAttributeValue(CSS_CLASS, CSS_RESULT_CLASS);
        if (elementsByAttributeValue.size() > 0) {
            Element first = elementsByAttributeValue.first();
            return first.removeAttr(CSS_WIDTH).outerHtml();
        }
        elementsByAttributeValue = parse.getElementsByClass(CSS_ERROR_CLASS);
        if (elementsByAttributeValue.size() > 0) {
            Element first = elementsByAttributeValue.first();
            return first.html();
        }
        return NO_INFORMATION;
    }
}