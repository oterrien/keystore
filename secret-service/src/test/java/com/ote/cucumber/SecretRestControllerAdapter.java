package com.ote.cucumber;

import com.ote.JsonUtils;
import com.ote.domain.secret.business.NotFoundException;
import com.ote.secret.rest.payload.SecretPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Controller
public class SecretRestControllerAdapter {

    private static final String URI = "/api/v1/secrets";

    @Autowired
    private WebConfigurationTest webConfigurationTest;

    public void createSecret(SecretPayload payload) {
        try {
            MvcResult result = webConfigurationTest.getMockMvc().perform(post(URI).
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                    content(JsonUtils.serialize(payload))).
                    andReturn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public SecretPayload findSecret(String name) throws NotFoundException {
        try {
            MvcResult result = webConfigurationTest.getMockMvc().perform(get(URI + "/" + name)).andReturn();
            if (result.getResolvedException() != null) {
                throw result.getResolvedException();
            }
            return JsonUtils.parse(result.getResponse().getContentAsString(), SecretPayload.class);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
