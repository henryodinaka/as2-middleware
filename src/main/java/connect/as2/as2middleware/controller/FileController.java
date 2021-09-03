package connect.as2.as2middleware.controller;

import connect.as2.as2middleware.exception.MiddlewareException;
import connect.as2.as2middleware.service.EntryService;
import connect.as2.as2middleware.utils.Response;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/file")
public class FileController {
    private final EntryService entryService;

    public FileController(EntryService entryService) {
        this.entryService = entryService;
    }

    @ApiOperation(value = "Upload file to a any partner. The file name must contain the name of the sender and the receive. eg. MyCompanyName-PartnerName-FileName.txt")
    @PostMapping(value = "/upload/generic",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Response> uploadToAny(@RequestParam(value = "file") MultipartFile multipartFile) {
            Response response = this.entryService.sendToAnyPartner(multipartFile);
            return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @ApiOperation(value = "Upload file to a dedicated partner folder. Requires partner name or the receiver name")
    @PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Response> uploadTo(@RequestParam String partnerName, @RequestParam(value = "file") MultipartFile multipartFile) {
            Response response = this.entryService.sendToOnePartner(partnerName,multipartFile);
            return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @ApiOperation(value = "This endpoint will enable you test for message being sent from the partner. The file sent fromt he client is dropped in the inbox folder")
    @PostMapping(value = "/test/inbox/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Response> uploadToTestInbox(@RequestParam String partnerName, @RequestParam(value = "file") MultipartFile multipartFile) {
            Response response = this.entryService.sendReverse(partnerName,multipartFile);
            return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @ApiOperation(value = "Retrieve all files received from the clients")
    @GetMapping("/inbox" )
    public ResponseEntity<Object> loadInbox(@RequestParam String from,@RequestParam(required = false) String to) throws MiddlewareException {
        var files = this.entryService.loadInbox(from,to);
        return ResponseEntity.status(200).body(files);
    }

    @ApiOperation(value = "Retrieve all message deposition notification sent by our company in .txt files.")
    @GetMapping("/mdn/out" )
    public ResponseEntity<Object> loadMDNOut() {
        var files = this.entryService.loadAllMDNOut();
        return ResponseEntity.status(200).body(files);
    }

    @ApiOperation(value = "Retrieve all message deposition notification sent by clients to our company in .txt files")
    @GetMapping("/mdn/in" )
    public ResponseEntity<Object> loadMDNIn() {
        var files = this.entryService.loadAllMDNIn();
        return ResponseEntity.status(200).body(files);
    }

    @ApiOperation(value = "Retrieve all file sent by our company to clients")
    @GetMapping("/sent" )
    public ResponseEntity<Object> loadSent(@RequestParam String from,@RequestParam(required = false) String to) throws MiddlewareException {
        var files = this.entryService.loadAllSent(from,to);
        return ResponseEntity.status(200).body(files);
    }

}