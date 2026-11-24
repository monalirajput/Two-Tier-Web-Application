package com.studentapp;

public class Student {
    private int id;
    private String name, address, qualification, percentage, yearPassed;
    private int age;

    public Student() {}
    public Student(int id, String name, String address, int age, String qualification, String percentage, String yearPassed) {
        this.id=id; this.name=name; this.address=address; this.age=age;
        this.qualification=qualification; this.percentage=percentage; this.yearPassed=yearPassed;
    }
    public int getId(){return id;} public void setId(int v){id=v;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getAddress(){return address;} public void setAddress(String v){address=v;}
    public int getAge(){return age;} public void setAge(int v){age=v;}
    public String getQualification(){return qualification;} public void setQualification(String v){qualification=v;}
    public String getPercentage(){return percentage;} public void setPercentage(String v){percentage=v;}
    public String getYearPassed(){return yearPassed;} public void setYearPassed(String v){yearPassed=v;}
}
