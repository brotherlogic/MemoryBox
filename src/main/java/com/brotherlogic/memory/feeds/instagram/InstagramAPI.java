package com.brotherlogic.memory.feeds.instagram;

import java.util.logging.Logger;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuth20ServiceImpl;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.OAuthEncoder;

/**
 * Defines the OAUTH Instagram API
 * 
 * @author simon
 * 
 */
public class InstagramAPI extends DefaultApi20
{
   Logger logger = Logger.getLogger(getClass().getName());

   private static final String URL = "https://api.instagram.com/oauth/authorize/?client_id=%s&redirect_uri=%s&response_type=code";

   @Override
   public Verb getAccessTokenVerb()
   {
      return Verb.POST;
   }

   @Override
   public String getAccessTokenEndpoint()
   {
      return "https://api.instagram.com/oauth/access_token";
   }

   @Override
   public String getAuthorizationUrl(OAuthConfig config)
   {
      return String.format(URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
   }

   @Override
   public AccessTokenExtractor getAccessTokenExtractor()
   {
      return new JsonTokenExtractor();
   }

   /**
    * Not sure if it's a Scribe bug or an Instagram oddity. We have to send the
    * various parameters in the POST body (Scribe sends them as query string)
    * and we must include the 'grant_type'.
    */
   @Override
   public OAuthService createService(final OAuthConfig config)
   {
      return new OAuth20ServiceImpl(this, config)
      {
         @Override
         public Token getAccessToken(Token requestToken, Verifier verifier)
         {
            OAuthRequest request = new OAuthRequest(getAccessTokenVerb(), getAccessTokenEndpoint());

            request.addBodyParameter("grant_type", "authorization_code");
            request.addBodyParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
            request.addBodyParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
            request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
            request.addBodyParameter(OAuthConstants.REDIRECT_URI, config.getCallback());

            if (config.hasScope())
               request.addBodyParameter(OAuthConstants.SCOPE, config.getScope());

            Response response = request.send();
            return getAccessTokenExtractor().extract(response.getBody());
         }
      };
   }
}