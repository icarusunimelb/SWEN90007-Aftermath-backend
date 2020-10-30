package security;

import org.json.JSONObject;
import org.springframework.web.filter.GenericFilterBean;
import utils.USERTYPE;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthenFilter extends GenericFilterBean {
    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        System.out.println("AuthenFilter!!!");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if(TokenVerification.getRoleFromRequest(httpRequest).equals(USERTYPE.UNKNOWN)){
            PrintWriter out = httpResponse.getWriter();
            JSONObject jsonObject = new JSONObject(String.format(
                    "{\"code\":\"%s\"}",HttpServletResponse.SC_UNAUTHORIZED));
            out.print(jsonObject);
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            System.out.println("--------------Authen fail------------");
            out.flush();
            return;
        }else {
            chain.doFilter(request, response);
        }
    }
}
