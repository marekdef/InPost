package net.retsat1.starlab.inpost;

import net.retsat1.starlab.inpost.exceptions.JSoupParserException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JSoupParser {

    public String parse(String inpostPage) throws JSoupParserException {
        Document parse = Jsoup.parse(inpostPage, "utf-8");

        Elements elementsByAttributeValue = parse.getElementsByAttributeValue("class", "current-status important");

        if (elementsByAttributeValue.size() > 0) {
            Element currentStatusImportant = elementsByAttributeValue.first();

            Elements h3s = currentStatusImportant.getElementsByTag("h3");

            if(h3s.size() > 0) {
                return h3s.first().html();
            }
        }

        elementsByAttributeValue = parse.getElementsByClass("system-error");

        if (elementsByAttributeValue.size() > 0) {
            Element systemError = elementsByAttributeValue.first();
            return systemError.child(0).    html();
        }

        throw new JSoupParserException("Nie mogę odczytać odpowiedzi");
    }
}
