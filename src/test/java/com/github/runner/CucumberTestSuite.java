package com.github.runner;

import org.junit.platform.suite.api.*;

import static io.cucumber.junit.platform.engine.Constants.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.github.stepdefinitions")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@GitHub and (@smoke or @regression)")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "net.serenitybdd.cucumber.core.plugin.SerenityReporter")
public class CucumberTestSuite {
}
