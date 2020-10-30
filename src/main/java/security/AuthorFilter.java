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

public class AuthorFilter extends GenericFilterBean {
    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        USERTYPE role = TokenVerification.getRoleFromRequest(httpRequest);
        String accessList = AccessControlMap.ACCESS_CONTROL.get(role);
        Boolean hasAccess = false;
        for (String access: accessList.split(",")) {
            if (httpRequest.getRequestURL().toString().contains(access)) {
                hasAccess = true;
                break;
            }
        }
        if (!hasAccess) {
            PrintWriter out = httpResponse.getWriter();
            JSONObject jsonObject = new JSONObject(String.format(
                    "{\"code\":\"%s\"}", HttpServletResponse.SC_UNAUTHORIZED));
            out.print(jsonObject);
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.flush();
            return;
        } else {
            chain.doFilter(request, response);
        }
    }
}
