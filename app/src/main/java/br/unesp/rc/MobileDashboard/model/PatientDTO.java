package br.unesp.rc.MobileDashboard.model;

import androidx.annotation.NonNull;

public class PatientDTO {

    private int idPaciente;
    private String firstName;
    private String lastName;
    private float height;
    private float weight;
    private int age;
    private String port;

    public PatientDTO() {
    }

    public PatientDTO(int idPaciente, String firstName, String lastName, float height, float weight, int age, String port) {
        this.idPaciente = idPaciente;
        this.firstName = firstName;
        this.lastName = lastName;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.port = port;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @NonNull
    @Override
    public String toString() {
        return "PatientDTO{" + "idPaciente=" + idPaciente + ", firstName=" + firstName + ", lastName=" + lastName + ", height=" + height + ", weight=" + weight + ", age=" + age + ", port=" + port + '}';
    }
}
