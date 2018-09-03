package pw.io.booker.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import pw.io.booker.exception.BookingException;
import pw.io.booker.repo.AuthenticationRepository;

@Aspect
@Component
public class AuthenticationAspect {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private AuthenticationRepository authenticationRepository;

	public AuthenticationAspect(AuthenticationRepository authenticationRepository) {
		super();
		this.authenticationRepository = authenticationRepository;
	}

	@Before("execution(* pw.io.booker.controller..*(..))")
	public void beforeController(JoinPoint joinPoint) {

		System.out.println("This happened before!");
		logger.info("This happened before!");

	}

	@After("execution(* pw.io.booker.controller..*(..))")
	public void afterController(JoinPoint joinPoint) {

		System.out.println("This happened after!");
		logger.info("This happened after!");

	}

	@Around("execution(* pw.io.booker.controller..*(..)) && args(tokenID,..) && !execution(* pw.io.booker.controller.CustomerController.saveAll())")
	public Object checkAuthentication(ProceedingJoinPoint joinPoint, String tokenID) throws Throwable {

		if (tokenID == null) {

			logger.error("Please input a token ID!");
			throw new BookingException("Please input a token ID!");

		}

		if (authenticationRepository.findByToken(tokenID) == null) {

			logger.error("Please input a token ID!");
			throw new BookingException("Invalid credentials!");

		}

		return joinPoint.proceed();

	}

}
