package com.diffbuffers.common.types;

public class FixedArray extends ArrayType implements FixedSizeType {
    private int counts;
    private FixedSizeType element;

    public FixedArray(int counts, FixedSizeType element) {
        this.counts = counts;
        this.element = element;
    }

    @Override
    public int size() {
        return counts * element.size();
    }
}
