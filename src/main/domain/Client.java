package main.domain;

import java.sql.Timestamp;

import main.annotations.KeyType;
import main.annotations.ProjectColumn;
import main.annotations.ProjectTable;
import main.dao.Persistent;

@ProjectTable("TB_CLIENTS")
public class Client implements Persistent {

	@ProjectColumn(dbName = "id", setJavaName = "setId")
	private Long id;

    @ProjectColumn(dbName = "name", setJavaName = "setName")
	private String name;

	@KeyType("getCpf")
	@ProjectColumn(dbName = "cpf", setJavaName = "setCpf")
    private Long cpf;

	@ProjectColumn(dbName = "fone", setJavaName = "setFone")
    private Long fone;

	@ProjectColumn(dbName = "address", setJavaName = "setAddress")
    private String address;

	@ProjectColumn(dbName = "number", setJavaName = "setNumber")
    private Integer number;

	@ProjectColumn(dbName = "city", setJavaName = "setCity")
    private String city;

	@ProjectColumn(dbName = "state", setJavaName = "setState")
    private String state;

    @ProjectColumn(dbName = "creation_date", setJavaName = "setCreationDate")
    private Timestamp creationDate;

    @Override
    public String toString() {
        return "Client [id=" + id + ", name=" + name + ", cpf=" + cpf + ", fone=" + fone + ", address=" + address
                + ", number=" + number + ", city=" + city + ", state=" + state + ", creationDate=" + creationDate + "]";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCpf() {
        return cpf;
    }

    public void setCpf(Long cpf) {
        this.cpf = cpf;
    }

    public Long getFone() {
        return fone;
    }

    public void setFone(Long fone) {
        this.fone = fone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }


}
