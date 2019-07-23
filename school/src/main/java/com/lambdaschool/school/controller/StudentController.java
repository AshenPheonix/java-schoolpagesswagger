package com.lambdaschool.school.controller;

import com.lambdaschool.school.model.ErrorDetails;
import com.lambdaschool.school.model.Student;
import com.lambdaschool.school.service.StudentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController{


    @Autowired
    private StudentService studentService;

    // Please note there is no way to add students to course yet!
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private void Log(HttpServletRequest req){
        logger.info(req.getMethod() + " " + req.getRequestURI() + " Accessed");
    }

    @ApiOperation(value = "Lists all Students",responseContainer = "List")
    @GetMapping(value = "/students", produces = {"application/json"})
    public ResponseEntity<?> listAllStudents(HttpServletRequest req)
    {
        Log(req);
        List<Student> myStudents = studentService.findAll();
        return new ResponseEntity<>(myStudents, HttpStatus.OK);
    }

    @ApiOperation(value = "Get a Student given it's id", response = Student.class)
    @ApiResponses(value = {
            @ApiResponse(code=404,message = "Student Not Found", response = ErrorDetails.class),
            @ApiResponse(code=401,message = "Not Authorized", response = ErrorDetails.class)
    })
    @GetMapping(value = "/Student/{StudentId}",
                produces = {"application/json"})
    public ResponseEntity<?> getStudentById(
            @ApiParam(name = "Student Id", required = true, example = "1") @PathVariable Long StudentId,
            HttpServletRequest req
    ) {
        Log(req);
        Student r = studentService.findStudentById(StudentId);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @ApiOperation(value = "Get Students by a matched name container", responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code=404,message = "No Students Found", response = ErrorDetails.class),
            @ApiResponse(code=401,message = "Not Authorized", response = ErrorDetails.class)
    })
    @GetMapping(value = "/student/namelike/{name}",
                produces = {"application/json"})
    public ResponseEntity<?> getStudentByNameContaining(
            @ApiParam(name = "Name",required = true, example = "Name") @PathVariable String name,
            HttpServletRequest req
    ) {
        Log(req);
        List<Student> myStudents = studentService.findStudentByNameLike(name);
        return new ResponseEntity<>(myStudents, HttpStatus.OK);
    }

    @ApiOperation(value="Add Student", response = void.class, notes = "Location of new Student in Location header")
    @ApiResponses(value = {
            @ApiResponse(code=401, message="Not Authorized", response = ErrorDetails.class),
            @ApiResponse(code=500, message="Error Creating Student", response = ErrorDetails.class)
    })
    @PostMapping(value = "/Student",
                 consumes = {"application/json"},
                 produces = {"application/json"})
    public ResponseEntity<?> addNewStudent(@Valid
                                           @RequestBody Student newStudent,
                                           HttpServletRequest req) throws URISyntaxException
    {
        Log(req);
        newStudent = studentService.save(newStudent);

        // set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newStudentURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{Studentid}").buildAndExpand(newStudent.getStudid()).toUri();
        responseHeaders.setLocation(newStudentURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }


    @ApiOperation(value = "Edit a Student given their Id", response = void.class)
    @ApiResponses(value = {
            @ApiResponse(code=404, message = "Student Not Found", response = ErrorDetails.class),
            @ApiResponse(code=401,message = "Not Authorized", response = ErrorDetails.class),
            @ApiResponse(code = 404, message = "Student Not Found", response = ErrorDetails.class)
    })
    @PutMapping(value = "/Student/{Studentid}")
    public ResponseEntity<?> updateStudent(
            @RequestBody
                    Student updateStudent,
            @ApiParam(name = "Student Id", required = true, example = "1") @PathVariable long Studentid,
            HttpServletRequest req
    ) {
        Log(req);
        studentService.update(updateStudent, Studentid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Delete Student Given their Id", response = void.class)
    @ApiResponses(value = {
            @ApiResponse(code=401, message = "Not Authorized", response = ErrorDetails.class),
            @ApiResponse(code = 404, message = "Not Found", response = ErrorDetails.class),
            @ApiResponse(code=500,message = "Error Deleting", response = ErrorDetails.class)
    })
    @DeleteMapping("/Student/{Studentid}")
    public ResponseEntity<?> deleteStudentById(
            @ApiParam(name = "Student Id", required = true, example = "1") @PathVariable long Studentid,
            HttpServletRequest req)
    {
        Log(req);
        studentService.delete(Studentid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
