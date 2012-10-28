package com.brotherlogic.memory.feeds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class Config
{
   private static Map<String, String> configMap = null;

   private static void buildConfig()
   {
      if (configMap == null)
      {
         configMap = new TreeMap<String, String>();
         File f = new File("etc/config.txt");
         try
         {
            BufferedReader reader = new BufferedReader(new FileReader(f));
            for (String line = reader.readLine(); line != null; line = reader.readLine())
            {
               String[] elems = line.trim().split("\\|");
               configMap.put(elems[0], elems[1]);
            }
            reader.close();
         }
         catch (IOException e)
         {
            e.printStackTrace();
         }
      }
   }

   public static String getParameter(String key)
   {
      buildConfig();
      return configMap.get(key);
   }
}
