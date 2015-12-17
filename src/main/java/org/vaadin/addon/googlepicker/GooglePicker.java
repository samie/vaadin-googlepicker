/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.addon.googlepicker;

import org.vaadin.addon.googlepicker.auth.GoogleAuthorizer;
import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.JavaScriptFunction;

import elemental.json.JsonArray;
import elemental.json.JsonType;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author se
 */
@JavaScript({"GooglePicker.js", "https://apis.google.com/js/api.js?onload=org_vaadin_addon_googlepicker_GooglePicker_onApiLoad"})
public class GooglePicker extends GoogleAuthorizer {

    public enum Type {

        DOCS("All Google Drive items", "google.picker.ViewId.DOCS", "https://www.googleapis.com/auth/drive.readonly"),
        DOCS_IMAGES("Google Drive photos", "google.picker.ViewId.DOCS_IMAGES)", "https://www.googleapis.com/auth/drive.readonly"),
        DOCUMENTS("Google Drive Documents", "google.picker.ViewId.DOCUMENTS)", "https://www.googleapis.com/auth/drive.readonly"),
        PRESENTATIONS("Google Drive Presentations", "google.picker.ViewId.PRESENTATIONS", "https://www.googleapis.com/auth/drive.readonly"),
        SPREADSHEETS("Google Drive Spreadsheet", "google.picker.ViewId.SPREADSHEETS", "https://www.googleapis.com/auth/drive.readonly"),
        FORMS("Google Drive Forms", "google.picker.ViewId.FORMS", "https://www.googleapis.com/auth/drive.readonly"),
        DOCS_IMAGES_AND_VIDEOS("Google Drive photos and videos", "google.picker.ViewId.DOCS_IMAGES_AND_VIDEOS", "https://www.googleapis.com/auth/drive.readonly"),
        DOCS_VIDEOS("Google Drive videos", "google.picker.ViewId.DOCS_VIDEOS", "https://www.googleapis.com/auth/drive.readonly"),
        FOLDERS("Google Drive Folders", "google.picker.ViewId.FOLDERS", "https://www.googleapis.com/auth/drive.readonly"),
        PDFS("Adobe PDF files stored in Google Drive", "google.picker.ViewId.PDFS", "https://www.googleapis.com/auth/drive.readonly"),
        UPLOAD("Upload documents to Google Drive.", "google.picker.â€‹DocsUploadView", "https://www.googleapis.com/auth/drive"),
        PHOTO_ALBUMS("Picasa Web Albums photo albums", "google.picker.ViewId.PHOTO_ALBUMS", "https://www.googleapis.com/auth/photos"),
        PHOTOS("Picasa Web Albums photos", "google.picker.ViewId.PHOTOS", "https://www.googleapis.com/auth/photos"),
        PHOTO_UPLOAD("Upload to Picasa Web Albums", "google.picker.ViewId.PHOTO_UPLOAD", "https://www.googleapis.com/auth/photos.upload"),
        IMAGE_SEARCH("Google Image Search", "google.picker.ViewId.IMAGE_SEARCH", null),
        MAPS("Google Maps", "google.picker.ViewId.MAPS", null),
        VIDEO_SEARCH("Video Search", "google.picker.ViewId.VIDEO_SEARCH", null),
        WEBCAM("Webcam photos and videos", "google.picker.ViewId.WEBCAM", "https://www.googleapis.com/auth/photos.upload"),
        YOUTUBE("Your YouTube videos", "google.picker.ViewId.YOUTUBE", "https://www.googleapis.com/auth/youtube"),
        RECENTLY_PICKED("A collection of most recently selected items", "google.picker.ViewId.RECENTLY_PICKED", null);

        private String text;
        private String viewId;
        private String scope;

        Type(String text, String viewId, String scope) {
            this.text = text;
            this.viewId = viewId;
            this.scope = scope;
        }
    }

    private static String DEFAULT_CAPTION = "Choose file from Google Drive";

    private Document document;

    public interface SelectionListener extends Serializable {

        void documentSelected(Document document);
    }

    List<SelectionListener> listeners
            = new CopyOnWriteArrayList<SelectionListener>();

    public void addSelectionListener(
            SelectionListener listener) {
        listeners.add(listener);
    }

    public void removeSelectionListener(
            SelectionListener listener) {
        listeners.remove(listener);
    }

    public GooglePicker(String developerKey, String clientId) {
        this(developerKey, clientId, Type.DOCS.viewId, Type.DOCS.scope);
    }

    public GooglePicker(String developerKey, String clientId, Type type) {
        this(developerKey, clientId, type.viewId, type.scope);
    }

    public GooglePicker(String developerKey, String clientId, Type type, String scope) {
        this(developerKey, clientId, type.viewId, scope);
    }

