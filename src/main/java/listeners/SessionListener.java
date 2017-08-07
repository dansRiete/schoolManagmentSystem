package listeners;

import org.apache.log4j.Logger;
import servlets.LoginServlet;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by Aleks on 07.08.2017.
 */
public class SessionListener implements HttpSessionListener {
    Logger logger = Logger.getLogger(LoginServlet.class);

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {

    }
    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        logger.info("Session is invalidated");
        logger.info("User " + httpSessionEvent.getSession().getAttribute("username") + " is logged out");
    }
}
