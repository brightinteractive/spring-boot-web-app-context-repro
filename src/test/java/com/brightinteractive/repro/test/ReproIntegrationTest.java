package com.brightinteractive.repro.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;

import com.brightinteractive.repro.ReproApplication;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SpringApplicationConfiguration(classes = ReproApplication.class)
@ActiveProfiles("test")
public @interface ReproIntegrationTest
{
}
