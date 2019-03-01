package com.fw.tester.service;

import java.util.Date;

import org.springframework.stereotype.Service;
import com.fw.tester.entity.Book;
import com.fw.tester.message.BookRequest;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Slf4j
@Service
public class BookService {
	
	public Book findBookById(int id) {
		log.info("find book by id"+id);
		Book book = new Book();
		book.setAuthor("yejingtao");
		book.setId(id);
		book.setName("Who am i");
		book.setPrice(23.67);
		book.setTime(new Date());
		book.setVersion("V1");
		JSONObject jsonObj = JSONObject.fromObject(book);
		if(id == 2) {
			throw new RuntimeException();
		}
		log.info(jsonObj.toString());
		return book;
	}
	
	public int createBook(BookRequest book) {
		return 2;
	}
}
