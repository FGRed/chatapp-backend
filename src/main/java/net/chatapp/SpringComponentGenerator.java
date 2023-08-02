package net.chatapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class SpringComponentGenerator {

    public static void main(String... args) {
        createBundle("Settings");
    }

    public static String createBundle(String className) {

        String packageStr = "package net.chatapp.model." + className.toLowerCase();

        String entityClass = packageStr + ";\n" +
                "\n" +
                "import lombok.Getter;\n" +
                "import lombok.Setter;\n" +
                "import javax.persistence.*;\n" +
                "\n" +
                "@Entity\n" +
                "@Getter\n" +
                "@Setter\n" +
                "public class " + className + "{\n" +
                "   @GeneratedValue(strategy = GenerationType.AUTO)\n" +
                "   @Id\n" +
                "   private Long id;\n"
                + "\n"
                + "}";

        printFile("Model", className, entityClass);

        packageStr = "net.chatapp.repository." + className.toLowerCase();

        String repositoryClass = "package " + packageStr + ";\n" +
                "import org.springframework.data.jpa.repository.JpaRepository;\n" +
                "import " + packageStr.replace("repository", "model") + "." + className + ";\n" +
                "import org.springframework.stereotype.Repository;\n" +
                "\n" +
                "@Repository\n" +
                "public interface " + className + "Repository extends JpaRepository<" + className + ", Long> {\n" +
                "}";

        printFile("Repository", className, repositoryClass);

        packageStr = "net.chatapp.service." + className.toLowerCase();

        String serviceClass = "package " + packageStr + ";\n" +
                "import org.springframework.stereotype.Service;\n" +
                "\n" +
                "@Service\n" +
                "public class " + className + "Service {\n" +
                "}";

        printFile("Service", className, serviceClass);

        packageStr = "net.himo.himonetapiv1.restcontroller." + className.toLowerCase();

        String stringBuilder = "package " + packageStr + ";" +
                "\n" +
                "\n" +
                "import org.springframework.web.bind.annotation.*;" +
                "\n" +
                "\n" +
                "@RestController" +
                "\n" +
                "@RequestMapping(\"/" + className.toLowerCase() + "\")" +
                "\n" +
                "public class " + className + "RestController {" +
                "\n" +
                "}";

        printFile("RestController", className, stringBuilder);
        return "";

    }


    public static void printFile(String type, String className, String clazz) {
        String modelFolder = System.getProperty("user.dir") + "\\src\\main\\java\\net\\chatapp\\" + type.toLowerCase() + "\\" + className.toLowerCase() + "\\";
        File file = new File(modelFolder);
        file.mkdirs();
        System.out.println("creating path: " + file.getAbsolutePath());
        String fileStr = "";
        if (!type.equals("Model")) {
            fileStr = modelFolder + className + type + ".java";
        } else {
            fileStr = modelFolder + className + ".java";
        }

        System.out.println("Creating class file " + fileStr);
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(fileStr);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        printWriter.print(clazz);
        printWriter.close();
        String createdClass = "";
        if (type.equals("Model")) {
            createdClass = className + ".java";
        } else {
            createdClass = className + type + ".java";
        }

        System.out.println("Created " + createdClass);
    }


}

