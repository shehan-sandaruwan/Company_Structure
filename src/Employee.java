import java.util.ArrayList;
import java.util.List;

public class Employee {

    private String name;
    private double salary;
    private int employeeId;
    private Employee manager;

    public Employee(String name,double baseSalary){
        this.name = name;
        this.salary = baseSalary;
    }

    public Employee(String name){
        this.name = name;
    }

    public double getBaseSalary() {
        return salary;
    }

    public void setBaseSalary(double salary) {
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public int getEmployeeId() {
        return employeeId;
    }
    public void setManager(Employee manager){
        this.manager = manager;
    }
    public Employee getManager(){
        return manager;
    }
    public boolean equals(Employee other){
        if(this.employeeId == other.employeeId){
            return true;
        }
        return false;
    }
}

class TechnicalEmployee extends Employee{
    private int checkIns;
    private TechnicalLead manager;
    public TechnicalEmployee(String name){
        super(name);
        this.setBaseSalary(75000);
    }

    public void setCheckIns(int checkIns){
        this.checkIns += checkIns;
    }
    public int getCheckIns() {
        return checkIns;
    }
    public TechnicalLead getManager(){
        return this.manager;
    }

    public void setManager(TechnicalLead manager) {
        this.manager = manager;
    }

    public String employeeStatus(){
        return this.getEmployeeId()+this.getName()+" has "+ checkIns + " Successful checks ins";
    }
}
 class BusinessEmployee extends Employee {
      private double bonusBudget;
      private BusinessLead manager;
    public BusinessEmployee(String name){
        super(name);
        this.setBaseSalary(50000);
    }
     public double getBonusBudget() {
         return bonusBudget;
     }

     public void setBonusBudget(double bonusBudget) {
         this.bonusBudget = bonusBudget;
     }
     public BusinessEmployee getManager(){
        return this.manager;
     }
     public void setManager(BusinessLead manager) {
         this.manager = manager;
     }

     public String employeeStatus(){
        return getEmployeeId()+" "+ getName()+" "+"with a budget of "+getBonusBudget();
     }
 }

 class SoftwareEngineer extends TechnicalEmployee{

    private boolean codeAccess = false;
    private int checkIns;

    public SoftwareEngineer(String name){
        super(name);
        this.checkIns = 0;
    }

    public boolean getCodeAccess(){
        return this.codeAccess;
    }

     public void setCodeAccess(boolean codeAccess) {
         this.codeAccess = codeAccess;
     }
     public int getSuccessfulCheckIns(){
        return this.checkIns;
     }
     public boolean checkInCode(){
        if(this.getManager().approveCheckIn(this)){
            this.setCheckIns(this.getCheckIns()+1);
            return true;
        }
        return false;
     }

 }

 class Accountant extends BusinessEmployee{

    private TechnicalLead supportTeam;


    public Accountant(String name){
        super(name);
        this.setBonusBudget(0.0);
        this.supportTeam = null;
    }

     public TechnicalLead getTeamSupported(){
        if(this.supportTeam != null){
            return this.supportTeam;
        }
        return null;
     }
     public void supportTeam(TechnicalLead lead){
        this.supportTeam = lead;
         int totalBaseSalary = 0;
         for (SoftwareEngineer se: lead.getDirectReport()) {
             totalBaseSalary += se.getBaseSalary();
         }
        this.setBonusBudget(totalBaseSalary+ totalBaseSalary*0.1);

     }

     public boolean approveBonus(double bonus){
        if(getTeamSupported()==null){
            return false;
        }
        if (this.getBonusBudget()>=bonus){
            return true;
        }
        return false;
     }
     public String employeeStatus(){
        return this.getEmployeeId()+" "+this.getName() + " with a budget of "+this.getBonusBudget()+" is suporting "+this.supportTeam.getName();
     }
 }
 class TechnicalLead extends TechnicalEmployee{
     private BusinessLead supportTeam ;
    private int headCount;
    private List<SoftwareEngineer> directReport = new ArrayList<>();

