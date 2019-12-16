package com.stc21.boot.auction.service;

import com.google.api.services.drive.model.File;
import com.stc21.boot.auction.entity.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface GoogleDriveService {

    List<Photo> uploadLotMedia(Long lotID, MultipartFile[] file) throws IOException;

    List<Photo> uploadAvatarMedia(Long userID, MultipartFile[] file) throws IOException;

    List<Photo> getUserImage(Long userID) throws IOException;

    List<Photo> getLotImage(Long lotID) throws IOException;

    Boolean deleteFolder(String parentFolderId, String folderName);

    Boolean deleteFile(String parentFolderId, String FolderOrFileName);

    String checkFolderExist(String parentFolderID, String FolderName) throws IOException;

    List<File> getFolderByName(String parentFolderID, String subFolderName) throws IOException;

}
