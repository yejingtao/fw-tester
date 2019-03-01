package com.fw.tester.controller.sub;

import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.fw.tester.entity.Book;
import com.fw.tester.service.BookService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class SubBookController {
	
	@Autowired
	private BookService bookService;
	
	@GetMapping(path = "/sub/account/{id}",  
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Book getBook(@PathVariable("id") @Size(min = 1) int id) {
		log.info("Sub http request params id:"+id);
		return bookService.findBookById(id);
	}
}
