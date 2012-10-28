package com.brotherlogic.memory;

import java.io.IOException;

import com.brotherlogic.memory.feeds.UntappdFeedReader;

public class CommandLineUpdate
{
   public static void main(String[] args) throws IOException
   {
      CommandLineUpdate clu = new CommandLineUpdate();
      clu.run();
   }

   public void run() throws IOException
   {
      UntappdFeedReader reader = new UntappdFeedReader("brotherlogic");
      reader.update();
   }
}
