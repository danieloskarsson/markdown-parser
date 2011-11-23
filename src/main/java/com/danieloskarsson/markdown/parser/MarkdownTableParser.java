
/* 
   Copyright 2011 Daniel Oskarsson

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.danieloskarsson.markdown.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 
 * @author Daniel Oskarsson (daniel.oskarsson@gmail.com)
 *
 * Parser for tables in markdown strings. Complement to MarkdownJ Core, which doesn't seem to support tables.
 * 
 * Usage:
 * 
 * String html = new MarkdownTableParser().markdownTables(markdown); // Markdown Table Parser
 * html = new MarkdownProcessor().markdown(html); // MarkdownJ
 *
 * If executed in the other order (i.e. MarkdownJ first) the table will be surrounded with <p> </p> tags.
 */
public class MarkdownTableParser {

    /**
     * Replaces occurences of markdown tables with html representations.
     * @param markdown
     * @return
     */
    public String markdownTables(String markdown) {
        SortedMap<String, String> sortedMap = tableAsHtml(markdown);
        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            markdown = markdown.replaceAll(key, value);
        }
        return markdown;
    }

    /**
     * Parses a string and returns a map with markdown representations of a table
     * as the key and a html representation of the same table as the value.
     * @param text
     * @return
     */
    SortedMap<String, String> tableAsHtml(String text) {
            SortedMap<String, String> sortedMap = new TreeMap<String, String>();
            List<String> markdownTable = new ArrayList<String>();
            String lastLine = null;
            String magicLine = null;
            boolean parseTable = false;
            int numberOfColumns = 0;

            String[] array = text.split("\\n");
            array = addEmptyLine(array);
            for (String line : array) {
                    if (parseTable) {
                            if (line.replaceAll("[^|]", "").length() != numberOfColumns - 1) {
                                    parseTable = false;
                                    List<String> htmlTable = this.asHtml(lastLine, markdownTable, numberOfColumns);
                                    markdownTable.add(0, magicLine);
                                    markdownTable.add(0, lastLine);
                                    String markdown = this.mergeList(markdownTable);
                                    String html = this.mergeList(htmlTable);
                                    sortedMap.put(markdown, html);
                            } else {
                                    markdownTable.add(line);
                            }
                    } else {			
                            if (line.matches("^-{3,}\\s*\\|\\s*-{3,}(\\s*\\|\\s*-{3,}\\s*)*$")) {
                                    numberOfColumns = line.split("\\s*\\|\\s*").length;
                                    if (lastLine.replaceAll("[^|]", "").length() == numberOfColumns - 1) {
                                            parseTable = true;
                                            markdownTable.clear();
                                            magicLine = line;
                                    } else {
                                            lastLine = line;
                                    }
                            } else {
                                    lastLine = line;
                            }
                    }
            }

            return sortedMap;
    }

    /**
     * Converts a markdown representation of a table to a html representation of a table
     * @param markdown
     * @return
     */
    List<String> asHtml(String header, List<String> markdown, int numberOfColumns) {
            List<String> html = new ArrayList<String>();
            html.add("<table><thead>");
            html.add("<tr>");
            String[] headers = header.split("\\s*\\|\\s*");
            for (String value : headers) {
                    html.add("<th>" + value + "</th>");
            }
            html.add("</tr>");
            html.add("</thead><tbody>");

            for (String line : markdown) {
                    html.add("<tr>");
                    String[] values = line.split("\\s*\\|\\s*");
                    for (String value : values) {
                            html.add("<td>" + value + "</td>");
                    }
                    html.add("</tr>");			
            }
            html.add("</tbody></table>");
            return html;
    }

    /**
     * Returns a String consisting of each line in a list + the new line character (\n).
     * @param list
     * @return
     */
    String mergeList(List<String> list) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String string : list) {
                    stringBuilder.append(string);
                    stringBuilder.append("\n");
            }
            return stringBuilder.toString();
    }

    String[] addEmptyLine(String[] array) {
        List<String> list = Arrays.asList(array);
        ArrayList<String> arrayList = new ArrayList<String>(list);
        arrayList.add("\n");
        array = arrayList.toArray(new String[arrayList.size()]);
        return array;
    }
}
