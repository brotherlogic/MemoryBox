package com.brotherlogic.memory.feeds.flickr;

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
import org.scribe.builder.api.FlickrApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.brotherlogic.memory.Constants;
import com.brotherlogic.memory.core.Memory;
import com.brotherlogic.memory.db.DBFactory;
import com.brotherlogic.memory.feeds.Config;
import com.brotherlogic.memory.feeds.FeedReader;
import com.brotherlogic.memory.feeds.JSONFeedReader;

public class FlickrFeedReader extends JSONFeedReader
{
   Logger logger = Logger.getLogger(getClass().getName());

   /** The OAuth service used to pull URLS */
   private OAuthService service;

   public static void main(String[] args)
   {
      FeedReader reader = new FlickrFeedReader();
   }

   @Override
   protected Memory buildMemory(JSONObject json) throws JSONException
   {
      logger.log(Level.INFO, "Building Memory");

      try
      {
         FlickrMemory mem = new FlickrMemory();
         mem.setUniqueID(json.getString("id"));
         mem.setImagePath(DBFactory.buildInterface().getDownloadQueue()
               .download(new URL(json.getString("url_o"))));
         mem.setCaption(json.getString("title"));
         logger.log(Level.INFO, "Memory Built");
         return mem;
      }
      catch (MalformedURLException e)
      {
         throw new JSONException(e);
      }

   }

   @Override
   protected String getClassName()
   {
      return FlickrMemory.class.getName();
   }

   @Override
   protected URL getFeedURL(String pagination) throws MalformedURLException
   {
      String url = "http://api.flickr.com/services/rest/?method=flickr.photos.search&user_id=me&per_page=500&extras=url_o&format=json";
      if (pagination.equals(""))
      {
         return new URL(url);
      }
      else
         return new URL(url + "&page=" + pagination);

   }

   @Override
   protected String processFeedText(String text) throws JSONException
   {
      System.out.println("TEXT=" + text);
      JSONObject baseObj = new JSONObject(text.substring("jsonFlickrApi(".length(),
            text.length() - 1));
      JSONObject nextObj = baseObj.getJSONObject("photos");

      String max_id = null;
      if (nextObj.getInt("page") < nextObj.getInt("pages"))
         max_id = nextObj.getInt("page") + 1 + "";

      JSONArray arr = nextObj.getJSONArray("photo");
      for (int i = 0; i < arr.length(); i++)
      {
         Memory mem = buildMemory(arr.getJSONObject(i));
         addObjectToRead(mem, arr.getJSONObject(i).toString());
      }

      return max_id;
   }

   @Override
   protected void login() throws IOException
   {
      logger.log(Level.INFO, "Flickr log in");

      service = new ServiceBuilder().provider(FlickrApi.class)
            .apiKey(Config.getConfig(Constants.CONFIG_SERVER).getParameter("flickr.key"))
            .apiSecret(Config.getConfig(Constants.CONFIG_SERVER).getParameter("flickr.secret"))
            .callback("http://www.dcs.shef.ac.uk/~sat").build();

      try
      {
         Object o = Config.getConfig(Constants.CONFIG_SERVER).retrieveObject("flickr.accessToken");
         if (o == null)
         {
            Scanner in = new Scanner(System.in);

            Token requestToken = service.getRequestToken();
            String authorizationUrl = service.getAuthorizationUrl(requestToken);
            Desktop.getDesktop().browse(new URI(authorizationUrl + "&perms=read"));
            Verifier verifier = new Verifier(in.nextLine());
            accessToken = service.getAccessToken(requestToken, verifier);

            Config.getConfig(Constants.CONFIG_SERVER)
                  .storeObject("flickr.accessToken", accessToken);
         }
         else
            accessToken = (Token) o;
      }
      catch (URISyntaxException e)
      {
         throw new IOException(e);
      }
   }

   /** THe access token generated in oauth */
   private Token accessToken;

   @Override
   protected String read(final URL urlToRead) throws IOException
   {
      if (service == null)
         login();

      System.out.println("Service = " + service + " and " + urlToRead);
      OAuthRequest request = new OAuthRequest(Verb.GET, urlToRead.toString());
      service.signRequest(accessToken, request);
      System.out.println("REQUEST = " + request.getUrl() + " from " + urlToRead);
      Response response = request.send();
      return response.getBody();
   }

}
