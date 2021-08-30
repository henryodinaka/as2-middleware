package connect.as2.as2middleware.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;

/**
 * Created by Odinaka Onah on 30 Aug, 2021.
 */
@Data
@AllArgsConstructor
public class FileResponseObject {
    private String date;
    private File file;
}
