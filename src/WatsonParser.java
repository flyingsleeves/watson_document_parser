import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WatsonParser {

    public static void main(String args[]) {
        try {
            Parser parser = new Parser ("http://docs.oracle.com/javase/specs/jls/se7/html/jls-1.html");
            NodeList list = new NodeList();

            TagNameFilter titleTNF = new TagNameFilter("title");
            TagNameFilter h1TNF = new TagNameFilter("h1");
            TagNameFilter h2TNF = new TagNameFilter("h2");
            TagNameFilter h3TNF = new TagNameFilter("h3");
            TagNameFilter pTNF = new TagNameFilter("p");
            TagNameFilter tableTNF = new TagNameFilter("table");

            NodeFilter[] tags = {titleTNF, h1TNF, h2TNF, h3TNF, pTNF};
            OrFilter tagFilter = new OrFilter(tags);


            for (org.htmlparser.util.NodeIterator e = parser.elements(); e.hasMoreNodes(); ) {
                e.nextNode().collectInto(list, tagFilter);
            }

            System.out.println(list.toHtml());

            PrintWriter writer;
            try {
                writer = new PrintWriter("parsed.html", "UTF-8");
                writer.println(list.toHtml());
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } catch (ParserException pe) {
            pe.printStackTrace();
        }

        postProcess();
    }

    private static void postProcess() {
        Path path = Paths.get("parsed.html");
        Charset charset = StandardCharsets.UTF_8;



        String content = "empty";

        try {
            content = new String(Files.readAllBytes(path), charset);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Remove formatting tags and other irrelevant info
        content = content.replaceAll("<span[^>]*>|</span>", "");
        content = content.replaceAll("<p></p>", "");
        content = content.replaceAll("\\[<a[^>]*>edit</a>]", "");
        content = content.replaceAll("<font[^>]*>", "");
        content = content.replaceAll("</font>", "");
        content = content.replaceAll("<code .*>|</code>", "");


        // Add tabs and newlines to make it human readable
        content = content.replaceAll("</p>", "</p>\n");
        content = content.replaceAll("</title>", "</title>\n");
        content = content.replaceAll("</h1>", "</h1>\n");
        content = content.replaceAll("</h2>", "</h2>\n");
        content = content.replaceAll("</h3>", "</h3>\n");
        content = content.replaceAll("<h1>", "\t<h1>");
        content = content.replaceAll("<h2>", "\t\t<h2>");
        content = content.replaceAll("<h3>", "\t\t\t<h3>");
        content = content.replaceAll("<p>", "\t\t\t\t<p>");


        try {
            Files.write(path, content.getBytes(charset));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
