package security;

import org.json.JSONObject;
import utils.USERTYPE;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter(filterName = "authorfilter")
public class AuthorFilter implements Filter {
    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        //System.out.println("AuthorFilter!!!");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("application/json");
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", "authorization");
        if (httpRequest.getMethod().equals("OPTIONS")) {
            chain.doFilter(request, response);
            return;
        }
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
                    "{\"code\":\"%s\"}", HttpServletResponse.SC_FORBIDDEN));
            out.print(jsonObject);
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            //System.out.println("-----------Authorize fail---------------");
            out.flush();
            return;
        } else {
            chain.doFilter(request, response);
        }
    }
}
