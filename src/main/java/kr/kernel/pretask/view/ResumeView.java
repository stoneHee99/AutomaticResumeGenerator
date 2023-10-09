package kr.kernel.pretask.view;

import kr.kernel.pretask.model.Career;
import kr.kernel.pretask.model.Education;
import kr.kernel.pretask.model.PersonInfo;

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
        System.out.print("사진 파일명을 입력하세요: ");
        String photo = s.nextLine();

        System.out.print("이름을 입력하세요: ");
        String name = s.nextLine();

        System.out.print("이메일을 입력하세요: ");
        String email = s.nextLine();

        System.out.print("주소를 입력하세요: ");
        String address = s.nextLine();

        System.out.print("전화번호를 입력하세요: ");
        String phoneNumber = s.nextLine();

        System.out.print("생년월일을 입력하세요 (예: 1990-01-01): ");
        String birthDate = s.nextLine();

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

            int graduationYear;
            try {
                graduationYear = Integer.parseInt(tokens[0]);
            } catch (NumberFormatException e) {
                System.out.println("잘못된 입력입니다.");
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
        while ((input = s.nextLine()).trim().length() > 0) {
            sb.append(input).append("\n");
        }
        return sb.toString().trim();
    }
}
