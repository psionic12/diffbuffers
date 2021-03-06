package com.diffbuffers.common;

public class Records {
    // Notice: all field name should be the same with the static final string
    public static class CompositeRecord {
        public static final String NODE_ID = "nodeId";
        public Long nodeId;
        public static final String NAME = "name";
        public String name;
        public static final String ID = "id";
        public Long id;
    }

    public static class NameSpaceRecord {
        public static final String NODE_ID = "nodeId";
        public Long nodeId;
        public static final String NAME = "name";
        public String name;
    }

    public static class FieldRelationship {
        public static final String FIELD_ID = "fieldId";
        public Long fieldId;
        public static final String COUNTS = "counts";
        public Long counts;
        public static final String NAME = "name";
        public String name;
    }
}
