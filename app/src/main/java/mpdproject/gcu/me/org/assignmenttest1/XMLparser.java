package mpdproject.gcu.me.org.assignmenttest1;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.Debug;
import android.util.Log;

/**
 * ZICONG LI
 * S1635332
 *
 * This class parses the xml data
 * It uses the android XmlPullParser class
 */

public class XMLparser {
	
    LinkedList<Item> aList;
    Item anItem;

    public LinkedList<Item> getItemList(){
        return aList;
    }

    public void parseXML(String dataToParse ){

        aList = new LinkedList<Item>();

        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput( new StringReader ( dataToParse ) );
            int eventType = xpp.getEventType();
            int titleCount = 0;
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                    // Found a start tag
                    if(eventType == XmlPullParser.START_TAG)
                    {
                            // Check which Tag we have
                            if (xpp.getName().equalsIgnoreCase("title") && titleCount != 2)
                            {
                                anItem = new Item();
                                // Now just get the associated text
                                String temp = xpp.nextText();

                                anItem.setTitle(temp);


                            }
                            else
                                // Check which Tag we have
                                if (xpp.getName().equalsIgnoreCase("description") && titleCount != 3)
                                {
                                    // Now just get the associated text
                                    String temp = xpp.nextText();
                                    anItem.setDescription(temp);


                                }
                                else
                                    // Check which Tag we have
                                    if (xpp.getName().equalsIgnoreCase("link") && titleCount != 4)
                                    {
                                        // Now just get the associated text
                                        String temp = xpp.nextText();
                                        anItem.setLink(temp);

                                    }
                                else
                                    // Check which Tag we have
                                    if (xpp.getName().equalsIgnoreCase("point") && titleCount != 5)
                                    {
                                        // Now just get the associated text
                                        String temp = xpp.nextText();
                                        anItem.setGeorssPoint(temp);
                                        Log.e("Geo Point", temp);

                                    }
                                else
                                    // Check which Tag we have
                                    if (xpp.getName().equalsIgnoreCase("pubDate") && titleCount != 6)
                                    {
                                        // Now just get the associated text
                                        String temp = xpp.nextText();
                                        anItem.setPubDate(temp);
                                        aList.add(anItem);

                                    }
                        titleCount++;
                    }

                // Get the next event
                eventType = xpp.next();

            }

        }
            catch (XmlPullParserException ae1)
            {
                Log.e("XMLParser","Parsing error" + ae1.toString());
            }
            catch (IOException ae1)
            {
                Log.e("XMLparser","IO error");
            }

        Log.e("XMLparser","End of document");
    }
}