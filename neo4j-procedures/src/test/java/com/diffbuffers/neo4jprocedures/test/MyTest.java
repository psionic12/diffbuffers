package com.diffbuffers.neo4jprocedures.test;

<<<<<<< dbf32005c0226d8a6443290a1c9bc8f9b93d1175
import com.diffbuffers.common.types.ClassDef;
import com.diffbuffers.common.types.NameSpace;
import com.diffbuffers.common.types.Uint32Def;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
=======

import com.diffbuffers.neo4jprocedures.DiffbuffersProcedures;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
>>>>>>> diffbufffers

public class MyTest {
    @Test
    public void test() {
<<<<<<< dbf32005c0226d8a6443290a1c9bc8f9b93d1175
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
=======
        int[] ints = {6,4,0,2,1,5};
        ArrayList<Integer> list = new ArrayList<>();
        for (int i : ints)
        {
            list.add(i);
        }
        System.out.print(DiffbuffersProcedures.getMinMissingInt(list));
>>>>>>> diffbufffers
    }
}
