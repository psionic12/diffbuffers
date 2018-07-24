package com.diffbuffers.dbfclient.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CompositeType implements FieldType {
    private NamespaceDef mNameSpace;
    private String mName;

    private List<Identifier> mFields =
            new ArrayList<>();

    private Map<String, Object> mInitList = new HashMap<>();

    public CompositeType(String name, NamespaceDef namespace, List<Identifier> fields) {
        mName = name;
        mNameSpace = namespace;
        if (mFields != null) {
            mFields.addAll(fields);
        }
    }

    public void setInitList(Map<String, Object> initList) {
        for (String key : initList.keySet()) {
            mInitList.put(key, initList.get(key));
        }
    }

    public NamespaceDef namespace() {
        return mNameSpace;
    }

    public String name() {
        return mName;
    }

    public List<Identifier> fields() {
        return mFields;
    }

    public int count() {
        return mFields.size();
    }
}
