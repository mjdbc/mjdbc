package mini.jdbc;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import javax.activation.DataSource;

/**
 *
 */
public class DbFactory implements Opcodes {

    public static final Loader LOADER = new Loader();


    protected static void generateBasicClass(ClassVisitor cv, String className, String superClassName) {
        cv.visit(V1_1, ACC_PUBLIC, className.replace('.', '/'), null, superClassName.replace('.', '/'), null);

        // add default constructor
        MethodVisitor mv = cv.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        if (mv != null) {
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, superClassName.replace('.', '/'), "<init>", "()V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }

        // add field to store context
        String fieldName = "context";
        String fieldType = "Lmini/jdbc/DbOpContext;";
        FieldVisitor fv = cv.visitField(Opcodes.ACC_PUBLIC, fieldName, fieldType, null, null);
        if (fv != null) {
            fv.visitEnd();
        }

        // add context getter method
        mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "getContext", "()" + fieldType, null, null);
        if (mv != null) {
            mv.visitCode();
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, className.replace('.', '/'), fieldName, fieldType);
            mv.visitInsn(Type.getType(fieldType).getOpcode(Opcodes.IRETURN));
            mv.visitMaxs(3, 1);
            mv.visitEnd();
        }

        cv.visitEnd();
    }

    public static DbInterface wrap(@NotNull Class dbClass, DataSource source) {
        try {
            String resultClassName = "mini.jdbc.Wrapper"; //todo:
            ClassWriter cw = new ClassWriter(0);
            generateBasicClass(cw, resultClassName, dbClass.getName());
            return (DbInterface) LOADER.defineClass(resultClassName, cw.toByteArray()).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static class Loader extends ClassLoader {
        public Class<?> defineClass(String name, byte[] b) {
            return defineClass(name, b, 0, b.length);
        }
    }
}