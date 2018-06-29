package com.diffbuffers.neo4jprocedures;


import java.util.Map;
import java.util.Objects;

public class Records {
    // Notice: all field name should be the same with the static final string
    public static class ClassRecord {
        public static final String NODE_ID = "nodeId";
        public Long nodeId;
        public static final String NAME = "name";
        public String name;
        public static final String ID = "id";
        public Long id;

        @Override
        public String toString() {
            return "ClassRecord{\n"
                    + "nodeId: " + nodeId + "\n"
                    + "name: " + name + "\n"
                    + "id: " + id + "\n"
                    + "}\n";
        }
    }

    public static class NameSpaceRecord {
        public static final String NODE_ID = "nodeId";
        public Long nodeId;
        public static final String NAME = "name";
        public String name;

        @Override
        public String toString() {
            return "NameSpaceRecord{\n"
                    + "nodeId: " + nodeId + "\n"
                    + "name: " + name + "\n"
                    + "}\n";
        }
    }

    public static class StructRecord {
        public static final String NODE_ID = "nodeId";
        public Long nodeId;
        public static final String NAME = "name";
        public String name;

        @Override
        public String toString() {
            return "StructRecord{\n"
                    + "nodeId: " + nodeId + "\n"
                    + "name: " + name + "\n"
                    + "}\n";
        }
    }

    public static class ArrayRecord {
        public static final String NODE_ID = "nodeId";
        public Long nodeId;

        @Override
        public String toString() {
            return "ArrayRecord{\n"
                    + "nodeId: " + nodeId + "\n"
                    + "}\n";
        }
    }

    public static class FixedArrayRecord {
        public static final String NODE_ID = "nodeId";
        public Long nodeId;
        public static final String COUNTS = "counts";
        public Long counts;
        public static final String TYPE = "type";
        public String type;

        @Override
        public String toString() {
            return "FixedArrayRecord{\n"
                    + "nodeId: " + nodeId + "\n"
                    + "counts: " + counts + "\n"
                    + "type: " + type + "\n"
                    + "}\n";
        }
    }

    public static class BasicTypeRecord {
        public static final String NODE_ID = "nodeId";
        public Long nodeId;

        @Override
        public String toString() {
            return "BasicTypeRecord{\n"
                    + "nodeId: " + nodeId + "\n"
                    + "}\n";
        }
    }
}
