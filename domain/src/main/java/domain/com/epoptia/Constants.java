package domain.com.epoptia;

public class Constants {

    //region DEVICE

    //DeviceUtility categories
    public static final int PHONE = 1;
    public static final int TABLET = 2;

    //DeviceUtility mode states
    public static final int KIOSK_MODE_STATE = 10;
    public static final int DEFAULT_MODE_STATE = 15;

    //endregion

    //region ERRORS

    //Error handling
    public static final int MAX_RETRIES = 3;
    public static final int RETRY_DELAY_MILLIS = 2000;

    public static final String INVALID_DTO = "Μη έγκυρο dto";
    public static final String INVALID_DOMAIN_MODEL = "Μη έγκυρο domain model";
    public static final String INVALID_SUBDOMAIN = "Μη έγκυρο sub domain.";

    public static final String SUBDOMAIN_REQUIRED = "Το domain είναι υποχρεωτικό.";
    public static final String USERNAME_PASSWORD_REQUIRED = "Το όνομα χρήστη και ο κωδικός πρόσβασης είναι υποχρεωτικά.";
    public static final String WORKSTATION_ID_REQUIRED = "Μη έγκυρο workstation.";

    //Network
    public static final String NO_WIFI_INFO = "Μη διαθέσιμο WIFI.";

    //endregion

    //region VIEWMODEL

    //Subject types
    public static final int COMPLETABLE_SUBJECT = 1;
    public static final int SINGLE_SUBJECT = 2;
    public static final int FLOWABLE_SUBJECT = 3;

    //view state
    public static final int VIEW_STATE_ATTACHED = 1;
    public static final int VIEW_STATE_RESUMED = 2;
    public static final int VIEW_STATE_DETACHED = 3;

    //request state
    public static final int REQUEST_RUNNING = 10;
    public static final int REQUEST_NONE = 0;

    //endregion

    //region API

    //Status codes
    public static final int RESPONSE_OK = 200;

    //Actions
    public static final String ACTION_VALIDATE_ADMIN = "validate_admin";
    public static final String ACTION_VALIDATE_CUSTOMER_DOMAIN = "validate_customer_domain";
    public static final String ACTION_GET_WORKSTATIONS = "get_workstations";
    public static final String ACTION_UNLOCK_DEVICE = "unlock_device";
    public static final String ACTION_GET_STATIONWORKERS = "get_workers";

    //endregion

}
