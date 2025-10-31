package cn.kmdckj.epersonnelarchivegenerator.engine;

import cn.kmdckj.epersonnelarchivegenerator.engine.config.FieldConfigRegistry;
import cn.kmdckj.epersonnelarchivegenerator.engine.config.FieldFilter;
import cn.kmdckj.epersonnelarchivegenerator.engine.layout.*;
import cn.kmdckj.epersonnelarchivegenerator.model.Employee;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 排版引擎 - 核心布局计算类
 */
@Component
public class LayoutEngine {

    private final FieldConfigRegistry configRegistry;
    private final FieldFilter fieldFilter;

    // 布局常量
    private static final double ROW_WIDTH = 100.0;  // 行宽百分比
    private static final double MIN_FIELD_WIDTH = 20.0;  // 最小字段宽度

    public LayoutEngine(FieldConfigRegistry configRegistry, FieldFilter fieldFilter) {
        this.configRegistry = configRegistry;
        this.fieldFilter = fieldFilter;
    }

    /**
     * 计算完整的档案布局
     *
     * @param employee 员工数据
     * @return 布局模型
     */
    public LayoutModel calculateLayout(Employee employee) {
        LayoutModel model = new LayoutModel();

        // 1. 基础信息区:基础信息 + 照片
        LayoutZone basicInfoZone = createBasicInfoZone(employee);
        model.addZone(basicInfoZone);

        // 2. 工作经历区
        if (employee.getWorkExperiences() != null && !employee.getWorkExperiences().isEmpty()) {
            LayoutZone workZone = createWorkExperienceZone(employee.getWorkExperiences());
            model.addZone(workZone);
        }

        // 3. 教育背景区
        if (employee.getEducations() != null && !employee.getEducations().isEmpty()) {
            LayoutZone eduZone = createEducationZone(employee.getEducations());
            model.addZone(eduZone);
        }

        // 4. 家庭成员区
        if (employee.getFamilyMembers() != null && !employee.getFamilyMembers().isEmpty()) {
            LayoutZone familyZone = createFamilyZone(employee.getFamilyMembers());
            model.addZone(familyZone);
        }

        return model;
    }

    /**
     * 创建基础信息区(基础信息 + 照片)
     */
    private LayoutZone createBasicInfoZone(Employee employee) {
        LayoutZone zone = new LayoutZone("basicInfo", LayoutZone.ZoneType.BASIC_INFO_WITH_PHOTO);

        // 设置照片元数据
        if (employee.getPhotoBase64() != null && !employee.getPhotoBase64().isEmpty()) {
            ZoneMetadata.PhotoMetadata photoMeta = new ZoneMetadata.PhotoMetadata(employee.getPhotoBase64());
            zone.setMetadata(photoMeta);
        }

        // 准备基础信息字段数据
        Map<String, String> fieldData = new LinkedHashMap<>();
        fieldData.put("name", employee.getName());
        fieldData.put("gender", employee.getGender());
        fieldData.put("birthDate", formatDate(employee.getBirthDate()));
        fieldData.put("nation", employee.getNation());
        fieldData.put("idCard", employee.getIdCard());
        fieldData.put("politicalStatus", employee.getPoliticalStatus());
        fieldData.put("maritalStatus", employee.getMaritalStatus());
        fieldData.put("phone", employee.getPhone());
        fieldData.put("email", employee.getEmail());
        fieldData.put("nativePlace", employee.getNativePlace());
        fieldData.put("currentAddress", employee.getCurrentAddress());
        fieldData.put("emergencyContact", employee.getEmergencyContact());
        fieldData.put("emergencyPhone", employee.getEmergencyPhone());

        // 过滤并转换为LayoutField
        List<LayoutField> fields = fieldFilter.filterAndConvertOrdered(
                fieldData,
                FieldConfigRegistry.FieldGroup.BASIC_INFO
        );

        // 贪心填充算法
        fillRowsGreedy(zone, fields);

        return zone;
    }

    /**
     * 创建工作经历区
     */
    private LayoutZone createWorkExperienceZone(List<Employee.WorkExperience> experiences) {
        LayoutZone zone = new LayoutZone("workExperience", LayoutZone.ZoneType.BODY_CONTENT);

        // 添加标题行
        LayoutRow titleRow = new LayoutRow();
        LayoutField titleField = new LayoutField("workTitle", "工作经历", "", 100.0);
        titleField.setForceFullWidth(true);
        titleRow.addField(titleField);
        titleRow.setEstimatedHeight(10.0);
        zone.addRow(titleRow);

        // 遍历每段工作经历
        for (int i = 0; i < experiences.size(); i++) {
            Employee.WorkExperience exp = experiences.get(i);

            Map<String, String> fieldData = new LinkedHashMap<>();
            fieldData.put("workStartDate", formatDate(exp.getStartDate()));
            fieldData.put("workEndDate", exp.getEndDate() != null ? formatDate(exp.getEndDate()) : "至今");
            fieldData.put("company", exp.getCompany());
            fieldData.put("position", exp.getPosition());
            fieldData.put("duties", exp.getDuties());

            List<LayoutField> fields = fieldFilter.filterAndConvertOrdered(
                    fieldData,
                    FieldConfigRegistry.FieldGroup.WORK_EXPERIENCE
            );

            // 贪心填充
            fillRowsGreedy(zone, fields);

            // 如果不是最后一项,添加分隔空行
            if (i < experiences.size() - 1) {
                LayoutRow separatorRow = new LayoutRow();
                separatorRow.setEstimatedHeight(5.0);
                zone.addRow(separatorRow);
            }
        }

        return zone;
    }

