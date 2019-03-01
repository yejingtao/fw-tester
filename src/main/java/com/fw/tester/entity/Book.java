package com.fw.tester.entity;

import java.util.Date;
import lombok.Data;

@Data
public class Book {
	 private int id;
	 private String name;
	 private String author;
	 private String version;
	 private Date time;
	 private double price;
	 
}
