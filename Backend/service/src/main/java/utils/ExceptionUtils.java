package utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionUtils {

    public static Logger exceptionLogger = LoggerFactory.getLogger(ExceptionUtils.class);

    public static void assertPropertyValid(final String prop, final String propName) {
        if (prop == null || prop.isEmpty()) { ExceptionUtils.invalidProperty(propName); }
    }

    public static void assertPropertyValid(final Object prop, final String propName) {
        if (prop == null) { ExceptionUtils.invalidProperty(propName); }
    }

    public static void assertDatabaseObjectFound(final Object o, final String propName) {
        if (o == null) { ExceptionUtils.objectNotFound(propName); }
    }

    public static void objectNotFound(final String propName) {
        String errMsg = String.format("%s Not Found!", propName);
        exceptionLogger.error(errMsg);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, errMsg);
    }

    public static void invalidProperty(final String propName) {
        String errMsg = String.format("Invalid %s!", propName);
        exceptionLogger.error(errMsg);
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errMsg);
    }
}