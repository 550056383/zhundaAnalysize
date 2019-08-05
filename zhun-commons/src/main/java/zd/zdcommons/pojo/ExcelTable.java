package zd.zdcommons.pojo;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName ExcelTable
 * @Author chenkun
 * @TIME 2019/7/24 0024-9:18
 */
@Data
public class ExcelTable {
    private String sheetName;
    private String[] title;
    //private List<List<String>> resource;
    private List<List<String>> resource;

    @Override
    public String toString() {
        return "ExcelTable{" +
                "sheetName='" + sheetName + '\'' +
                ", title=" + Arrays.toString(title) +
                ", resource=" + resource +
                '}';
    }
}