    public static class Document {

        private String id;
        private String serviceId;
        private String name;
        private String type;
        private String mimeType;
        private String lastEdited;
        private String url;
        private String embeddableUrl;
        private String iconUrl;
        private String latitude;
        private String longitude;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public String getUrlString() {
            return url;
        }

        public URL getUrl() {
            try {
                return new URL(url);
            } catch (MalformedURLException ex) {
            }
            return null;
        }

        public URL getEmbeddableUrl() {
            try {
                return new URL(embeddableUrl);
            } catch (MalformedURLException ex) {
            }
            return null;
        }

        public String getServiceId() {
            return serviceId;
        }

        public String getMimeType() {
            return mimeType;
        }

        public String getLastEdited() {
            return lastEdited;
        }

        public URL getIconUrl() {
            try {
                return new URL(iconUrl);
            } catch (MalformedURLException ex) {
            }
            return null;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getDocumentApiUrl() {
            return "https://spreadsheets.google.com/feeds/spreadsheets/private/full/" + id + "";
        }

        @Override
        public String toString() {
            return "Document ["
                    + "id=" + id
                    + ", serviceId=" + serviceId
                    + ", name=" + name
                    + ", type=" + type
                    + ", mimeType=" + mimeType
                    + ", lastEdited=" + lastEdited
                    + ", url=" + url
                    + ", embeddableUrl=" + embeddableUrl
                    + ", iconUrl=" + iconUrl
                    + ", latitude=" + latitude
                    + ", longitude=" + longitude
                    + "]";
        }

    }

    private GooglePicker(String developerKey, String clientId, String viewId, String scope) {
        super(clientId);

        getState().developerKey = developerKey;
        getState().scope = scope;
        getState().viewId = viewId;

        addFunction("onDocumentSelected", new JavaScriptFunction() {

            @Override
            public void call(JsonArray arguments) {

                /*
                 var id = doc[google.picker.Document.ID];
                 var serviceId = doc[google.picker.Document.SERVICE_ID];
                 var name = doc[google.picker.Document.NAME];
                 var type = doc[google.picker.Document.TYPE];
                 var mimeType = doc[google.picker.Document.MIME_TYPE];
                 var lastEdited = doc[google.picker.Document.LAST_EDITED_UTC];
                 var webUrl = doc[google.picker.Document.URL];
                 var embeddableUrl = doc[google.picker.Document.EMBEDDABLE_URL];
                 var iconUrl = doc[google.picker.Document.ICON_URL];
                 var latitude = doc[google.picker.Document.LATITUDE];
                 var longitude = doc[google.picker.Document.LONGITUDE];

                 */
                Document doc = new Document();
                doc.id = arguments.get(0).getType().equals(JsonType.NULL) ? null : arguments.getString(0);
                doc.serviceId = arguments.get(1).getType().equals(JsonType.NULL) ? null : arguments.getString(1);
                doc.name = arguments.get(2).getType().equals(JsonType.NULL) ? null : arguments.getString(2);
                doc.type = arguments.get(3).getType().equals(JsonType.NULL) ? null : arguments.getString(3);
                doc.mimeType = arguments.get(4).getType().equals(JsonType.NULL) ? null : arguments.getString(4);
                doc.lastEdited = arguments.get(5).getType().equals(JsonType.NULL) ? null : arguments.getNumber(5) + "";
                doc.url = arguments.get(6).getType().equals(JsonType.NULL) ? null : arguments.getString(6);
                doc.embeddableUrl = arguments.get(7).getType().equals(JsonType.NULL) ? null : arguments.getString(7);
                doc.iconUrl = arguments.get(8).getType().equals(JsonType.NULL) ? null : arguments.getString(8);
                doc.latitude = arguments.get(9).getType().equals(JsonType.NULL) ? null : arguments.getString(9);
                doc.longitude = arguments.get(10).getType().equals(JsonType.NULL) ? null : arguments.getString(10);
                document = doc;
                for (SelectionListener listener : listeners) {
                    listener.documentSelected(document);
                }
            }
        });
    }

    public String getDocumentKey() {
        String val = getState().value;
        String key = null;
        if (val != null) {
            int s = val.indexOf("key=");
            int e = val.indexOf("&", s);
            if (s > 0 && e > s) {
                key = val.substring(s + 4, e);
            }
        }
        return key;
    }

    protected GooglePickerState getState() {
        return (GooglePickerState) super.getState();
    }

    public Document getDocument() {
        return this.document;
    }

    public void pickDocument() {
        callFunction("buttonClicked");
    }

    public void pickDocument(Type type) {
        getState().viewId = type.viewId;
        getState().scope = type.scope;
        callFunction("buttonClicked");
    }

}
