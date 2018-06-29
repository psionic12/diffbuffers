package com.diffbuffers.neo4jprocedures.test;

import com.diffbuffers.common.types.ClassDef;
import com.diffbuffers.common.types.NameSpace;
import com.diffbuffers.common.types.Uint32Def;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class MyTest {
    @Test
    public void test() {
        NameSpace nameSpace = new NameSpace("testNameSpace");
        ClassDef classDef = new ClassDef("TestClass", nameSpace);
        classDef.addField(new Uint32Def(), "uint32field", 33);
        try {
            FileOutputStream fileOut =
                    new FileOutputStream("/tmp/employee.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(classDef);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
