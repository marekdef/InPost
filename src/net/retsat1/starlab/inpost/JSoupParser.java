package net.retsat1.starlab.inpost;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JSoupParser {
	public String parse(String inpostPage) {
		Document parse = Jsoup.parse(inpostPage, "utf-8");
		
		Elements elementsByAttributeValue = parse.getElementsByAttributeValue("class", "sledz-result contenttable");
		
		Element first = elementsByAttributeValue.first();
		
		first.removeAttr("width");
		return first.outerHtml();
	}
}
