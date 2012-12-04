package com.brotherlogic.memory.discogs;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.brotherlogic.memory.feeds.discogs.MusicbrainzArtistMatch;

public class ArtistMatchTest
{
   @Test
   public void testArtistMatch() throws IOException
   {
      String artist = "The Beatles";
      String hopefully = "Beatles, The";

      MusicbrainzArtistMatch match = MusicbrainzArtistMatch.getMatcher();
      String actually = match.getSortName(artist);

      Assert.assertEquals("Artist names don't match", hopefully, actually);
   }
}
