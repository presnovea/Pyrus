package com.pear.pearsample;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implements List of local files
 */
public class LocalFilesList {
    private static LocalFilesList mFilesList;
    private List<LocalFile> mLocalFileList;

    private LocalFilesList(File[] listOfLocalFiles)
    {
        mLocalFileList = new ArrayList<>();

        for (File file: listOfLocalFiles) {
            mLocalFileList.add(new LocalFile(file));
        }
    }

    public static LocalFilesList update(File[] listOfLocalFiles) {
               mFilesList = new LocalFilesList(listOfLocalFiles);
        return mFilesList;
    }

    public List getLocalFiles()
    {
        return mLocalFileList;
    }

    public LocalFile getLocalFile(UUID id){
        for (LocalFile file: mLocalFileList) {
            if (file.getId().equals(id))
                return file;
        }
        return null;
    }


}


