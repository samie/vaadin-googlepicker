window.org_vaadin_addon_googlepicker_GooglePicker = function() {

    //TODO: Can we somehow handle multiple pickers at time?
    var self = this;
    this.oauthToken = null;

    /* Inherited (read: copied) methods from GoogleAuthenticator.js */
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
    /* End of Inherited (read: copied) methods from GoogleAuthenticator.js */
    
    this.buttonClicked = function() {
        if (self.getState().scope) {
            self.authorize(self.getState().clientId, self.getState().scope, false, self.handleAuthResult);
        } else {
            self.createPicker();
        }
    };

    this.handleAuthResult = function(authResult) {
        if (authResult && !authResult.error) {
            self.oauthToken = authResult.access_token;
            self.createPicker();
        }
    };

    // Create and render a Picker object for picking user Photos.
    this.createPicker = function() {
        if (window.org_vaadin_addon_googlepicker_GooglePicker_pickerApiLoaded && (self.oauthToken || !self.getState().scope)) {
            var vid = self.getState().viewId;
            if (vid.indexOf("google.picker.ViewId.") === 0) {
                vid = google.picker.ViewId[vid.substring(21)];
            }
            // TODO: Upload view type missing

            var picker = new google.picker.PickerBuilder().
                    addView(vid).
                    setOAuthToken(self.oauthToken).
                    setDeveloperKey(self.getState().developerKey).
                    setCallback(self.pickerCallback).
                    build();
            picker.setVisible(true);
        }
    };

    /** Pass the selected document to the server-side callbacks. */
    this.pickerCallback = function(data) {
        if (data[google.picker.Response.ACTION] == google.picker.Action.PICKED) {
            var doc = data[google.picker.Response.DOCUMENTS][0];
            var id = doc[google.picker.Document.ID];
            var serviceId = doc[google.picker.Document.SERVICE_ID];
            var name = doc[google.picker.Document.NAME];
            var type = doc[google.picker.Document.TYPE];
            var mimeType = doc[google.picker.Document.MIME_TYPE];
            var lastEdited = doc[google.picker.Document.LAST_EDITED_UTC];
            var url = doc[google.picker.Document.URL];
            var embeddableUrl = doc[google.picker.Document.EMBEDDABLE_URL];
            var iconUrl = doc[google.picker.Document.ICON_URL];
            var latitude = doc[google.picker.Document.LATITUDE];
            var longitude = doc[google.picker.Document.LONGITUDE];
            self.onDocumentSelected(id, serviceId, name, type, mimeType, lastEdited, url, embeddableUrl, iconUrl, latitude, longitude, self.oauthToken);
        }
    };

};

/* "static" method for tracking API loads */
window.org_vaadin_addon_googlepicker_GooglePicker_pickerApiLoaded = false;

window.org_vaadin_addon_googlepicker_GooglePicker_onPickerApiLoaded = function() {
    window.org_vaadin_addon_googlepicker_GooglePicker_pickerApiLoaded = true;
};

window.org_vaadin_addon_googlepicker_GooglePicker_onApiLoad = function() {
    window.org_vaadin_addon_googlepicker_GooglePicker_apiLoaderLoaded = true;
    window.gapi.load('picker', {'callback': org_vaadin_addon_googlepicker_GooglePicker_onPickerApiLoaded});
}









