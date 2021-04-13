package com.hotel

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.test.json.JacksonTester
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE

class MockMvcTest extends Specification {

    protected MockMvc mockMvc

    def setupMvc(controller) {
        JacksonTester.initFields(this, new ObjectMapper())
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .build()
    }

    MockHttpServletResponse post(String path, String content = "") {
        mockMvc.perform(buildPostRequest(path, content))
                .andReturn()
                .response
    }

    private static MockHttpServletRequestBuilder buildPostRequest(String path, String content = "") {
        def request = MockMvcRequestBuilders.post(path)
        if (content?.trim()) {
            request.contentType(APPLICATION_JSON_VALUE)
                    .content(content)
                    .accept(APPLICATION_JSON_VALUE)
        }

        request
    }
}
