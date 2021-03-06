package security;

import org.json.JSONObject;
import utils.USERTYPE;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter(filterName = "authenfilter")
public class AuthenFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("application/json");
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", "authorization");
        if(!httpRequest.getMethod().equals("OPTIONS") && TokenVerification.getRoleFromRequest(httpRequest).equals(USERTYPE.UNKNOWN)){
            PrintWriter out = httpResponse.getWriter();
            JSONObject jsonObject = new JSONObject(String.format(
                    "{\"code\":\"%s\"}",HttpServletResponse.SC_UNAUTHORIZED));
            out.print(jsonObject);
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.flush();
            return;
        }else {
            chain.doFilter(request, response);
        }
    }
}
