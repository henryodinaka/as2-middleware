package connect.as2.as2middleware.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import connect.as2.as2middleware.config.APIException;
import connect.as2.as2middleware.dto.FileResponseObject;
import connect.as2.as2middleware.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class FileService {

    @Value("#{'${partners.name}'.split(',')}")
    private List<String> partnerNames;
    @Value("${path.storageBaseDir}")
    private String baseDir;

    @Value("${path.reverse.test}")
    private String reverseTestPath;

    @Value("${path.mdn.out}")
    private String mdnOutBasePath;
    @Value("${path.mdn.in}")
    private String mdnInBasePath;
    @Value("${path.sent}")
    private String sentBasePath;
    @Value("${path.inbox}")
    private String inboxPath;

    @PostConstruct
    public void init() {
        for (String name : partnerNames) {
            String path = baseDir + "/to" + name;
            try {
                Files.createDirectories(Paths.get(path));
                log.info("created folder {}",path);
            } catch (IOException e) {
                throw new RuntimeException("Could not create partners folder!");
            }
        }
    }

    public void save(String partnerName, MultipartFile file) {
        var pathToAnyPartner = generatePartnerPath(partnerName);
        try {
            Path root = Paths.get(pathToAnyPartner);
            if (!Files.exists(root)) {
                init();
            }
            Files.copy(file.getInputStream(), root.resolve(Objects.requireNonNull(file.getOriginalFilename())));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public void saveReverse(MultipartFile file) {
        try {
            Path root = Paths.get(reverseTestPath);
            if (!Files.exists(root)) {
                Files.createDirectories(Paths.get(reverseTestPath));
            }
            Files.copy(file.getInputStream(), root.resolve(Objects.requireNonNull(file.getOriginalFilename())));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public Resource loadFileByName(String filename) {
        var pathToAnyPartner = "";
        try {
            Path file = Paths.get(pathToAnyPartner)
                    .resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public String generatePartnerPath(String partnerName) {
        String pathToAnyPartner = null;
        for (String name : partnerNames) {
            if (name.equals(partnerName)) {
                pathToAnyPartner = baseDir + "/to" + name;
                break;
            }
        }
        if (pathToAnyPartner == null)
            throw new APIException(new Response(400, "16", "Partner " + partnerName + " is not configure to receiver message yet"));
        return pathToAnyPartner;
    }

    public void deleteAll(String folderName) {
        FileSystemUtils.deleteRecursively(Paths.get(folderName)
                .toFile());
    }

    public void deleteFile(String fileName) {
        String pathToAnyPartner = "";
        FileSystemUtils.deleteRecursively(Paths.get(pathToAnyPartner)
                .toFile());
    }

    public List<File> loadAllMDN(boolean in) {
        try {
            String mdnPath = "";
            if (in)
                   mdnPath = mdnInBasePath + "/" + LocalDate.now();
            else mdnPath = mdnOutBasePath + "/" + LocalDate.now();
//            log.info("Mdn path {}", mdnPath);
            Path root = Paths.get(mdnPath);
            if (Files.exists(root)) {
                var fileList = Files.walk(root, 1)
                        .filter(path -> !path.equals(root)).map(Path::toFile)
                        .collect(Collectors.toList());
//              todo  for (File file : fileList) {
//                    log.info("MDN File Received {}", file.getName());
//                }
                List<FileResponseObject> files = new ArrayList<>();
//                for (File file : fileList){
//                    var name = file.getName();
//                    log.info("File name {}",name);
//                    var split = name.split("-");
//                    if (split.length <1) continue;
//
//                    String yyyy = split[1];
//                    String MM = split[2];
//                    String dTime = split[3];
//                    log.info("DateTime log {}",dTime);
//                    String dd = dTime.substring(0,2);
//                    String HH = dTime.substring(2,4);
//                    String mm = dTime.substring(4,6);
//                    String ss = dTime.substring(6,8);
//
//                    String dateTime = yyyy+"-"+MM+"-"+dd+" "+HH+":"+mm+":"+ss;
//                    log.info("Name splits yyyy {} MM {} dd {} HH {} mm {} ss {}",yyyy,MM,dd,HH,mm,ss);
//                    log.info("Final date and time {}",dateTime);
//                    files.add(new FileResponseObject(dateTime,file));
//                }
//                return files;
                return fileList;
            }

            return Collections.emptyList();
        } catch (IOException e) {
            throw new RuntimeException("Could not list the files!");
        }
    }

    public List<File> loadInbox() {
        try {
//            log.info("inbox path {}", inboxPath);
            Path root = Paths.get(inboxPath);
            if (Files.exists(root)) {
                var fileList = Files.walk(root, 1)
                        .filter(path -> !path.equals(root)).map(Path::toFile)
                        .collect(Collectors.toList());
//        todo        for (File file : fileList) {
//                    log.info("Inbox File Received {}", file.getName());
//                }
//                List<FileResponseObject> files = new ArrayList<>();
//                for (File file : fileList){
//                    var name = file.getName();
//                    log.info("File name {}",name);
//                    var split = name.split("-");
//                    if (split.length <1) continue;
//
//                    String yyyy = split[1];
//                    String MM = split[2];
//                    String dTime = split[3];
//                    log.info("DateTime log {}",dTime);
//                    String dd = dTime.substring(0,2);
//                    String HH = dTime.substring(2,4);
//                    String mm = dTime.substring(4,6);
//                    String ss = dTime.substring(6,8);
//
//                    String dateTime = yyyy+"-"+MM+"-"+dd+" "+HH+":"+mm+":"+ss;
//                    log.info("Name splits yyyy {} MM {} dd {} HH {} mm {} ss {}",yyyy,MM,dd,HH,mm,ss);
//                    log.info("Final date and time {}",dateTime);
//                    files.add(new FileResponseObject(dateTime,file));
//                }
//                return files;
                return fileList;
            }
            return Collections.emptyList();
        } catch (IOException e) {
            throw new RuntimeException("Could not list the files!");
        }
    }


    public List<File> loadAllSent() {

            String sentPath = sentBasePath + "/" + LocalDate.now().getYear();
//            log.info("Sent path {}", sentPath);

        var fileList = listRecursive(sentPath, new ArrayList<>());
//        List<FileResponseObject> files = new ArrayList<>();
//        for (File file : fileList){
//            var name = file.getName();
//            log.info("File name {}",name);
//            var split = name.split("-");
//            if (split.length <1) continue;
//
//            String yyyy = split[1];
//            String MM = split[2];
//            String dTime = split[3];
//            log.info("DateTime log {}",dTime);
//            String dd = dTime.substring(0,2);
//            String HH = dTime.substring(2,4);
//            String mm = dTime.substring(4,6);
//            String ss = dTime.substring(6,8);
//
//            String dateTime = yyyy+"-"+MM+"-"+dd+" "+HH+":"+mm+":"+ss;
//            log.info("Name splits yyyy {} MM {} dd {} HH {} mm {} ss {}",yyyy,MM,dd,HH,mm,ss);
//            log.info("Final date and time {}",dateTime);
//            files.add(new FileResponseObject(dateTime,file));
//        }
//        return files;
        return fileList;
    }

    public List<File> listRecursive(String directoryName, List<File> files) {
        File directory = new File(directoryName);

        // Get all files from a directory.
        File[] fList = directory.listFiles();
        if(fList != null)
            for (File file : fList) {
                if (file.isFile()) {
                    files.add(file);
                } else if (file.isDirectory()) {
                    listRecursive(file.getAbsolutePath(), files);
                }
            }
        return files;
    }
}