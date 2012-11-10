package com.brotherlogic.memory.feeds.discogs;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

/**
 * Defines the OAUTH Discogs API
 * 
 * @author simon
 * 
 */
public class DiscogsAPI extends DefaultApi10a
{
   /** THe URL used to authorize the oauth stuff */
   private static final String AUTHORIZE_URL = "http://www.discogs.com/oauth/authorize?oauth_token=%s";

   @Override
   public String getAccessTokenEndpoint()
   {
      return "http://api.discogs.com/oauth/access_token";
   }

   @Override
   public String getAuthorizationUrl(final Token arg0)
   {
      return String.format(AUTHORIZE_URL, arg0.getToken());
   }

   @Override
   public String getRequestTokenEndpoint()
   {
      return "http://api.discogs.com/oauth/request_token";
   }

}
