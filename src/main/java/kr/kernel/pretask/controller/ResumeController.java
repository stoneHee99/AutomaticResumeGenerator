package kr.kernel.pretask.controller;

import kr.kernel.pretask.model.Career;
import kr.kernel.pretask.model.Education;
import kr.kernel.pretask.model.PersonInfo;
import kr.kernel.pretask.view.ResumeView;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;


public class ResumeController {
    private ResumeView view;
    private Workbook workbook;
    private Sheet resumeSheet;
    private Sheet selfIntroductionSheet;
    private static final double MILLIMETERS_TO_PIXEL = 2.83465;

    public ResumeController() {
        view = new ResumeView();
        workbook = new HSSFWorkbook();
        resumeSheet = workbook.createSheet("이력서");
        selfIntroductionSheet = workbook.createSheet("자기소개서");
    }

    public void createResume() {
        PersonInfo personInfo = view.inputPersonInfo();
        List<Education> educationList = view.inputEducationList();
        List<Career> careerList = view.inputCareerList();
        String selfIntroduction = view.inputSelfIntroduction();

        createResumeSheet(personInfo, educationList, careerList);
        createSelfIntroductionSheet(selfIntroduction);

        saveWorkbookToFile();

        System.out.println("이력서 생성이 완료되었습니다.");
    }

    private void createResumeSheet(PersonInfo personInfo, List<Education> educationList, List<Career> careerList) {
        createPersonInfoHeader();

        Row personInfoDataRow = resumeSheet.createRow(1);
        insertPhotoToResume(personInfo, personInfoDataRow);
        createPersonInfoData(personInfoDataRow, personInfo);

        int educationStartRow = 3;
        createEducationHeader(educationStartRow);

        educationStartRow = createEducationData(educationList, educationStartRow);

        int careerStartRow = educationStartRow + 1;
        createCareerHeader(careerStartRow);

        createCareerData(careerList, careerStartRow);
    }

    private void createPersonInfoHeader() {
        Row personInfoHeaderRow = resumeSheet.createRow(0);
        personInfoHeaderRow.createCell(0).setCellValue("사진");
        personInfoHeaderRow.createCell(1).setCellValue("이름");
        personInfoHeaderRow.createCell(2).setCellValue("이메일");
        personInfoHeaderRow.createCell(3).setCellValue("주소");
        personInfoHeaderRow.createCell(4).setCellValue("전화번호");
        personInfoHeaderRow.createCell(5).setCellValue("생년월일");
    }

    private void insertPhotoToResume(PersonInfo personInfo, Row row) {

        String photoName = personInfo.getPhoto();
        try (InputStream photoStream = new FileInputStream(photoName)) {
            BufferedImage image = ImageIO.read(photoStream);

            int imageWidth = (int) (35 * MILLIMETERS_TO_PIXEL);
            int imageHeight = (int) (45 * MILLIMETERS_TO_PIXEL);
            Image resizedImage = image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
            BufferedImage resizedBufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2d = resizedBufferedImage.createGraphics();
            g2d.drawImage(resizedImage, 0, 0, null);
            g2d.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizedBufferedImage, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            int imageIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);

            Drawing drawing = (Drawing) resumeSheet.createDrawingPatriarch();
            ClientAnchor anchor = new HSSFClientAnchor(0, 0, 0, 0, (short) 0, 1, (short) 1, 2);
            drawing.createPicture(anchor, imageIdx);

            row.setHeightInPoints(imageHeight * 72 / 96);
            int columnWidth = (int) Math.floor(((float) imageWidth / (float) 8) * 256);
            resumeSheet.setColumnWidth(0, columnWidth);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createPersonInfoData(Row r, PersonInfo personInfo) {
        r.createCell(1).setCellValue(personInfo.getName());
        r.createCell(2).setCellValue(personInfo.getEmail());
        r.createCell(3).setCellValue(personInfo.getAddress());
        r.createCell(4).setCellValue(personInfo.getPhoneNumber());
        r.createCell(5).setCellValue(personInfo.getBirthDate());
    }

    private void createEducationHeader(int rowNum) {
        Row educationHeaderRow = resumeSheet.createRow(rowNum - 1);
        educationHeaderRow.createCell(0).setCellValue("졸업년도");
        educationHeaderRow.createCell(1).setCellValue("학교명");
        educationHeaderRow.createCell(2).setCellValue("전공");
        educationHeaderRow.createCell(3).setCellValue("졸업여부");
    }

    private int createEducationData(List<Education> educationList, int rowNum) {
        for (Education education : educationList) {
            Row educationDataRow = resumeSheet.createRow(rowNum++);
            educationDataRow.createCell(0).setCellValue(education.getGraduationYear());
            educationDataRow.createCell(1).setCellValue(education.getSchoolName());
            educationDataRow.createCell(2).setCellValue(education.getMajor());
            educationDataRow.createCell(3).setCellValue(education.getGraduationStatus());
        }
        return rowNum;
    }

    private void createCareerHeader(int rowNum) {
        Row careerHeaderRow = resumeSheet.createRow(rowNum - 1);
        careerHeaderRow.createCell(0).setCellValue("근무기간");
        careerHeaderRow.createCell(1).setCellValue("근무처");
        careerHeaderRow.createCell(2).setCellValue("담당업무");
        careerHeaderRow.createCell(3).setCellValue("근속연수");
    }

    private void createCareerData(List<Career> careerList, int rowNum) {
        for (Career career : careerList) {
            Row careerDataRow = resumeSheet.createRow(rowNum++);
            careerDataRow.createCell(0).setCellValue(career.getWorkPeriod());
            careerDataRow.createCell(1).setCellValue(career.getCompanyName());
            careerDataRow.createCell(2).setCellValue(career.getJobTitle());
            careerDataRow.createCell(3).setCellValue(career.getEmploymentYears());
        }
    }

    private void createSelfIntroductionSheet(String selfIntroduction) {
        Row dataRow = selfIntroductionSheet.createRow(0);
        Cell selfIntroductionCell = dataRow.createCell(0);
        selfIntroductionCell.setCellStyle(getWrapCellStyle());
        selfIntroductionCell.setCellValue(new HSSFRichTextString(selfIntroduction.replaceAll("\n", String.valueOf((char) 10))));
    }

    private HSSFCellStyle getWrapCellStyle() {
        HSSFCellStyle style = (HSSFCellStyle) workbook.createCellStyle();
        style.setWrapText(true);
        return style;
    }

    private void saveWorkbookToFile() {
        try (FileOutputStream fileOut = new FileOutputStream("이력서.xls")) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ResumeController controller = new ResumeController();
        controller.createResume();
    }
}
