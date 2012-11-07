package com.brotherlogic.memory.core;


public class GitMemory extends Memory
{
   private String branch;

   private String project;

   public String getBranch()
   {
      return branch;
   }

   public String getProject()
   {
      return project;
   }

   public void setBranch(String branch)
   {
      this.branch = branch;
   }

   public void setProject(String project)
   {
      this.project = project;
   }
}
