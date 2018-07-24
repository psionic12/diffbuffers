package com.diffbuffers.dbfclient.types;

public interface FixedSizeType extends Type {
    default int size() {
        return 0;
    }
}
