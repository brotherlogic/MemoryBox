package com.brotherlogic.memory.feeds.discogs;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

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

public class DiscogsFeedReader extends JSONFeedReader
{
   public static void main(String[] args) throws Exception
   {
      DiscogsFeedReader dfr = new DiscogsFeedReader();
      dfr.login();
      dfr.update();
   }

   Token accessToken;

   OAuthService service;

   @Override
   protected Memory buildMemory(JSONObject json) throws JSONException
   {
      DiscogsMemory mem = new DiscogsMemory();
      String artistString = "";
      JSONArray artArr = json.getJSONObject("basic_information").getJSONArray("artists");
      for (int i = 0; i < artArr.length(); i++)
         artistString += artArr.getJSONObject(i).getString("name");
      mem.setArtist(artistString);
      return mem;
   }

   @Override
   protected String getClassName()
   {
      return DiscogsMemory.class.getName();
   }

   @Override
   protected URL getFeedURL(long pagination) throws MalformedURLException
   {
      return new URL("http://api.discogs.com/users/brotherlogic/collection/folders/242017/releases");
   }

   @Override
   protected void login() throws IOException
   {
      service = new ServiceBuilder().provider(DiscogsAPI.class)
            .apiKey(Config.getParameter("discogs.key"))
            .apiSecret(Config.getParameter("discogs.secret")).build();

      try
      {
         Object o = DBFactory.buildInterface().getDownloadQueue()
               .retrieveObject("discogs.accessToken");
         if (o == null)
         {
            Scanner in = new Scanner(System.in);
            Token requestToken = service.getRequestToken();
            Desktop.getDesktop().browse(new URI(service.getAuthorizationUrl(requestToken)));
            Verifier verifier = new Verifier(in.nextLine());
            accessToken = service.getAccessToken(requestToken, verifier);

            DBFactory.buildInterface().getDownloadQueue()
                  .saveObject(accessToken, "discogs.accessToken");
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
   protected long processFeedText(String text) throws JSONException
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
   protected String read(URL urlToRead)
   {
      OAuthRequest request = new OAuthRequest(Verb.GET, urlToRead.toString());
      service.signRequest(accessToken, request);
      System.out.println("REQUEST = " + request.getSanitizedUrl());
      Response response = request.send();
      return response.getBody();
   }
}