    /**
     * 创建教育背景区
     */
    private LayoutZone createEducationZone(List<Employee.Education> educations) {
        LayoutZone zone = new LayoutZone("education", LayoutZone.ZoneType.BODY_CONTENT);

        // 添加标题行
        LayoutRow titleRow = new LayoutRow();
        LayoutField titleField = new LayoutField("eduTitle", "教育背景", "", 100.0);
        titleField.setForceFullWidth(true);
        titleRow.addField(titleField);
        titleRow.setEstimatedHeight(10.0);
        zone.addRow(titleRow);

        for (int i = 0; i < educations.size(); i++) {
            Employee.Education edu = educations.get(i);

            Map<String, String> fieldData = new LinkedHashMap<>();
            fieldData.put("eduStartDate", formatDate(edu.getStartDate()));
            fieldData.put("eduEndDate", formatDate(edu.getEndDate()));
            fieldData.put("school", edu.getSchool());
            fieldData.put("major", edu.getMajor());
            fieldData.put("degree", edu.getDegree());

            List<LayoutField> fields = fieldFilter.filterAndConvertOrdered(
                    fieldData,
                    FieldConfigRegistry.FieldGroup.EDUCATION
            );

            fillRowsGreedy(zone, fields);

            if (i < educations.size() - 1) {
                LayoutRow separatorRow = new LayoutRow();
                separatorRow.setEstimatedHeight(5.0);
                zone.addRow(separatorRow);
            }
        }

        return zone;
    }

    /**
     * 创建家庭成员区
     */
    private LayoutZone createFamilyZone(List<Employee.FamilyMember> members) {
        LayoutZone zone = new LayoutZone("family", LayoutZone.ZoneType.BODY_CONTENT);

        // 添加标题行
        LayoutRow titleRow = new LayoutRow();
        LayoutField titleField = new LayoutField("familyTitle", "家庭成员", "", 100.0);
        titleField.setForceFullWidth(true);
        titleRow.addField(titleField);
        titleRow.setEstimatedHeight(10.0);
        zone.addRow(titleRow);

        for (int i = 0; i < members.size(); i++) {
            Employee.FamilyMember member = members.get(i);

            Map<String, String> fieldData = new LinkedHashMap<>();
            fieldData.put("relation", member.getRelation());
            fieldData.put("familyName", member.getName());
            fieldData.put("age", member.getAge() != null ? member.getAge().toString() : null);
            fieldData.put("workUnit", member.getWorkUnit());

            List<LayoutField> fields = fieldFilter.filterAndConvertOrdered(
                    fieldData,
                    FieldConfigRegistry.FieldGroup.FAMILY
            );

            fillRowsGreedy(zone, fields);

            if (i < members.size() - 1) {
                LayoutRow separatorRow = new LayoutRow();
                separatorRow.setEstimatedHeight(5.0);
                zone.addRow(separatorRow);
            }
        }

        return zone;
    }

    /**
     * 贪心填充算法 - 核心方法
     * 将字段列表按贪心策略填充到行中
     *
     * @param zone   目标分区
     * @param fields 待填充的字段列表
     */
    private void fillRowsGreedy(LayoutZone zone, List<LayoutField> fields) {
        if (fields == null || fields.isEmpty()) {
            return;
        }

        LayoutRow currentRow = new LayoutRow();

        for (LayoutField field : fields) {
            // 1. 如果字段是超长内容或强制独占行
            if (field.isLongContent()) {
                // 先结束当前行(如果有内容)
                if (!currentRow.getFields().isEmpty()) {
                    zone.addRow(currentRow);
                    currentRow = new LayoutRow();
                }

                // 该字段独占一行
                field.setWidthPercent(100.0);
                currentRow.addField(field);
                currentRow.setEstimatedHeight(calculateFieldHeight(field));
                zone.addRow(currentRow);

                // 开启新行
                currentRow = new LayoutRow();
                continue;
            }

            // 2. 尝试将字段放入当前行
            if (currentRow.canFit(field)) {
                // 能放入,直接添加
                currentRow.addField(field);
            } else {
                // 3. 放不下,需要换行

                // 先检查当前行是否为空
                if (currentRow.getFields().isEmpty()) {
                    // 当前行为空但字段放不下,说明字段本身太宽
                    // 强制放入并占满整行
                    field.setWidthPercent(100.0);
                    currentRow.addField(field);
                    currentRow.setEstimatedHeight(calculateFieldHeight(field));
                    zone.addRow(currentRow);
                    currentRow = new LayoutRow();
                } else {
                    // 当前行有内容,结束当前行
                    zone.addRow(currentRow);

                    // 开启新行并放入该字段
                    currentRow = new LayoutRow();
                    currentRow.addField(field);
                }
            }
        }

        // 4. 处理最后一行
        if (!currentRow.getFields().isEmpty()) {
            zone.addRow(currentRow);
        }
    }

    /**
     * 计算字段高度(根据内容长度估算)
     */
    private double calculateFieldHeight(LayoutField field) {
        if (field.getValue() == null) {
            return 8.0;
        }

        int length = field.getValue().length();

        if (length <= 50) {
            return 8.0;  // 单行
        } else if (length <= 100) {
            return 12.0;  // 约1.5行
        } else if (length <= 150) {
            return 16.0;  // 约2行
        } else {
            // 超长文本,按每50字符约8mm计算
            return Math.ceil(length / 50.0) * 8.0;
        }
    }

    /**
     * 格式化日期
     */
    private String formatDate(java.time.LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
