package com.diffbuffers.dbfclient.language;

import com.diffbuffers.dbfclient.types.*;

import java.io.File;
import java.util.HashSet;
import java.util.List;

public class CppBuilder extends Builder {
    public CppBuilder(File targetDir) {
        super(targetDir);
    }

    @Override
    public void build(Type type) {
        if (type instanceof NamespaceDef) {
            return;
        }
        buildHeaderFile(type);
        buildSourceFile(type);
    }

    private void buildHeaderFile(CompositeType compositeType) {
        String s = "";
        if (!mCopyRight.isEmpty()) {
            s += "/**" + mCopyRight + "**/" + "\n";
        }
        s += "#ifndef " + CamelCaseToSeparatedCase(compositeType.name()) + "H_" + "\n";
        s += "#define " + CamelCaseToSeparatedCase(compositeType.name()) + "H_" + "\n";
        s += getIncludes(compositeType);
        if (compositeType.namespace() != null) {
            for (String namespace : compositeType.namespace().getFullNamespace()) {
                s += "namespace " + namespace + " {" + "\n";
            }
        }
        s += getCompositeContent(compositeType);
        if (compositeType.namespace() != null) {
            List list = compositeType.namespace().getFullNamespace();
            for (int i = list.size() - 1; i >= 0; i--) {
                s += "} // namespace " + list.get(i) + "\n";
            }
        }
        s += "#endif // " + CamelCaseToSeparatedCase(compositeType.name()) + "H_" + "\n";
    }

    private void buildSourceFile(CompositeType compositeType) {

    }

    private String CamelCaseToSeparatedCase(String s) {
        String s1 = "";
        for (String w : s.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
            s1 += w.toUpperCase() + '_';
        }
        return s1;
    }

    private String getIncludes(CompositeType compositeType) {
        String includes = "";
        HashSet<String> includeFiles = new HashSet<>();
        for (Identifier identifier : compositeType.fields()) {
            if (identifier.type() instanceof BasicType) {
                includeFiles.add("<cstdint>");
            } else if (identifier.type() instanceof ArrayDef) {

            } else if (identifier.type() instanceof CompositeType) {
                includeFiles.add("<" + ((CompositeType) identifier.type()).name().toLowerCase() + ".h>");
            }
        }
        for (String includeFile : includeFiles) {
            includes += "#include " + includeFile + "\n";
        }
        return includes;
    }

    private String getCompositeContent(CompositeType compositeType) {
        String content = "";
        if (compositeType instanceof ClassDef) {
            ClassDef classDef = (ClassDef) compositeType;
            content += "class " + classDef.name();
            if (classDef.parent() != null) {
                content += " : virtual public " + getFullName(classDef.parent());
            }
            content += " {" + "\n";
            content += "public:" + "\n";

            content += "protected:" + "\n";

            content += "private:" + "\n";

            content += "}" + "\n";
        } else if (compositeType instanceof StructDef) {

        } else if (compositeType instanceof EnumDef) {

        }
        return content;
    }

    private String getFullName(CompositeType compositeType) {
        String fullName = "";
        for (String namespace : compositeType.namespace().getFullNamespace()) {
            fullName += namespace + "::";
        }
        fullName += compositeType.name();
        return fullName;
    }
}
