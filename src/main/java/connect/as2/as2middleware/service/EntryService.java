package connect.as2.as2middleware.service;

import connect.as2.as2middleware.dto.FileResponseObject;
import connect.as2.as2middleware.utils.Response;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by Odinaka Onah on 01 Jun, 2021.
 */
public interface EntryService {
    Response sendToAnyPartner(MultipartFile multipartFile);
    Response sendToOnePartner(String partnerName,MultipartFile multipartFile);
    Response sendReverse(String partnerName,MultipartFile multipartFile);
    List<FileResponseObject> loadAllMDNOut();
    List<FileResponseObject> loadAllMDNIn();
    List<FileResponseObject> loadAllSent();
    List<FileResponseObject> loadInbox();
}
