package kr.kernel.pretask.view;

import kr.kernel.pretask.controller.InputValidationController;
import kr.kernel.pretask.model.Career;
import kr.kernel.pretask.model.Education;
import kr.kernel.pretask.model.PersonInfo;
import kr.kernel.pretask.utility.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ResumeView {
    private Scanner s;
    public static final String QUIT_SIGN = "q";

    public ResumeView() {
        s = new Scanner(System.in);
    }

    public PersonInfo inputPersonInfo() {
        String photo = "", name = "", email = "", address = "", phoneNumber = "", birthDate = "";
        boolean photoFlag = true, nameFlag = true, emailFlag = true, addressFlag = true, phoneFlag = true, birthFlag = true;

        while (photoFlag) {
            System.out.print("사진 파일명을 입력하세요: ");
            photo = s.nextLine();
            photoFlag = !InputValidationController.isValidPhoto(photo);
            if (photoFlag) System.out.println("사진 파일이 프로젝트에 존재하지 않습니다.");
        }

        while (nameFlag) {
            System.out.print("이름을 입력하세요: ");
            name = s.nextLine();
            nameFlag = name.isEmpty();
            if (nameFlag) System.out.println("이름은 빈 값일 수 없습니다.");
        }

        while (emailFlag) {
            System.out.print("이메일을 입력하세요: ");
            email = s.nextLine();
            emailFlag = !email.matches("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
            if (emailFlag) System.out.println("이메일 형식이 올바르지 않습니다.");
        }

        while (addressFlag) {
            System.out.print("주소를 입력하세요: ");
            address = s.nextLine();
            addressFlag = address.isEmpty();
            if (addressFlag) System.out.println("주소는 빈 값일 수 없습니다.");
        }

        while (phoneFlag) {
            System.out.print("전화번호를 입력하세요: ");
            phoneNumber = s.nextLine();
            phoneFlag = !phoneNumber.matches("^\\d{3}-\\d{3,4}-\\d{4}$");
            if (phoneFlag) System.out.println("전화번호 형식이 올바르지 않습니다.");
        }

        while (birthFlag) {
            System.out.print("생년월일을 입력하세요 (예: 1990-01-01): ");
            birthDate = s.nextLine();
            birthFlag = !InputValidationController.isValidDate(birthDate);
            if (birthFlag) System.out.println("생년월일 형식이 올바르지 않습니다.");
        }

        return new PersonInfo(photo, name, email, address, phoneNumber, birthDate);
    }

    public List<Education> inputEducationList() {
        List<Education> educationList = new ArrayList<>();

        while (true) {
            System.out.println("학력 정보를 입력하세요 (종료는 " + QUIT_SIGN + "):");
            System.out.println("졸업년도 학교명 전공 졸업여부");

            String input = s.nextLine();
            if (input.equalsIgnoreCase(QUIT_SIGN)) break;

            String[] tokens = input.split(" ");
            if (tokens.length != 4) {
                System.out.println("잘못된 입력입니다.");
                continue;
            }

            if (!InputValidationController.isValidTokens(tokens)) {
                System.out.println("잘못된 입력입니다.");
                continue;
            }

            int graduationYear;
            try {
                graduationYear = Integer.parseInt(tokens[0]);
            } catch (NumberFormatException e) {
                LogUtil.printError("졸업년도 파싱 중 오류가 발생했습니다", e);
                continue;
            }

            String schoolName = tokens[1];
            String major = tokens[2];
            String graduationStatus = tokens[3];

            educationList.add(new Education(graduationYear, schoolName, major, graduationStatus));
        }

        return educationList;
    }

    public List<Career> inputCareerList() {
        List<Career> careerList = new ArrayList<>();

        while (true) {
            System.out.println("경력 정보를 입력하세요 (종료는 " + QUIT_SIGN + "):");
            System.out.println("근무기간 근무처 담당업무 근속연수");

            String input = s.nextLine();
            if (input.equalsIgnoreCase(QUIT_SIGN)) break;

            String[] tokens = input.split(" ");
            if (tokens.length != 4) {
                System.out.println("잘못된 입력입니다.");
                continue;
            }

            if (!InputValidationController.isValidTokens(tokens)) {
                System.out.println("잘못된 입력입니다.");
                continue;
            }

            String workPeriod = tokens[0];
            String companyName = tokens[1];
            String jobTitle = tokens[2];
            String employmentYears = tokens[3];

            careerList.add(new Career(workPeriod, companyName, jobTitle, employmentYears));
        }

        return careerList;
    }

    public String inputSelfIntroduction() {
        System.out.println("자기소개서를 입력하세요. 여러 줄을 입력하려면 빈 줄을 입력하세요.");
        StringBuilder sb = new StringBuilder();
        String input;
        while (!(input = s.nextLine()).trim().isEmpty()) {
            sb.append(input).append("\n");
        }
        return sb.toString().trim();
    }
}
