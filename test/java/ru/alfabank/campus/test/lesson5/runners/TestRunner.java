package ru.alfabank.campus.test.lesson5.runners;

import org.junit.platform.suite.api.BeforeSuite;
import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;
import ru.alfabank.campus.test.lesson5.parallel.TestClass1;
import ru.alfabank.campus.test.lesson5.parallel.TestClass2;

//@Suite
//@SuiteDisplayName("Тестовый сьют")
//@SelectPackages()
//@SelectClasses({TestClass1.class, TestClass2.class})
//@IncludeTags({"smoke", "payment"})
//@IncludeTags({"smoke & payment"})
//@ExcludeTags()
public class TestRunner {
//
//    @BeforeSuite
//    static void healthCheck() {
//        System.out.println("Мы в BeforeSuite");
//    }
}