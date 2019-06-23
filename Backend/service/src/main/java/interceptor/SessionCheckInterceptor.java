package interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import constant.ApiConstant;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

@Component
public class SessionCheckInterceptor implements HandlerInterceptor {
    Logger logger = LoggerFactory.getLogger(SessionCheckInterceptor.class);
    private static final JacksonFactory jsonFactory = new JacksonFactory();
    private static final HttpTransport transport = new NetHttpTransport();


    private static final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
    .build();


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String idTokenString = request.getHeader(ApiConstant.GOOGLE_TOKEN);
        if (idTokenString == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Invalid Token");
        }
        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            Payload payload = idToken.getPayload();
            String userId = payload.getSubject();
            logger.info("User ID: " + userId);

            // Get profile information from payload
            String email = payload.getEmail();
            HttpSession session = request.getSession();
            session.setAttribute(ApiConstant.ACCOUNT_ACCOUNT_ID,email);
            // boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            return true;
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Invalid Token");
    }

    // @Override
    // public void postHandle( HttpServletRequest request, HttpServletResponse response,
    //         Object handler, ModelAndView modelAndView) throws Exception {
    //     // System.out.println("---method executed---");
    // }

    // @Override
    // public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
    //         Object handler, Exception ex) throws Exception {
    //     // System.out.println("---Request Completed---");
    // }
 }