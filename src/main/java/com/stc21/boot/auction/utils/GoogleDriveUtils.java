package com.stc21.boot.auction.utils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.io.*;
import java.util.Collections;
import java.util.List;

/**
 * The GoogleDrive instance of the storage class and the connection to it.
 * The client_secret.json file must be in the root of the project in the folder
 * ./src/main/resources or another one specified in the settings.
 * The client_secret.json file is obtained when registering the OAuth 2.0 key for the account.
 */
public class GoogleDriveUtils {

    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    // Directory to store user credentials for this application.
    private static final File CREDENTIALS_FOLDER = new File(".", "credentials");
    private static final String CLIENT_SECRET_FILE_NAME = "client_secret.json";
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    // Global instance of the {@link FileDataStoreFactory}.
    private static FileDataStoreFactory DATA_STORE_FACTORY;
    // Global instance of the HTTP transport.
    private static HttpTransport HTTP_TRANSPORT;
    private static Drive driveServiceInstance;

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(CREDENTIALS_FOLDER);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }


    private static Credential getCredentials() throws IOException {
        File clientSecretFilePath = new File(CLIENT_SECRET_FILE_NAME);

//        if (!clientSecretFilePath.exists()) {
//            throw new FileNotFoundException("Please copy " + CLIENT_SECRET_FILE_NAME //
//                    + " to folder: " + CREDENTIALS_FOLDER.getAbsolutePath());
//        }
        InputStream in = new FileInputStream(clientSecretFilePath);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        return credential;
    }

    /**
     * Creating a new connection to GoogleDrive based on the received permissions from the client_secret.json file.
     * If the connection is created, return the existing instance.
     *
     * @return - GoogleDrive connection instance.
     * @throws IOException - Throws if it was not possible to connect to GoogleDrive.
    users */
    public static Drive getDriveService() throws IOException {
        if (driveServiceInstance != null) {
            return driveServiceInstance;
        }
        Credential credential = getCredentials();
        driveServiceInstance = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
        return driveServiceInstance;
    }
}