import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class WatsonParser {

    public static void main(String args[]) {
        try
        {
            Parser parser = new Parser (args[0]);
            NodeList list = parser.parse(new TagNameFilter("P"));
            System.out.println(list.toHtml());
        }
        catch (ParserException pe)
        {
            pe.printStackTrace();
        }
    }
}