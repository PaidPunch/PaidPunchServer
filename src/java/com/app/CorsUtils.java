package com.app;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.server.SimpleLogger;

public class CorsUtils 
{
    private final static String currentClassName = CorsUtils.class.getSimpleName();

    // Hard-coded list for domains which are allowed to send requests to all methods that add the
    // CORS header info defined in this class to the response objects, used e.g. by dev/test/prod
    // machines running the PaidPunchWebsite
    // Without CORS header info no requests are allowed across domains.
    private final static Set<String> allowedOrigins = new HashSet<String>(Arrays.asList(new String[] {
            "http://localhost:8080",
            "http://tb-mac.local:8080",
            "http://www.paidpunch.com",
            "https://www.paidpunch.com",
            "http://testsite.paidpunch.com",
            "http://dev.paidpunch.com:8080",
            "http://devtest.paidpunch.com:8080"}));

    public static void addOptionsCorsHeaderInfo(HttpServletRequest request, HttpServletResponse response) 
    {
        String requestOrigin = request.getHeader("origin");
        response.setStatus(204);
        response.setContentType("application/json");
        if (allowedOrigins.contains(requestOrigin)) 
        {
            SimpleLogger.getInstance().trace(currentClassName, "Incoming request from origin: " + requestOrigin + " allowed.");
            response.addHeader("Access-Control-Allow-Origin", requestOrigin);
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.addHeader("Access-Control-Allow-Headers",
                    "content-type, accept, x-requested-with, api-version, enabled-only, sessionid, Authorization, X-Authorization");
            response.addHeader("access-control-max-age", "10");
        }
        else 
        {
            SimpleLogger.getInstance().trace(currentClassName, "Incoming request from origin " + requestOrigin + " rejected.");
        }
    }

    public static void addCorsHeaderInfo(HttpServletRequest request, HttpServletResponse response) 
    {
        String requestOrigin = request.getHeader("origin");
        if (allowedOrigins.contains(requestOrigin)) {
            SimpleLogger.getInstance().trace(currentClassName, "Incoming request from origin: " + requestOrigin + " allowed.");
            response.addHeader("Access-Control-Allow-Origin", requestOrigin);
        } 
        else 
        {
            SimpleLogger.getInstance().trace(currentClassName, "Incoming request from origin " + requestOrigin + " rejected.");
        }
    }

}
