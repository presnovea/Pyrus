package com.pear.pearsample;

import java.io.File;
import java.util.UUID;

/**
 * Data implementation of each local directory file
 */
public class LocalFile {

    private UUID mId;
    private String mName;
    private String mCopiedFrom;

    public LocalFile(File inputFile) {
        mId = UUID.randomUUID();
        mName = inputFile.getName();
        mCopiedFrom = inputFile.getAbsolutePath();
    }

    public UUID getId(){return mId;}

    public String getName() {
        return mName;
    }

    public String getCopiedFrom() {
        return mCopiedFrom;
    }
}
