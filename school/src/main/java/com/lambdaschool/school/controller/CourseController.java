package com.lambdaschool.school.controller;

import com.lambdaschool.school.model.Course;
import com.lambdaschool.school.model.ErrorDetails;
import com.lambdaschool.school.service.CourseService;
import com.lambdaschool.school.view.CountStudentsInCourses;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
@RequestMapping(value = "/courses")
public class CourseController
{
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    private void Log(HttpServletRequest req){
        logger.info(req.getMethod() + " " + req.getRequestURI() + " Accessed");
    }
    @Autowired
    private CourseService courseService;

    @ApiOperation(value = "Lists all courses", responseContainer = "List")
    @GetMapping(value = "/courses", produces = {"application/json"})
    public ResponseEntity<?> listAllCourses(HttpServletRequest req)
    {
        Log(req);
        ArrayList<Course> myCourses = courseService.findAll();
        return new ResponseEntity<>(myCourses, HttpStatus.OK);
    }

    @ApiOperation(value="Gets number of students in the course", responseContainer = "List")
    @GetMapping(value = "/studcount", produces = {"application/json"})
    public ResponseEntity<?> getCountStudentsInCourses(HttpServletRequest req)
    {
        Log(req);
        return new ResponseEntity<>(courseService.getCountStudentsInCourse(), HttpStatus.OK);
    }

    @ApiOperation(value = "Delete course with given Id", response = void.class)
    @ApiResponses(value = {
            @ApiResponse(code=200, message="", response = void.class),
            @ApiResponse(code=401, message = "Not Authorized", response = ErrorDetails.class),
            @ApiResponse(code=500, message = "Error Deleting", response = ErrorDetails.class)
    })
    @DeleteMapping("/courses/{courseid}")
    public ResponseEntity<?> deleteCourseById(
            @ApiParam(value = "Course Id", required = true,example = "1") @PathVariable long courseid,
            HttpServletRequest req
    ) {
        Log(req);
        courseService.delete(courseid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
