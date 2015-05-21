package net.retsat1.starlab.inpost;

import net.retsat1.starlab.inpost.exceptions.JSoupParserException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JSoupParser {

    public static final String UTF_8_ENCODING = "utf-8";

    public static final String ATTRIBUTE_CLASS = "class";

    public static final String SYSTEM_ERROR_CLASS = "system-error";

    public static final String CURRENT_STATUS_IMPORTANT = "current-status important";

    public static final String H3 = "h3";

    public String parse(String inpostPage) throws JSoupParserException {
        Document parse = Jsoup.parse(inpostPage, UTF_8_ENCODING);

        Elements elementsByAttributeValue = parse.getElementsByAttributeValue(ATTRIBUTE_CLASS, CURRENT_STATUS_IMPORTANT);

        if (elementsByAttributeValue.size() > 0) {
            Element currentStatusImportant = elementsByAttributeValue.first();

            Elements h3s = currentStatusImportant.getElementsByTag(H3);

            if(h3s.size() > 0) {
                return h3s.first().html();
            }
        }

        elementsByAttributeValue = parse.getElementsByClass(SYSTEM_ERROR_CLASS);

        if (elementsByAttributeValue.size() > 0) {
            Element systemError = elementsByAttributeValue.first();
            return systemError.child(0).html();
        }

        throw new JSoupParserException();
    }
}
