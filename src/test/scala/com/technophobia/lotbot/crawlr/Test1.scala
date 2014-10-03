package com.technophobia.lotbot.crawlr

import org.junit.runner.RunWith
import org.scalatest.FeatureSpec
import org.scalatest.GivenWhenThen
import org.scalatest.Matchers
import org.scalatest.junit.JUnitRunner
import com.technophobia.lotbot.mock.SecondVeryStaticSite
import org.scalatest.BeforeAndAfterAll
import com.technophobia.lotbot.mock.VeryStaticInternet
import com.technophobia.lotbot.mock.VeryStaticInternet

@RunWith(classOf[JUnitRunner])
class Test1 extends FeatureSpec with GivenWhenThen with Matchers with VeryStaticInternet {
  
  info("As an employee object consumer")
  info("I want to be able to create an employee object")
  info("So I can access the first name and last name")
  info("And get the employee full name when I need it")
  info("And also get the Social Security Number")

  feature("Employee object") {
    
    scenario("Create an employee object with first and last name") {
    
      Given("an Employee object is created")      
      val employee = "Lukasz"
      
      Then("the first name and last name should be set")
      val firstName = employee
      convertToStringShouldWrapper(firstName) should be("Lukasz")
      
      val lastName = employee
      lastName should be("Szwed")
            
    }
  }
  
  feature("Employee object2") {
    
    scenario("Create an employee2 object with same first and last name") {
    
      Given("an Employee object is created")      
      val employee = "Lukasz"
      
      Then("the first name and last name should be set")
      val firstName = employee
      convertToStringShouldWrapper(firstName) should be("Lukasz")
      
      val lastName = employee
      lastName should be("Lukasz")
            
    }
  }
} 