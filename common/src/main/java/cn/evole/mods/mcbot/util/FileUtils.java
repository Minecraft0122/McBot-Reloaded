package cn.evole.mods.mcbot.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @Project: McBot
 * @Author: cnlimiter
 * @CreateTime: 2024/8/11 20:18
 * @Description:
 */
public class FileUtils {
    public static Path checkFolder(Path folder) {
        if (!folder.toFile().isDirectory()) {
            try {
                return Files.createDirectories(folder);
            } catch (IOException e) {
                throw new IllegalStateException("无法创建目录：" + folder, e);
            }
        } else {
            return folder;
        }
    }

    public static Path checkFile(Path file) {
        if (Files.isRegularFile(file)) return file;
        try {
            Path parent = file.getParent();
            if (parent != null) Files.createDirectories(parent);
            return Files.createFile(file);
        } catch (IOException e) {
            throw new IllegalStateException("无法创建文件：" + file, e);
        }
    }

}
