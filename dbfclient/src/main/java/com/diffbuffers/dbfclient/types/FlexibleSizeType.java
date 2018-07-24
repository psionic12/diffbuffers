package com.diffbuffers.dbfclient.types;

public interface FlexibleSizeType extends Type {
    default int size() {
        return 0;
    }
}
