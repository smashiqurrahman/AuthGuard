package com.ashiq.AuthGuard.helper;

import com.ashiq.AuthGuard.dto.Response;

public interface CommonFunction {
    default Response getSuccessResponse(String message) {
        Response response = new Response();
        response.setSuccess(true);
        response.setMessage(message);
        return response;
    }

    default Response getSuccessResponse(String message, Response response) {
        response.setSuccess(true);
        response.setMessage(message);
        return response;
    }


    default Response getErrorResponse(String message) {
        Response response = new Response();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }

    default Response getErrorResponse(String message, Response response) {
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }
}
