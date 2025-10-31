package cn.kmdckj.epersonnelarchivegenerator.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Employee {
    private String id;
    private String name;
    private String gender;
    private LocalDate birthDate;
    private String idCard;
    private String nation;
    private String politicalStatus;
    private String maritalStatus;
    private String phone;
    private String email;
    private String nativePlace;
    private String currentAddress;
    private String emergencyContact;
    private String emergencyPhone;
    private String photoBase64; // Base64编码的照片

    private List<WorkExperience> workExperiences;
    private List<Education> educations;
    private List<FamilyMember> familyMembers;

    // 内部类
    @Data
    public static class WorkExperience {
        private LocalDate startDate;
        private LocalDate endDate;
        private String company;
        private String position;
        private String duties;
    }

    @Data
    public static class Education {
        private LocalDate startDate;
        private LocalDate endDate;
        private String school;
        private String major;
        private String degree;
    }

    @Data
    public static class FamilyMember {
        private String relation;
        private String name;
        private Integer age;
        private String workUnit;
    }
}
