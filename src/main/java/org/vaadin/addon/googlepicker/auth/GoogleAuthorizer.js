window.org_vaadin_addon_googlepicker_auth_GoogleAuthorizer = function() {

    var self = this;

    this.authorizeScope = function(scope) {
        self.authorize(self.getState().clientId, scope, false, self.handleAuthScopeResult);
    }

    this.handleAuthScopeResult = function(authResult) {
        if (authResult && !authResult.error) {
            self.onAuthorizeScope(authResult.scope, authResult.access_token);
        }
    }

    this.authorize = function(clientId, scope, isImmediate, handler) {
        if (window.org_vaadin_addon_googlepicker_auth_GoogleAuthorizer_authApiLoaded) {
            window.gapi.auth.authorize(
                    {
                        'client_id': clientId,
                        'scope': scope,
                        'immediate': isImmediate
                    },
            handler);
        }

    };
    
};

/* "static" method for tracking API loads */

window.org_vaadin_addon_googlepicker_auth_GoogleAuthorizer_apiLoaderLoaded = false;
window.org_vaadin_addon_googlepicker_auth_GoogleAuthorizer_authApiLoaded = false;

window.org_vaadin_addon_googlepicker_auth_GoogleAuthorizer_onAuthApiLoaded = function() {
    window.org_vaadin_addon_googlepicker_auth_GoogleAuthorizer_authApiLoaded = true;
};

window.org_vaadin_addon_googlepicker_auth_GoogleAuthorizer_onApiLoad = function() {
    window.org_vaadin_addon_googlepicker_auth_GoogleAuthorizer_apiLoaderLoaded = true;
    window.gapi.load('auth', {'callback': org_vaadin_addon_googlepicker_auth_GoogleAuthorizer_onAuthApiLoaded});
};









