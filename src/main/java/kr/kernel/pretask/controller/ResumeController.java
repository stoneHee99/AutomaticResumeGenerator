package kr.kernel.pretask.controller;

import kr.kernel.pretask.view.ResumeView;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;


public class ResumeController {
    private ResumeView view;
    private Workbook workbook;

    public ResumeController() {
        view = new ResumeView();
        workbook = new HSSFWorkbook();
    }
}
