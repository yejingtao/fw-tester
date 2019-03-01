package com.fw.tester.message;

import java.util.Date;
import lombok.Data;

@Data
public class BookRequest {
	 private int id;
	 private String name;
	 private String author;
	 private String version;
	 private Date time;
	 private double price;
	 
}
