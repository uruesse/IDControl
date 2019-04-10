/*
 * Copyright 2019 Ulrich Rüße <ulrich@ruesse.net>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ruesse.idc.control;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ulrich Rüße <ulrich@ruesse.net>
 */
public class FileService {

    private static final Logger LOGGER = Logger.getLogger(FileService.class.getName());

    public static Path getWorkingDir() {
        Path AppPath;
        String strAppPath = System.getenv("IDC_HOME");
        LOGGER.log(Level.INFO, "IDC_HOME={0}", strAppPath);

        if (strAppPath != null) {
            AppPath = Paths.get(strAppPath, "var");
        } else {
            AppPath = Paths.get("/Users/ulrich/IDControl/var");
        }
        LOGGER.log(Level.INFO, "WorkingDir={0}", AppPath);
        return AppPath;
    }

    public static Path getDatabaseDir() {
        return getWorkingDir().resolve("DB");
    }

    public static Path getExportsDir() {
        return getWorkingDir().resolve("Exports");
    }

    public static String getCurrentLocalDateTimeStamp() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    public static String getExpFileName() {
        VereinService vs= new VereinService();
        
        return vs.getExpFileName();
    }

    public static Path createDatedExportsDir() {
        Path exp = getWorkingDir().resolve("Exports");
        exp = exp.resolve(getExpFileName());
        if (Files.notExists(exp)) {
            try {
                Files.createDirectory(exp);
            } catch (IOException ex) {
                Logger.getLogger(FileService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return exp;
    }

    public static void deleteDirectoryStream(Path path) {
        try {
            Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public static Path getSewobeExportDir() {
        return getExportsDir().resolve("Sewobe");
    }

    public static Path getLogoDir() {
        return getWorkingDir().resolve("Logo");
    }

    public static Path getReportTemplatesDir() {
        return getWorkingDir().resolve("Reporttemplates");
    }

    public static Path getReportsDir() {
        return getWorkingDir().resolve("Reports");
    }

    public static Path getTempDir() {
        return getWorkingDir().resolve("Tmp");
    }

}
