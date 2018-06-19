package e.m7eds.myapplication;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "client")
public class Client {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String company;
    private int value;
    @ColumnInfo(name = "registered_date")
    private Date registeredDate;

    @Ignore
    public Client(String name, String company, int value, Date registeredDate) {
        this.name = name;
        this.company = company;
        this.value = value;
        this.registeredDate = registeredDate;
    }

    public Client(int id, String name, String company, int value, Date registeredDate) {
        this.id = id;
        this.name = name;
        this.company = company;
        this.value = value;
        this.registeredDate = registeredDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }
}
