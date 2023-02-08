package com.wtf.cauth.exception;

public class ErrorConstants {
    public static final String BAD_REQUEST = "bad_request";
    public static final String NOT_FOUND = "resource_not_found";
    public static final String UN_AUTHENTICATED = "un_authenticated";
    public static final String INTERNAL_SERVER_ERROR = "internal_server_error";

    public static final String AUTH_TOKEN_NOT_PRESENT_NOT_VALID = "auth token is not present or not valid";
    public static final String FAILED_TO_VERIFY_AUTH_TOKEN = "failed to verify auth-token";
    public static final String AUTH_TOKEN_DOES_NOT_BELONGS_TO_USER= "auth token doesn't belong to a user";

    public static final String JSON_PATCH_ERROR = "json_patch_error";
    public static final String MALFORMED_URL = "malformed_url";
    public static final String OBSERVE_EXT_SERVICE_FAILURE = "observe_ext_service_failure";
}
