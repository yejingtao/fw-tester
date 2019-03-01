package com.fw.tester.controller;

import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.fw.tester.entity.Book;
import com.fw.tester.message.BookRequest;
import com.fw.tester.message.BookResponse;
import com.fw.tester.service.BookService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.springframework.http.HttpStatus;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class BookController {

	@Autowired
	private BookService bookService;

	@GetMapping(path = "/book/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(tags = "BOOK", value = "Get a specific Book", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "id",paramType = "path", required = true) })
	@ApiResponses({ @ApiResponse(code = 200, message = "OK", response = Book.class) })
	public Book getBook(@PathVariable("id") @Size(min = 1) String id) {
		log.info("Http request params id:" + id);
		return bookService.findBookById(Integer.parseInt(id));
	}

	@PostMapping(path = "/book", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(tags = "BOOK", value = "Create an book", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "book", value = "book Info", required = true, paramType = "body", dataType = "BookRequest") })
	@ApiResponses({ @ApiResponse(code = 201, message = "Created", response = BookResponse.class) })
	public BookResponse createBook(@RequestBody @Validated BookRequest book) {
		int i = bookService.createBook(book);
		BookResponse bookResponse = new BookResponse();
		bookResponse.setResult("result is:" + i);
		return bookResponse;
	}
}
