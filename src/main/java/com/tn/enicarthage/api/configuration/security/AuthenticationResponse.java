package com.tn.enicarthage.api.configuration.security;

import java.io.Serializable;


public class AuthenticationResponse implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;
    private  final String  username;
    private final String token;

    public AuthenticationResponse(String token,String username) {
        this.token = token;
        this.username=username;

    }

    public String getToken() {
        return this.token;
    }

}
