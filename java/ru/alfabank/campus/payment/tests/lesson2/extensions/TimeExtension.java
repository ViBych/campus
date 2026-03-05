package ru.alfabank.campus.payment.tests.lesson2.extensions;

import org.joda.time.DateTime;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class TimeExtension implements BeforeTestExecutionCallback, BeforeEachCallback {

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        System.out.println(DateTime.now());
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        long threadId = Thread.currentThread().threadId();
        String threadName = Thread.currentThread().getName();
        long processId = ProcessHandle.current().pid();

        System.out.println(String.format("PID:%s | TName:%s | TID:%s", processId, threadName, threadId));
    }
}
