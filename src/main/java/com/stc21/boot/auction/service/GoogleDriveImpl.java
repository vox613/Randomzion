package com.stc21.boot.auction.service;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.stc21.boot.auction.entity.Photo;
import com.stc21.boot.auction.utils.GoogleDriveUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.api.services.drive.Drive.*;

/**
 * A class that implements the functionality of interacting with GoogleDrive.
 */
@Service
public class GoogleDriveImpl implements GoogleDriveService {

    private final String ROOT_FOLDER_NAME = "Reduction";
    private final String AVATAR_FOLDER_NAME = "avatars";
    private final String LOT_FOLDER_NAME = "lots";

    private final String RootFolderID;
    private final String AvatarsFolderID;
    private final String LotsFolderID;
    private final Drive driveService;


    public GoogleDriveImpl() throws IOException {
        this.driveService = GoogleDriveUtils.getDriveService();
        List<String> listID = createFolderSystem();
        this.RootFolderID = listID.get(0);
        this.AvatarsFolderID = listID.get(1);
        this.LotsFolderID = listID.get(2);
    }

    /**
     * Uploads media files of the lot to GoogleDrive.
     * A unique folder is created for each lot with the name of the transferred lotID in the Root folder.
     *
     * @param lotID - Lot ID that owns the media file.
     * @param file  - Array of media to record.
     * @return - A list of objects of the Photo class with the URL links to the content contained in them.
     * @throws IOException - Throws if an error occurs while writing files to Google Drive.
     */
    @Override
    public List<Photo> uploadLotMedia(Long lotID, MultipartFile[] file) throws IOException {
        return uploadMedia(lotID, file, LotsFolderID);
    }

    /**
     * Uploads media files of the Avatars to GoogleDrive.
     * A unique folder is created for each avatar with the name of the transferred userID in the Root folder.
     *
     * @param userID - User ID that owns the media file.
     * @param file-  Array of media to record.
     * @return - A list of objects of the Photo class with the URL links to the content contained in them.
     * @throws IOException - Throws if an error occurs while writing files to Google Drive.
     */
    @Override
    public List<Photo> uploadAvatarMedia(Long userID, MultipartFile[] file) throws IOException {
        return uploadMedia(userID, file, AvatarsFolderID);
    }

    /**
     * Allows you to get media files of a user account.
     *
     * @param userID - User ID that owns the media file.
     * @return - A list of objects of the Photo class with the URL links to the content contained in them.
     * @throws IOException - Throws if an error occurs while reading files from Google Drive. Or the file does not exist.
     */
    @Override
    public List<Photo> getUserImage(Long userID) throws IOException {
        return getImages(userID, AvatarsFolderID);
    }

    /**
     * Allows you to get media files of a specific lot.
     *
     * @param lotID - Lot ID that owns the media file.
     * @return - A list of objects of the Photo class with the URL links to the content contained in them.
     * @throws IOException - Throws if an error occurs while reading files from Google Drive. Or the file does not exist.
     */
    @Override
    public List<Photo> getLotImage(Long lotID) throws IOException {
        return getImages(lotID, LotsFolderID);
    }

