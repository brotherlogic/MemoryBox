package com.brotherlogic.memory;

import com.brotherlogic.memory.feeds.UntappdFeedReader;

public class CommandLineUpdate
{
   public static void main(String[] args)
   {
      CommandLineUpdate clu = new CommandLineUpdate();
      clu.run();
   }

   public void run()
   {
      UntappdFeedReader reader = new UntappdFeedReader("brotherlogic");
      // reader.updateFeed();
   }
}
