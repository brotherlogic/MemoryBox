package com.brotherlogic.memory.core;

import java.io.File;

import org.json.JSONObject;

public class UntappdMemory extends Memory implements ImageMemory, JSONConstructable
{
   @Override
   public int buildFromJSON(JSONObject obj)
   {
      // TODO Auto-generated method stub
      return 0;
   }

   File imageFile = new File("");

   @Override
   public String getImagePath()
   {
      return imageFile.getAbsolutePath();
   }

   @Override
   public void setImagePath(String filePath)
   {
      imageFile = new File(filePath);
   }

   @Override
   public boolean equals(Object o)
   {
      if (!(o instanceof UntappdMemory))
         return false;
      UntappdMemory other = (UntappdMemory) o;

      if (!other.imageFile.equals(imageFile))
         return false;

      return super.equals(o);
   }

}
