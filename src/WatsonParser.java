import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class WatsonParser {

    public static void main(String args[]) {
        try
        {
            Parser parser = new Parser ("http://docs.oracle.com/javase/specs/jls/se7/html/jls-1.html");
            NodeList list = parser.parse(new TagNameFilter("P"));
            System.out.println(list.toHtml());
        }
        catch (ParserException pe)
        {
            pe.printStackTrace();
        }
    }
}