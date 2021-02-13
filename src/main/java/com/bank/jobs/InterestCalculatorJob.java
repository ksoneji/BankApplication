package com.bank.jobs;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bank.components.SpringExtension;
import com.bank.model.dao.Account;
import com.bank.service.AccountService;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.routing.RoundRobinPool;

/**
 * <p>
 * Background job to generate simple interest for accounts on a daily basis.
 * </p>
 * 
 * @author Ketan Soneji
 */
@Component("InterestCalculatorJob")
@PropertySource("classpath:application.properties")
public class InterestCalculatorJob {
	private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

	@Value("${interest.calculator.job.threads}")
	private Integer jobThreads;
	
	@Autowired
	protected ActorSystem actorSystem;

	@Autowired
	AccountService accService;

	ActorRef actorRef;

	@Bean("InterestCalculatorActorRef")
	public void actorRef() {
		logger.debug("Trying to fetch actorRef");
		this.actorRef = actorRef("InterestCalculatorActor", jobThreads.intValue());
	}

	/**
	 * <p>
	 * Returns the ActorRef with the pool of actors
	 * </p>
	 * 
	 * @param actorClass      The actor class serving the request
	 * @param numberOfWorkers The number of working actor instances to serve the
	 *                        request
	 * @return The ActorRef object
	 */
	public ActorRef actorRef(String actorClass, int numberOfWorkers) {
		ActorRef actorRef = this.actorSystem.actorOf(SpringExtension.SPRING_EXTENSION_PROVIDER.get(actorSystem)
				.props(actorClass).withRouter(new RoundRobinPool(numberOfWorkers)), actorClass);

		logger.debug("Created actorRef for actorClass:: {} with id::{}", actorClass, actorRef.hashCode());
		return actorRef;
	}

	@Scheduled(cron = "${interest.calculator.job.frequency}")
	public void cronJobSch() {
		List<Account> activeAccounts = accService.getAccountsForInterestCalculation();
		logger.debug("Executing background job to calculate interest");
		
		/**
		 * Instead of spawning one actor per account we can actually send a batch of accounts to each actor.
		 * The below logic is for simplicity and demo purpose only.
		 */
		// Start generating interest for each account
		activeAccounts.forEach(account -> {
			actorRef.tell(account, ActorRef.noSender());
		});
	}

}
