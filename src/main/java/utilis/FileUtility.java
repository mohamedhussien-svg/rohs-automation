package utilis;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtility {
    public static void clearDir(String dir) {
        try {
            File file = new File(dir);
            if (!file.exists()) {
                FileUtils.createParentDirectories(file);
                file.mkdir();
            }
            FileUtils.cleanDirectory(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeFileRow(File file, String[] cells) {
        try {
            FileUtils.write(file, String.join("\t", cells) + "\n", StandardCharsets.UTF_8, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> readFile(String dir) {
        try {
            return FileUtils.readLines(new File(dir), StandardCharsets.UTF_8).stream().skip(1).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static String lastFileCreated(String dir) {
        return FileUtils.listFiles(new File(dir), null, false)
                .stream()
                .filter(File::isFile)
                .sorted((x, y) -> Long.compare(y.lastModified(), x.lastModified()))
                .map(File::getAbsolutePath)
                .collect(Collectors.toList()).stream().findFirst().orElse("");
    }
}
