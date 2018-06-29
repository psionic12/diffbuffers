package com.diffbuffers.common.types;

public interface FlexibleSizeType extends Type {
    default int size() {
        return 0;
    }
}
