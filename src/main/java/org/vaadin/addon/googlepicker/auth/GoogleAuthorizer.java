/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.addon.googlepicker.auth;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractJavaScriptExtension;
import com.vaadin.ui.JavaScriptFunction;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;

/**
 *
 * @author se
 */
@JavaScript({"GoogleAuthorizer.js", "https://apis.google.com/js/api.js?onload=org_vaadin_addon_googlepicker_auth_GoogleAuthorizer_onApiLoad"})
public class GoogleAuthorizer extends AbstractJavaScriptExtension {

    public interface AuthorizationListener extends Serializable {

        void authorized(String scope, String accessToken);
    }

    Map<String, AuthorizationListener> authListeners
            = new HashMap<String, AuthorizationListener>();

    public GoogleAuthorizer(String clientId) {

        getState().clientId = clientId;
                
        addFunction("onAuthorizeScope", new JavaScriptFunction() {

            @Override
            public void call(JSONArray arguments) throws JSONException {
                //TODO:
                String scopeString = arguments.getString(0);
                String accessToken = arguments.getString(1);
                String[] scopes = scopeString.split(" ");
                for (String scope : scopes) {
                    AuthorizationListener l = authListeners.get(scope);
                    if (l != null) {
                        l.authorized(scope, accessToken);
                    }
                }
            }
        });

    }

    protected GoogleAuthorizerState getState() {
        return (GoogleAuthorizerState) super.getState();
    }

    public void authorize(String scope, AuthorizationListener listener) {
        authListeners.put(scope, listener);
        callFunction("authorizeScope", scope);
    }

}
