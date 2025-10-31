package cn.kmdckj.epersonnelarchivegenerator.service;

import cn.kmdckj.epersonnelarchivegenerator.model.Employee;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class MockDataService {

    private static final Map<String, Employee> MOCK_DATA = new HashMap<>();

    static {
        // 场景1: 基本信息完整的员工
        Employee emp1 = new Employee();
        emp1.setId("001");
        emp1.setName("张三");
        emp1.setGender("男");
        emp1.setBirthDate(LocalDate.of(1990, 5, 15));
        emp1.setIdCard("110101199005151234");
        emp1.setNation("汉族");
        emp1.setPoliticalStatus("中共党员");
        emp1.setMaritalStatus("已婚");
        emp1.setPhone("13800138000");
        emp1.setEmail("zhangsan@example.com");
        emp1.setNativePlace("北京市东城区");
        emp1.setCurrentAddress("北京市朝阳区建国路1号院2号楼3单元401室");
        emp1.setEmergencyContact("李四");
        emp1.setEmergencyPhone("13900139000");
        emp1.setPhotoBase64("iVBORw0KGgoAAAANSUhEUgAAAJYAAACWCAYAAAA8AXHiAAAACXBIWXMAAAsTAAALEwEAmpwYAAABg0lEQVR4nO3BAQ0AAADCoPdPbQ8HFAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAOA3AvAAAbCR7QIAAAABJRU5ErkJggg==");

        Employee.WorkExperience work1 = new Employee.WorkExperience();
        work1.setStartDate(LocalDate.of(2015, 7, 1));
        work1.setEndDate(LocalDate.of(2018, 12, 31));
        work1.setCompany("ABC科技有限公司");
        work1.setPosition("Java开发工程师");
        work1.setDuties("负责公司核心业务系统的开发与维护,参与需求分析、系统设计、编码实现及测试等工作");

        Employee.WorkExperience work2 = new Employee.WorkExperience();
        work2.setStartDate(LocalDate.of(2019, 1, 1));
        work2.setEndDate(null); // 至今
        work2.setCompany("XYZ集团");
        work2.setPosition("高级Java工程师");
        work2.setDuties("负责企业级应用架构设计,带领团队完成多个大型项目的开发");

        emp1.setWorkExperiences(Arrays.asList(work1, work2));

        Employee.Education edu1 = new Employee.Education();
        edu1.setStartDate(LocalDate.of(2008, 9, 1));
        edu1.setEndDate(LocalDate.of(2012, 6, 30));
        edu1.setSchool("清华大学");
        edu1.setMajor("计算机科学与技术");
        edu1.setDegree("本科");

        Employee.Education edu2 = new Employee.Education();
        edu2.setStartDate(LocalDate.of(2012, 9, 1));
        edu2.setEndDate(LocalDate.of(2015, 6, 30));
        edu2.setSchool("清华大学");
        edu2.setMajor("软件工程");
        edu2.setDegree("硕士");

        emp1.setEducations(Arrays.asList(edu1, edu2));

        Employee.FamilyMember family1 = new Employee.FamilyMember();
        family1.setRelation("父亲");
        family1.setName("张大明");
        family1.setAge(65);
        family1.setWorkUnit("已退休");

        Employee.FamilyMember family2 = new Employee.FamilyMember();
        family2.setRelation("母亲");
        family2.setName("王秀英");
        family2.setAge(63);
        family2.setWorkUnit("已退休");

        Employee.FamilyMember family3 = new Employee.FamilyMember();
        family3.setRelation("配偶");
        family3.setName("李娜");
        family3.setAge(32);
        family3.setWorkUnit("北京市第一医院");

        emp1.setFamilyMembers(Arrays.asList(family1, family2, family3, family1, family2, family3, family1, family2, family3, family1, family2, family3));

        MOCK_DATA.put("001", emp1);


        // 场景2: 字段较少的员工(测试稀疏数据)
        Employee emp2 = new Employee();
        emp2.setId("002");
        emp2.setName("李四");
        emp2.setGender("女");
        emp2.setBirthDate(LocalDate.of(1995, 8, 20));
        emp2.setPhone("13700137000");
        emp2.setEmail("lisi@example.com");
        emp2.setPhotoBase64("iVBORw0KGgoAAAANSUhEUgAAAJYAAACWCAYAAAA8AXHiAAAACXBIWXMAAAsTAAALEwEAmpwYAAABg0lEQVR4nO3BAQ0AAADCoPdPbQ8HFAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAOA3AvAAAbCR7QIAAAABJRU5ErkJggg==");

        Employee.Education edu3 = new Employee.Education();
        edu3.setStartDate(LocalDate.of(2013, 9, 1));
        edu3.setEndDate(LocalDate.of(2017, 6, 30));
        edu3.setSchool("北京大学");
        edu3.setMajor("信息管理与信息系统");
        edu3.setDegree("本科");

        emp2.setEducations(Arrays.asList(edu3));

        MOCK_DATA.put("002", emp2);


        // 场景3: 包含超长内容的员工
        Employee emp3 = new Employee();
        emp3.setId("003");
        emp3.setName("王五");
        emp3.setGender("男");
        emp3.setBirthDate(LocalDate.of(1988, 3, 10));
        emp3.setIdCard("310101198803101234");
        emp3.setNation("汉族");
        emp3.setPoliticalStatus("群众");
        emp3.setMaritalStatus("未婚");
        emp3.setPhone("13600136000");
        emp3.setEmail("wangwu@example.com");
        emp3.setNativePlace("上海市黄浦区");
        emp3.setCurrentAddress("上海市浦东新区世纪大道1000号高银金融大厦A座2501室,靠近地铁2号线和4号线世纪大道站,交通便利");
        emp3.setPhotoBase64("iVBORw0KGgoAAAANSUhEUgAAAJYAAACWCAYAAAA8AXHiAAAACXBIWXMAAAsTAAALEwEAmpwYAAABg0lEQVR4nO3BAQ0AAADCoPdPbQ8HFAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAOA3AvAAAbCR7QIAAAABJRU5ErkJggg==");

        Employee.WorkExperience work3 = new Employee.WorkExperience();
        work3.setStartDate(LocalDate.of(2010, 7, 1));
        work3.setEndDate(LocalDate.of(2015, 12, 31));
        work3.setCompany("上海某大型互联网公司");
        work3.setPosition("后端开发工程师");
        work3.setDuties("负责公司电商平台后端服务的开发,包括但不限于:用户系统、订单系统、支付系统、库存系统等核心模块的设计与实现;参与系统架构优化,提升系统性能和稳定性;编写技术文档,指导初级工程师;参与Code Review,保证代码质量;与产品经理、前端工程师密切合作,确保需求准确实现");

        emp3.setWorkExperiences(Arrays.asList(work3));

        MOCK_DATA.put("003", emp3);
    }

    public Employee getEmployeeById(String employeeId) {
        return MOCK_DATA.get(employeeId);
    }
}