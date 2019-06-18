package zd.zdanalysis.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import zd.zdcommons.Utils;

import java.util.List;

@Service
public class EnginnerService {
    public List<String> getMessage(MultipartFile file){
        Utils utils = new Utils();
        List<String> title = utils.getLotTitle(file);
        return title;
    }
}
