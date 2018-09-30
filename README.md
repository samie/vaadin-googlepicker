[![Published on Vaadin  Directory](https://img.shields.io/badge/Vaadin%20Directory-published-00b4f0.svg)](https://vaadin.com/directory/component/googlepicker-for-vaadin)
[![Stars on Vaadin Directory](https://img.shields.io/vaadin-directory/star/googlepicker-for-vaadin.svg)](https://vaadin.com/directory/component/googlepicker-for-vaadin)

vaadin-googlepicker
===================

Google Picker integration for Vaadin. Implements a UI extension that you can use to open up the Google Picker interface to choose files and other resources from Google Drive.

## Creating a GooglePicker
To use Google Picker in your application you need to get a API Key and Client ID from Google Developer Console
https://console.developers.google.com/
```java
        picker = new GooglePicker(API_KEY, CLIENT_ID);
        picker.addSelectionListener(new GooglePicker.SelectionListener() {

            @Override
            public void documentSelected(GooglePicker.Document document) {
                String name = document.getName();
                // Do something with the chosen document
            }

        });
        addExtension(picker);

        // Open selection dialog for spreadseets
        picker.pickDocument(GooglePicker.Type.SPREADSHEETS);

```

More information about Google Picker: https://developers.google.com/picker/

