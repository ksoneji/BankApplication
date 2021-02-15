package com.bank.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import akka.actor.ActorSystem;
/**
 * <p>The configuration class to setup the Akka ActorSystem</p>
 * 
 * @author Ketan.Soneji
 */
@Configuration
@ComponentScan
public class ActorSystemConfiguration
{

   @Autowired
   private ApplicationContext applicationContext;

   private ActorSystem actorSystem;

   @Bean
   public ActorSystem actorSystem()
   {
      this.actorSystem = ActorSystem.create("akka-kms-actor-system");
      SpringExtension.SPRING_EXTENSION_PROVIDER.get(this.actorSystem)
            .initialize(applicationContext);
      return this.actorSystem;
   }

}