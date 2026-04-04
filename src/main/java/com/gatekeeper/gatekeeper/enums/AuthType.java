package com.gatekeeper.gatekeeper.enums;

public enum AuthType {
    BEARER,         // Authorization: Bearer {key}
    QUERY_PARAM,    // ?{paramName}={key}   e.g. ?key=, ?appid=, ?api_key=
    CUSTOM_HEADER,  // {headerName}: {key}  e.g. x-api-key, x-rapidapi-key
    BASIC_AUTH,     // Authorization: Basic Base64(username:password)
    NO_AUTH         // public APIs, no key needed
}
