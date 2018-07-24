package com.diffbuffers.dbfclient.types;

import java.util.ArrayList;
import java.util.List;

public class NamespaceDef implements Type {
    private NamespaceDef mNamespace;
    private String mName;

    public NamespaceDef(String name, NamespaceDef namespace) {
        this.mNamespace = namespace;
        this.mName = name;
    }

    public String name() {
        return mName;
    }

    public NamespaceDef belongsTo() {
        return mNamespace;
    }

    public List<String> getFullNamespace() {
        List<String> list;
        if (mNamespace == null) {
            list = new ArrayList<>();
        } else {
            list = mNamespace.getFullNamespace();
        }
        list.add(mName);
        return list;
    }
}
