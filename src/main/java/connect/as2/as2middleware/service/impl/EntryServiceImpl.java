package connect.as2.as2middleware.service.impl;

import connect.as2.as2middleware.config.APIException;
import connect.as2.as2middleware.dto.FileResponseObject;
import connect.as2.as2middleware.exception.MiddlewareException;
import connect.as2.as2middleware.service.EntryService;
import connect.as2.as2middleware.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;

/**
 * Created by Odinaka Onah on 01 Jun, 2021.
 */
@Service
@Slf4j
public class EntryServiceImpl implements EntryService {

    private final FileService fileService;

    @Value("${as2.partners.my-company.id}")
    private String companyId;
    @Value("#{'${partners.id}'.split(',')}")
    private List<String> partnerIds;

    public EntryServiceImpl(FileService fileService) {
        this.fileService = fileService;
    }

    public Response sendToAnyPartner(MultipartFile file) {

        if (file == null)
            throw new APIException(new Response("16", "File to be uploaded cannot be null", null));
        var originalFilename = file.getOriginalFilename();
        var split = originalFilename.split("-");
        if (split.length < 3)
            throw new APIException(new Response("16", "Invalid file name. It must be in this format: MyCompany_ID-Partner_ID-TheEdiFileNameToBeSent.<file extension>", null));
        var companyID = split[0];
        var partnerID = split[1];
        if (!companyId.equals(companyID))
            throw new APIException(new Response("16", "Invalid companyID. It must be: " + companyId, null));

        if (!partnerIds.contains(partnerID))
            throw new APIException(new Response("16", "This partner has not been configured to receive messages, See the configured partner Ids: " + partnerIds, null));

        fileService.save("Any", file);
        return new Response(200, "00", "File Dropped successfully", null);
    }

    public Response sendToOnePartner(String partnerName, MultipartFile file) {

        if (file == null)
            throw new APIException(new Response("16", "File to be uploaded cannot be null", null));
        fileService.save(partnerName, file);
        return new Response(200, "00", "File Dropped successfully", null);
    }

    public Response sendReverse(String partnerName, MultipartFile file) {

        if (file == null)
            throw new APIException(new Response("16", "File to be uploaded cannot be null", null));
        fileService.saveReverse(file);
        return new Response(200, "00", "File Dropped successfully", null);
    }

    public List<File> loadAllMDNOut() {
        return fileService.loadAllMDN(false);
    }

    public List<File> loadAllMDNIn() {
        return fileService.loadAllMDN(true);
    }

    public List<FileResponseObject> loadAllSent(String fileDate, String toDate) throws MiddlewareException {

        Date fro = convertDate(fileDate);
        Date to;
        if (toDate == null || toDate.isEmpty())
            to = new Date();
        else if (Objects.equals(fileDate, toDate)) {
            Calendar c = Calendar.getInstance();
            c.setTime(fro);
            c.add(Calendar.DAY_OF_MONTH, 1);
            to = c.getTime();
        } else {
            to = convertDate(toDate);
            Calendar c = Calendar.getInstance();
            c.setTime(to);
            c.add(Calendar.DAY_OF_MONTH, 1);
            to = c.getTime();
        }
        return fileService.loadAllSent(fro, to);
    }

    public List<FileResponseObject> loadInbox(String fileDate, String toDate) throws MiddlewareException {

        Date fro = convertDate(fileDate);
        Date to;
        if (toDate == null || toDate.isEmpty())
            to = new Date();
        else if (Objects.equals(fileDate, toDate)) {
            Calendar c = Calendar.getInstance();
            c.setTime(fro);
            c.add(Calendar.DAY_OF_MONTH, 1);
            to = c.getTime();
        } else {
            to = convertDate(toDate);
            Calendar c = Calendar.getInstance();
            c.setTime(to);
            c.add(Calendar.DAY_OF_MONTH, 1);
            to = c.getTime();
        }
        return fileService.loadInbox(fro, to);
    }

    public Date convertDate(String fileDate) throws MiddlewareException {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(fileDate);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new MiddlewareException(400, e.getMessage());
        }
    }
}
