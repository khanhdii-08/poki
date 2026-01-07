package com.remake.poki.i18n;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.regex.Pattern;

public class I18nKeysGenerator {
    public static void main(String[] args) throws IOException {
        String pathInput = "src/main/resources/i18n/messages_en.properties";
        String pathOutput = "src/main/java/";

        InputStream inputStream = new FileInputStream(pathInput);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        Class<?> targetClazz = I18nKeys.class;
        File fileOutput = new File(pathOutput + targetClazz.getName().replaceAll(Pattern.quote("."), "/") + ".java");

        System.out.println("write " + fileOutput.getAbsolutePath());
        PrintWriter pw = new PrintWriter(FileUtils.openOutputStream(fileOutput));

        try {
            pw.println("package " + targetClazz.getPackage().getName() + ";");
            pw.println("/**");
            pw.println(" * I18nKeys");
            pw.println(" */");
            pw.println("public class " + targetClazz.getSimpleName() + " {");

            String line;
            while ((line = br.readLine()) != null) {
                String[] vals = line.split("=", 2);
                if (vals.length > 1) {
                    String key = vals[0].trim();
                    String value = vals[1].trim();
                    pw.println("    /** " + key + "=" + value + " */");
                    pw.println("    public static final String "
                            + key.toUpperCase().replaceAll(Pattern.quote("."),
                            "_").replaceAll(Pattern.quote("-"), "_")
                            + " = \"" + key + "\";\n");
                }
            }
            pw.println("}");
            pw.flush();
        } finally {
            IOUtils.closeQuietly(br);
            IOUtils.closeQuietly(pw);
        }
    }
}