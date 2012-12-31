package com.brotherlogic.memory.feeds.instagram;

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

import com.brotherlogic.memory.Constants;
import com.brotherlogic.memory.core.Memory;
import com.brotherlogic.memory.db.DBFactory;
import com.brotherlogic.memory.feeds.Config;
import com.brotherlogic.memory.feeds.FeedReader;
import com.brotherlogic.memory.feeds.JSONFeedReader;

public class InstagramFeedReader extends JSONFeedReader
{
   Logger logger = Logger.getLogger(getClass().getName());

   /** The OAuth service used to pull URLS */
   private OAuthService service;

   public static void main(String[] args)
   {
      FeedReader reader = new InstagramFeedReader();
   }

   @Override
   protected Memory buildMemory(JSONObject json) throws JSONException
   {
      logger.log(Level.INFO, "Building Memory");

      try
      {
         InstagramMemory mem = new InstagramMemory();
         mem.setUniqueID(json.getString("id"));
         mem.setImagePath(DBFactory
               .buildInterface()
               .getDownloadQueue()
               .download(
                     new URL(json.getJSONObject("images").getJSONObject("standard_resolution")
                           .getString("url"))));
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
      return InstagramMemory.class.getName();
   }

   @Override
   protected URL getFeedURL(String pagination) throws MalformedURLException
   {
      String url = "https://api.instagram.com/v1/users/689612/media/recent";
      if (pagination.equals(""))
      {
         return new URL(url);
      }
      else
         return new URL(url + "?max_id=" + pagination);

   }

   @Override
   protected String processFeedText(String text) throws JSONException
   {
      System.out.println("PROC = " + text);

      JSONObject baseObj = new JSONObject(text);
      JSONObject nextObj = baseObj.getJSONObject("pagination");

      String max_id = null;
      if (nextObj.has("next_max_id"))
         max_id = nextObj.getString("next_max_id");

      JSONArray arr = baseObj.getJSONArray("data");
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
      logger.log(Level.INFO, "Instagram log in");

      service = new ServiceBuilder().provider(InstagramAPI.class)
            .apiKey(Config.getConfig(Constants.CONFIG_SERVER).getParameter("instagram.id"))
            .apiSecret(Config.getConfig(Constants.CONFIG_SERVER).getParameter("instagram.secret"))
            .callback("http://www.dcs.shef.ac.uk/~sat").build();

      try
      {
         Object o = Config.getConfig(Constants.CONFIG_SERVER).retrieveObject(
               "instagram.accessToken");
         if (o == null)
         {
            Scanner in = new Scanner(System.in);

            Desktop.getDesktop().browse(new URI(service.getAuthorizationUrl(null)));
            Verifier verifier = new Verifier(in.nextLine());
            accessToken = service.getAccessToken(null, verifier);

            Config.getConfig(Constants.CONFIG_SERVER).storeObject("instagram.accessToken",
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
