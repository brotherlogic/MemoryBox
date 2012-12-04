package com.brotherlogic.memory.feeds.discogs;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MusicbrainzArtistMatch
{
   private static MusicbrainzArtistMatch singleton = null;

   public static MusicbrainzArtistMatch getMatcher()
   {
      if (singleton == null)
         singleton = new MusicbrainzArtistMatch();
      return singleton;
   };

   long lastCall = 0;

   /**
    * Blocking constructor
    */
   private MusicbrainzArtistMatch()
   {

   }

   public String getSortName(String artistName) throws IOException
   {
      String urlStr = "http://musicbrainz.org/ws/2/artist?query=artist:"
            + artistName.replace(" ", "%20");
      URL url = new URL(urlStr);

      try
      {
         System.out.println("URL = " + url);

         // Wait one second between concurrent connections
         long waitTime = System.currentTimeMillis() - lastCall;
         if (waitTime < 1000)
            try
            {
               Thread.sleep(waitTime);
            }
            catch (InterruptedException e)
            {
               e.printStackTrace();
            }

         lastCall = System.currentTimeMillis();
         HttpURLConnection uc = (HttpURLConnection) url.openConnection();
         SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
         SortName sName = new SortName();
         parser.parse(uc.getInputStream(), sName);

         return sName.getSort();
      }
      catch (SAXException e)
      {
         throw new IOException(e);
      }
      catch (ParserConfigurationException e)
      {
         throw new IOException(e);
      }
   }
}

class SortName extends DefaultHandler
{
   String sort = "";
   String text = "";

   @Override
   public void characters(char[] ch, int start, int length) throws SAXException
   {
      text += new String(ch, start, length);
   }

   @Override
   public void endElement(String uri, String localName, String qName) throws SAXException
   {
      if (sort.length() == 0 && (localName + qName).equals("sort-name"))
         sort = text;
      else
         text = "";
   }

   public String getSort()
   {
      return sort;
   }
}