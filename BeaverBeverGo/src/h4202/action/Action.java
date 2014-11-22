package h4202.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Administrateur
 */
public abstract class Action {
    public abstract void execute(HttpServletRequest request,HttpSession session);
}