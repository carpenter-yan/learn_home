package mock.powermock;

import java.io.File;

public class ClassUnderTest {

    public boolean callArgumentInstance(File file) {
        return file.exists();
    }

    public boolean callInternalInstance(String path) {
        File file = new File(path);
        return file.exists();
    }

    public boolean callFinalMethod(ClassDependency refer) {
        return refer.isAlive();
    }

    public boolean callStaticMethod() {
        return ClassDependency.isExist();
    }

    public String callJDKStaticMethod(String str) {
        return System.getProperty(str);
    }

    public boolean callPrivateMethod() {
        return isExist();
    }

    private boolean isExist() {
        return false;
    }
}
