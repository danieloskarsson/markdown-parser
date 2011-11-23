package com.danieloskarsson.markdown.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;

import junit.framework.Assert;

public class Test {

	@org.junit.Test
	public void parseTwoTables() {
		String input = "" +
		"This is a simple table:\n" +
		"h1| h2\n" +
		"--- |------\n" +
		"v1|v2\n" +
		"v3| v4\n" +
		"v5 | v6\n" +
		"blah\n" +
		"To catch a table of the following format:\n" +
		"\n" +
		"First Header  | Second Header\n" +
		"------------- | ------------\n" +
		"Row1 Cell1    | Row1 Cell2\n" +
		"Row2 Cell1    | Row2 Cell2\n" +
		"\n" +
		"Use a regular expression:\n";

		MarkdownTableParser parser = new MarkdownTableParser();
		SortedMap<String, String> sortedMap = parser.tableAsHtml(input);		
		Assert.assertEquals(2, sortedMap.size());
	}
	
	@org.junit.Test
	public void parse() {
		String input = "" +
		"First Header  | Second Header\n" +
		"------------- | ------------\n" +
		"Row1 Cell1    | Row1 Cell2\n" +
		"Row2 Cell1    | Row2 Cell2\n";

		MarkdownTableParser parser = new MarkdownTableParser();
		SortedMap<String, String> sortedMap = parser.tableAsHtml(input);
		String value = "";
		String key = "";
		if (sortedMap.size() > 0) {
			key = sortedMap.firstKey();
			value = sortedMap.get(key);
		}
		
		String expected = "" +
		"<table><thead>\n" +
		"<tr>\n" +
		"<th>First Header</th>\n" +
		"<th>Second Header</th>\n" +
		"</tr>\n" +
		"</thead><tbody>\n" +
		"<tr>\n" +
		"<td>Row1 Cell1</td>\n" +
		"<td>Row1 Cell2</td>\n" +
		"</tr>\n" +
		"<tr>\n" +
		"<td>Row2 Cell1</td>\n" +
		"<td>Row2 Cell2</td>\n" +
		"</tr>\n" +
		"</tbody></table>\n";		
		
		Assert.assertEquals(expected, value);
		Assert.assertEquals(input, key);
	}
	
	@org.junit.Test
	public void numberOfOccurrences() {
		int charCount = "---|--- |--- |--- |--- |--- |--- |--- |--- |--- ".replaceAll("[^|]", "").length();
		Assert.assertEquals(9, charCount);
	}
	
	@org.junit.Test
	public void addOne() {
		String[] orgArray = {"1", "2"};
        List<String> list = Arrays.asList(orgArray);
        ArrayList<String> arrayList = new ArrayList<String>(list);
        arrayList.add("\n");
        String[] array = arrayList.toArray(new String[arrayList.size()]);
        Assert.assertEquals(orgArray.length+1, array.length);
	}
	
	public void replace() {
	}
}
