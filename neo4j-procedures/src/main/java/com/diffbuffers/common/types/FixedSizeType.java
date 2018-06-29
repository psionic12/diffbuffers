package com.diffbuffers.common.types;

public interface FixedSizeType extends Type {
    default int size() {
        return 0;
    }
}
