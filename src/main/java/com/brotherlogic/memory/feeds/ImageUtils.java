package com.brotherlogic.memory.feeds;

import java.net.URL;

import com.brotherlogic.memory.db.DBFactory;

public class ImageUtils
{
   public String storeImage(URL url)
   {
      return DBFactory.buildInterface().getDownloadQueue().download(url);
   }
}
