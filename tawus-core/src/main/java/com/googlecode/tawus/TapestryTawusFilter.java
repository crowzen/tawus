package com.googlecode.tawus;

import java.util.Set;
import java.util.HashSet;
import javax.persistence.Entity;
import javax.servlet.ServletContext;

import org.apache.tapestry5.TapestryFilter;
import org.apache.tapestry5.internal.TapestryInternalUtils;
import org.apache.tapestry5.ioc.def.ModuleDef;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import com.googlecode.tawus.internal.AbstractEntityLocator;
import com.googlecode.tawus.internal.def.EntityModuleDef;
import com.googlecode.tawus.services.EntityLocator;

public class TapestryTawusFilter extends TapestryFilter
{

   private static final String MODEL_PACKAGES = "tawus-model-packages";

   @Override
   protected ModuleDef[] provideExtraModuleDefs(ServletContext context)
   {
      String packages = context.getInitParameter(MODEL_PACKAGES);
      if(packages == null)
      {
         return new ModuleDef[] {};
      }

      Set<String> packageSet = new HashSet<String>();
      for(String packageName: TapestryInternalUtils.splitAtCommas(packages)){
	      packageSet.add(packageName);
      }

      EntityLocator entityLocator = new AbstractEntityLocator(packageSet)
      {
         @Override
         @SuppressWarnings("unchecked")
         public boolean isEntity(@SuppressWarnings("rawtypes") Class entityType)
         {
            return entityType.getAnnotation(Entity.class) != null;
         }
      };

      return new ModuleDef[] { new EntityModuleDef(entityLocator) };
   }
}
