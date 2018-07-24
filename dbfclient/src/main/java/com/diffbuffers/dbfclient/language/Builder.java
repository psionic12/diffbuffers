package com.diffbuffers.dbfclient.language;

import com.diffbuffers.dbfclient.types.CompositeType;
import com.diffbuffers.dbfclient.types.Type;

import java.io.File;

public abstract class Builder {
    protected File mTargetDir;
    protected String mCopyRight = "";

    public Builder(File targetDir) {
        mTargetDir = targetDir;
    }

    public void setCopyRight(String copyRight) {
        if (copyRight!=null) {
            mCopyRight = copyRight;
        }
    }

    public abstract void build(Type type);
}
