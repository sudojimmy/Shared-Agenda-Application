package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import store.DataStore;


public abstract class BaseController {
    public static DataStore dataStore = new DataStore();
    Logger logger = LoggerFactory.getLogger(BaseController.class);
}
