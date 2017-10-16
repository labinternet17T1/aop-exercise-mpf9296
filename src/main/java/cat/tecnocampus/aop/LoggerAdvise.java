package cat.tecnocampus.aop;

import cat.tecnocampus.domain.Classroom;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maria on 16/10/2017.
 */

@Aspect
@Component
public class LoggerAdvise {

    private static final Logger logger = LoggerFactory.getLogger(LoggerAdvise.class);

    //A pointcut that matches one single method
    @Pointcut("execution(* cat.tecnocampus.persistence.ClassroomDAO.findAll())") ///????????
    public void pointcutListUsers() {}

    @Before("pointcutListUsers()")
    public void beforeListUsers() {
        logger.info("Going to list all users");
    }

    @After("pointcutListUsers()")
    public void afterListUsers() {
        logger.info("Already listed all users");
    }

    //A pointcut that matches all methods having the word "Notes" in any position of methods' name
    @Pointcut("execution(* cat.tecnocampus.useCases.UserUseCases.*Notes*(..))")
    public void pointcutNotes() {}

    @Before("pointcutNotes()")
    public void beforeDealingNotes() {
        logger.info("Going to deal with notes");
    }

    //Around pointcut. Note that this method must return what the proxied method is supposed to return
    @Around("pointcutNotes()")
    public List<Classroom> dealRequestParam(ProceedingJoinPoint jp) {

        try {
            logger.info("Before showing notes");
            //note that showUserRequestParameter is proxied and it must return a string
            // representing the thymeleaf file name
            List<Classroom> res = (List<Classroom>) jp.proceed();
            logger.info("After showing user notes");
            return res;
        } catch (Throwable throwable) {
            logger.info("Showing notes: Something went wrong");
            throwable.printStackTrace();
            return new ArrayList<Classroom>();
        }
    }

    //Getting the parameters of the proxied method
    @Pointcut("execution(* cat.tecnocampus.useCases.UserUseCases.getUser(..)) && args(userName)")
    public void showUserPointcut(String userName) {}

    @Before("showUserPointcut(userName)")
    public void showUserAdvice(String userName) {
        logger.info("Going to show user: " + userName);
    }
}