    /**
     * Deletes a folder with all contents.
     *
     * @param parentFolderId - Folder ID of the parent holding the deleted folder.
     *                       Standard project folder IDs are constants:
     *                       1) RootFolderID      - Project root folder
     *                       2) AvatarsFolderID   - User avatars folder
     *                       3) LotsFolderID      - Folder with media files of lots
     *                       4) null              -   to search for folders on all Google Drive
     * @param folderName     - The name of the folder to be deleted.
     * @return - The result of the attempt to delete is true - the removal was successful, false - an error occurred
     * or the folder does not exist
     */
    @Override
    public Boolean deleteFolder(String parentFolderId, String folderName) {
        try {
            String deletedFolderID = checkFolderExist(parentFolderId, folderName);
            System.out.println("deletedFolderID = " + deletedFolderID);
            if (!deletedFolderID.isEmpty()) {
                driveService.files().delete(deletedFolderID).execute();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a file from current folder.
     *
     * @param parentFolderID - Folder ID of the parent holding the deleted folder.
     *                       Standard project folder IDs are constants:
     *                       1) RootFolderID      - Project root folder
     *                       2) AvatarsFolderID   - User avatars folder
     *                       3) LotsFolderID      - Folder with media files of lots
     *                       4) null              -   to search for folders on all Google Drive
     * @param fileName       - The name of the file to be deleted.
     * @return - The result of the attempt to delete is true - the removal was successful, false - an error occurred
     * or the folder does not exist
     */
    @Override
    public Boolean deleteFile(String parentFolderID, String fileName) {
        try {
            List<File> filesList = getFiles(parentFolderID);
            String deletedFileID = filesList.stream()
                    .filter(x -> x.getName().equals(fileName))
                    .limit(1)
                    .map(File::getId)
                    .collect(Collectors.joining());

            if (!deletedFileID.isEmpty()) {
                driveService.files().delete(deletedFileID).execute();
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if a folder exists.
     *
     * @param parentFolderID - Folder ID of the parent holding the deleted folder.
     *                       Standard project folder IDs are constants:
     *                       1) RootFolderID      - Project root folder
     *                       2) AvatarsFolderID   - User avatars folder
     *                       3) LotsFolderID      - Folder with media files of lots
     *                       4) null              -   to search for folders on all Google Drive
     * @param FolderName     - The name of the folder to be deleted.
     * @return - The result of the attempt to delete is true - the removal was successful, false - an error occurred
     * or the folder does not exist
     */
    @Override
    public String checkFolderExist(String parentFolderID, String FolderName) {
        try {
            List<File> foldersList = getFolderByName(parentFolderID, FolderName);
            return foldersList.isEmpty() ? "" : foldersList.get(0).getId();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * It searches for a folder by the given name.
     *
     * @param parentFolderID - Folder ID of the parent holding the deleted folder.
     *                       Standard project folder IDs are constants:
     *                       1) RootFolderID      - Project root folder
     *                       2) AvatarsFolderID   - User avatars folder
     *                       3) LotsFolderID      - Folder with media files of lots
     *                       4) null              -   to search for folders on all Google Drive
     * @param subFolderName - Search Folder Name.
     * @return - Returns a list with found files of folders.
     * @throws IOException - Throws if an error occurs while reading from Google Drive. Or the file/folder does not exist.
     */
    @Override
    public List<File> getFolderByName(String parentFolderID, String subFolderName) throws IOException {
        String query;
        if (parentFolderID == null) {
            query = " name = '" + subFolderName + "' "
                    + " and mimeType = 'application/vnd.google-apps.folder' "
                    + " and 'root' in parents";
        } else {
            query = " name = '" + subFolderName + "' "
                    + " and mimeType = 'application/vnd.google-apps.folder' "
                    + " and '" + parentFolderID + "' in parents";
        }

        return getContent(query);
    }



    private List<Photo> uploadMedia(Long id, MultipartFile[] file, String parentFolder) throws IOException {
        String folderID = foldersTrack(id, parentFolder);
        List<Photo> list = new ArrayList<>();
        for (MultipartFile filePart : file) {
            File diskDriveFile = createFile(folderID, filePart.getContentType(), filePart.getOriginalFilename(), filePart.getBytes());
            list.add(new Photo(diskDriveFile.getWebContentLink()));
        }

        return list;
    }

    private List<Photo> getImages(Long id, String parentFolderID) throws IOException {
        List<File> list = getFolderByName(parentFolderID, String.valueOf(id));
        List<Photo> listImageURL = new ArrayList<>();

        if (list.isEmpty()) {
            throw new FileNotFoundException("Folder is not exist");
        } else {
            String folderID = list.get(0).getId();
            list = getFiles(folderID);

            for (File file : list) {
                String webContentLink = file.getWebContentLink();
                listImageURL.add(new Photo(webContentLink));
            }
        }
        return listImageURL;
    }


    private String foldersTrack(Long id, String parentFolderID) throws IOException {
        String folderID = checkFolderExist(parentFolderID, String.valueOf(id));
        if (folderID.isEmpty()) {
            folderID = createFolder(parentFolderID, String.valueOf(id)).getId();
        }
        return folderID;
    }

    private List<String> createFolderSystem() throws IOException {
        List<String> foldersIDs = new ArrayList<>();

        String tempID = checkFolderExist(null, ROOT_FOLDER_NAME);
        foldersIDs.add((tempID.isEmpty()) ? createFolder(null, ROOT_FOLDER_NAME).getId() : tempID);

        tempID = checkFolderExist(foldersIDs.get(0), AVATAR_FOLDER_NAME);
        foldersIDs.add((tempID.isEmpty()) ? createFolder(foldersIDs.get(0), AVATAR_FOLDER_NAME).getId() : tempID);

        tempID = checkFolderExist(foldersIDs.get(0), LOT_FOLDER_NAME);
        foldersIDs.add((tempID.isEmpty()) ? createFolder(foldersIDs.get(0), LOT_FOLDER_NAME).getId() : tempID);

        return foldersIDs;
    }

    private File createDiskDriveFile(String googleFolderIdParent, String customFileName, AbstractInputStreamContent uploadStreamContent) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(customFileName);
        fileMetadata.setParents(Collections.singletonList(googleFolderIdParent));

        return driveService
                .files()
                .create(fileMetadata, uploadStreamContent)
                .setFields("*")
                .execute();
    }


    private List<File> getFiles(String folderIdParent) throws IOException {
        final String mimeType = "mimeType != 'application/vnd.google-apps.folder'";
        return getContent(createQuery(mimeType, folderIdParent));
    }

    private List<File> getFolders(String parentFolderID) throws IOException {
        final String mimeType = "mimeType = 'application/vnd.google-apps.folder'";
        return getContent(createQuery(mimeType, parentFolderID));
    }


    // mimeType in format "mimeType = 'application/vnd.google-apps.folder'" or "mimeType != 'application/vnd.google-apps.folder'"
    private String createQuery(String mimeType, String folderIdParent) {
        String query;
        if (folderIdParent == null) {
            query = mimeType + " and 'root' in parents";
        } else {
            query = mimeType + " and '" + folderIdParent + "' in parents";
        }
        return query;
    }


    private List<File> getContent(String query) throws IOException {
        String pageToken = null;
        List<File> list = new ArrayList<>();
        do {
            FileList result = driveService
                    .files()
                    .list()
                    .setQ(query)
                    .setSpaces("drive")
                    .setFields("nextPageToken, files(*)")  //.setFields("nextPageToken, files(id, name, createdTime, size)")//
                    .setPageToken(pageToken).execute();
            list.addAll(result.getFiles());
            pageToken = result.getNextPageToken();
        } while (pageToken != null);
        return list;
    }

    private File createFolder(String parentFolderID, String folderName) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(folderName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");

        if (parentFolderID != null) {
            fileMetadata.setParents(Collections.singletonList(parentFolderID));
        }
        return driveService.files().create(fileMetadata).setFields("*").execute();
    }

    private File createFile(String googleFolderIdParent, String contentType, String customFileName, byte[] uploadData) throws IOException {
        AbstractInputStreamContent uploadStreamContent = new ByteArrayContent(contentType, uploadData);
        return createDiskDriveFile(googleFolderIdParent, customFileName, uploadStreamContent);
    }

    public String getRootFolderID() {
        return RootFolderID;
    }

    public String getAvatarsFolderID() {
        return AvatarsFolderID;
    }

    public String getLotsFolderID() {
        return LotsFolderID;
    }
}
