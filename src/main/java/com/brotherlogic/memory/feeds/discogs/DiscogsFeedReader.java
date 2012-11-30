package com.brotherlogic.memory.feeds.discogs;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.brotherlogic.memory.core.DiscogsMemory;
import com.brotherlogic.memory.core.Memory;
import com.brotherlogic.memory.db.DBFactory;
import com.brotherlogic.memory.feeds.Config;
import com.brotherlogic.memory.feeds.JSONFeedReader;

/**
 * Discogs feed reader
 * 
 * @author simon
 * 
 */
public class DiscogsFeedReader extends JSONFeedReader
{
   /** THe access token generated in oauth */
   private Token accessToken;

   /** Logger used for logging */
   private final Logger logger = Logger.getLogger(this.getClass().getName());

   /** The OAuth service used to pull URLS */
   private OAuthService service;

   @Override
   protected Memory buildMemory(final JSONObject json) throws JSONException
   {
      try
      {
         DiscogsMemory mem = new DiscogsMemory();
         mem.setUniqueID(json.getString("id"));
         System.out.println(json);
         mem.setImagePath(DBFactory
               .buildInterface()
               .getDownloadQueue()
               .download(
                     new URL(convertImage(json.getJSONObject("basic_information")
                           .getString("thumb")))));

         String artistString = "";
         JSONArray artArr = json.getJSONObject("basic_information").getJSONArray("artists");
         for (int i = 0; i < artArr.length(); i++)
            artistString += artArr.getJSONObject(i).getString("name");
         mem.setArtist(artistString);

         mem.setTitle(json.getJSONObject("basic_information").getString("title"));
         mem.setReleaseYear(json.getJSONObject("basic_information").getInt("year"));
         mem.setUniqueID(json.getString("id"));

         return mem;
      }
      catch (MalformedURLException e)
      {
         throw new JSONException(e);
      }
   }

   /**
    * Converts a discogs image url into a full sized representation
    * 
    * @param url
    *           The url to convert
    * @return The name of the resolved url or null if a null string was passed
    *         in
    */
   private String convertImage(final String url)
   {
      logger.log(Level.INFO, "Converting " + url);
      String[] elems = url.split("-");
      return elems[0] + "-" + elems[2] + "-" + elems[3];
   }

   @Override
   protected String getClassName()
   {
      return DiscogsMemory.class.getName();
   }

   @Override
   protected URL getFeedURL(final long pagination) throws MalformedURLException
   {
      return new URL("http://api.discogs.com/users/brotherlogic/collection/folders/242017/releases");
   }

   @Override
   protected void login() throws IOException
   {
      logger.log(Level.INFO, "Discogs log in");

      service = new ServiceBuilder()
            .provider(DiscogsAPI.class)
            .apiKey(Config.getConfig("http://edip:8085/configstore/").getParameter("discogs.key"))
            .apiSecret(
                  Config.getConfig("http://edip:8085/configstore/").getParameter("discogs.secret"))
            .build();

      try
      {
         Object o = Config.getConfig("http://edip:8085/configstore/").retrieveObject(
               "discogs.accessToken");
         System.out.println(o);
         if (o == null)
         {
            Scanner in = new Scanner(System.in);
            Token requestToken = service.getRequestToken();
            Desktop.getDesktop().browse(new URI(service.getAuthorizationUrl(requestToken)));
            Verifier verifier = new Verifier(in.nextLine());
            accessToken = service.getAccessToken(requestToken, verifier);

            Config.getConfig("http://edip:8085/configstore/").storeObject("discogs.accessToken",
                  accessToken);
         }
         else
            accessToken = (Token) o;
      }
      catch (URISyntaxException e)
      {
         throw new IOException(e);
      }
   }

   @Override
   protected long processFeedText(final String text) throws JSONException
   {
      JSONObject obj = new JSONObject(text);
      JSONArray arr = obj.getJSONArray("releases");
      for (int i = 0; i < arr.length(); i++)
      {
         Memory mem = buildMemory(arr.getJSONObject(i));
         addObjectToRead(mem, arr.getJSONObject(i).toString());
      }

      return -1L;
   }

   @Override
   protected String read(final URL urlToRead)
   {
      System.out.println("Service = " + service);
      OAuthRequest request = new OAuthRequest(Verb.GET, urlToRead.toString());
      service.signRequest(accessToken, request);
      System.out.println("REQUEST = " + request.getSanitizedUrl());
      Response response = request.send();
      return response.getBody();
   }
}
