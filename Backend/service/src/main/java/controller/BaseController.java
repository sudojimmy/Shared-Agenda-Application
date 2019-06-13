package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import store.DataStore;

public abstract class BaseController {
    public static DataStore dataStore = new DataStore();
    Logger logger = LoggerFactory.getLogger(BaseController.class);

    public void assertPropertyValid(final String prop, final String propName) {
        if (prop == null || prop.isEmpty()) { invalidProperty(propName); }
    }

    public void assertPropertyValid(final Object prop, final String propName) {
        if (prop == null) { invalidProperty(propName); }
    }

    public void assertDatabaseObjectFound(final Object o, final String propName) {
        if (o == null) { objectNotFound(propName); }
    }

    private void objectNotFound(final String propName) {
        String errMsg = String.format("%s Not Found!", propName);
        logger.error(errMsg);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, errMsg);
    }

    public void invalidProperty(final String propName) {
        String errMsg = String.format("Invalid %s!", propName);
        logger.error(errMsg);
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errMsg);
    }
}
