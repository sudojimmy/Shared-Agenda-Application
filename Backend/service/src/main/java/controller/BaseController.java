package controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import constant.ApiConstant;
import store.DataStore;

public abstract class BaseController {
    public static DataStore dataStore = new DataStore();

    Logger logger = LoggerFactory.getLogger(BaseController.class);

    protected String getSessionAccountId(HttpServletRequest servletRequest) {
        return (String)servletRequest.getSession().getAttribute(ApiConstant.ACCOUNT_ACCOUNT_ID);
    }
}