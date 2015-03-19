package net.retsat1.starlab.inpost;

import net.retsat1.starlab.inpost.exceptions.JSoupParserException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JSoupParser {
	public String parse(String inpostPage) throws JSoupParserException {
		Document parse = Jsoup.parse(inpostPage, "utf-8");
		
		Elements elementsByAttributeValue = parse.getElementsByAttributeValue("class", "sledz-result contenttable");
		
		if(elementsByAttributeValue.size() > 0) {
			Element first = elementsByAttributeValue.first();
			
			return first.removeAttr("width").outerHtml();
		}
		
		elementsByAttributeValue = parse.getElementsByClass("sledzenie-error");
		
		if (elementsByAttributeValue.size() > 0) {
			Element first = elementsByAttributeValue.first();
			return first.html();
		}
		
		throw new JSoupParserException("Nie mogę odczytać odpowiedzi");
	}
}
