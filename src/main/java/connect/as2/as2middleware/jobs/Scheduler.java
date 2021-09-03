package connect.as2.as2middleware.jobs;

import connect.as2.as2middleware.service.impl.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class Scheduler {

    private FileService fileService;

    @Autowired
    public Scheduler(FileService fileService) {
        this.fileService = fileService;

    }

    @Scheduled(cron = "0 0/1 * 1/1 * ?")
    public void loadMDNOut() {
        var files = fileService.loadAllMDN(false);
    }
    @Scheduled(cron = "0 0/1 * 1/1 * ?")
    public void loadMDNIn() {
        var files = fileService.loadAllMDN(true);
    }
    @Scheduled(cron = "0 0/1 * 1/1 * ?")
    public void loadInbox() {

        var files = fileService.loadInbox(new Date(),new Date());
    }

}
