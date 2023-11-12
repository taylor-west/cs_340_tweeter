package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;

abstract public class BasePresenter {
    public interface View {
        void showErrorMessage(String message);

        void showInfoMessage(String message);
    }

    abstract protected View getView();


    public View view;

    protected Cache cache;
    protected UserService userService;
    protected StatusService statusService;

    protected UserService getUserService() {
        if (userService == null) {
            return new UserService();
        } else {
            return this.userService;
        }
    }

    protected StatusService getStatusService() {
        if (statusService == null) {
            return new StatusService();
        } else {
            return this.statusService;
        }
    }
}