    public TechnicalLead(String name){
        super(name);
        this.headCount = 4;
        this.setManager(this);
        this.setBaseSalary(75000*1.3);

    }

    public boolean hasHeadCount(){
        if(this.headCount>this.directReport.size()){
            return true;
        }
        return false;
     }
     public List<SoftwareEngineer> getDirectReport(){
        return directReport;
     }
     public boolean addReport(SoftwareEngineer e){
        if(hasHeadCount()){
            this.directReport.add(e);
            return true;
        }
        return false;
     }

     public boolean approveCheckIn(SoftwareEngineer e){
        if(e.getCodeAccess() && addReport(e)){
            return true;
        }
        return false;
     }
     public void setSupportTeam(BusinessLead lead){
        this.supportTeam = lead;
     }

     public boolean requestBonus(Employee e, double bonus){
        if(supportTeam.approveBonus(e,bonus)){
            return true;
        }
        return false;
     }

     public String getTeamStatus(){
        if(directReport == null){
            return this.employeeStatus()+" and no direct reports yet";
        }
        String status = null;
         for( SoftwareEngineer se: directReport){
             status += se.employeeStatus();
         }
        return  this.employeeStatus()+" is managing" + "/n " + status;


     }
 }

 class BusinessLead extends BusinessEmployee{
    private int headCount;
    private List<Accountant> directReport = new ArrayList<>();

   public BusinessLead(String name){
       super(name);
       this.headCount = 10;
       this.setManager(this);

   }
     public boolean hasHeadCount(){
        if(this.directReport.size()<this.headCount){
            return true;
        }
        return false;
     }
     public boolean addReport(Accountant e, TechnicalLead supportTeam){
       if(hasHeadCount()){
           this.headCount++;
           this.directReport.add(e);
           this.setBonusBudget(e.getBaseSalary()*1.1);
           e.supportTeam(supportTeam);
           supportTeam.setSupportTeam(this);
           return true;
       }
       return false;
     }

     public boolean requestBonus(Employee e, double bonus){
       if(this.getBonusBudget()>= bonus){
           e.setBaseSalary(bonus);
           this.setBonusBudget(this.getBonusBudget()-bonus);
           return true;
       }
       return false;
     }

     public boolean approveBonus(Employee e, double bonus){
         for ( Accountant member:directReport) {
             if(e.getManager()== member.getManager()){
                 member.approveBonus(bonus);
             }
         }
         return false;
     }
     public String getTeamStatus(){
       String status = null;
         for (Accountant a: directReport) {
             status += a.employeeStatus();
         }
         if(directReport == null){
             return this.employeeStatus();
         }
         return this.employeeStatus() + status;
     }
 }
 class CompanyStructure {
    public static void main(String[] args) {
        TechnicalLead CTO = new TechnicalLead("Satya Nadella");
        SoftwareEngineer seA = new SoftwareEngineer("Kasey");
        SoftwareEngineer seB = new SoftwareEngineer("Breana");
        SoftwareEngineer seC = new SoftwareEngineer("Eric");
        CTO.addReport(seA);
        CTO.addReport(seB);
        CTO.addReport(seC);
        System.out.println(CTO.getTeamStatus());

        TechnicalLead VPofENG = new TechnicalLead("Bill Gates");
        SoftwareEngineer seD = new SoftwareEngineer("Winter");
        SoftwareEngineer seE = new SoftwareEngineer("Libby");
        SoftwareEngineer seF = new SoftwareEngineer("Gizan");
        SoftwareEngineer seG = new SoftwareEngineer("Zaynah");
        VPofENG.addReport(seD);
        VPofENG.addReport(seE);
        VPofENG.addReport(seF);
        VPofENG.addReport(seG);
        System.out.println(VPofENG.getTeamStatus());

        BusinessLead CFO = new BusinessLead("Amy Hood");
        Accountant actA = new Accountant("Niky");
        Accountant actB = new Accountant("Andrew");
        CFO.addReport(actA, CTO);
        CFO.addReport(actB, VPofENG);
        System.out.println(CFO.getTeamStatus());
    }
}