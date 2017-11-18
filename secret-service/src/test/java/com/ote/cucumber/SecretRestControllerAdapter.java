package com.ote.cucumber;

import com.ote.JsonUtils;
import com.ote.domain.secret.business.NotFoundException;
import com.ote.secret.rest.payload.SecretPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Controller
public class SecretRestControllerAdapter {

    private static final String URI = "/api/v1/secrets";

    @Autowired
    private WebConfigurationTest webConfigurationTest;

    public long createSecret(SecretPayload payload) {
        try {
            MvcResult result = webConfigurationTest.getMockMvc().perform(post(URI).
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                    content(JsonUtils.serialize(payload))).
                    andReturn();
            return Long.parseLong(result.getResponse().getContentAsString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public SecretPayload findSecret(long id) throws NotFoundException {
        try {
            MvcResult result = webConfigurationTest.getMockMvc().perform(get(URI + "/" + id)).
                    andReturn();
            return JsonUtils.parse(result.getResponse().getContentAsString(), SecretPayload.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteSecret(long id) throws NotFoundException {
        try {
            webConfigurationTest.getMockMvc().perform(delete(URI + "/" + id)).andReturn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void changeParent(long id, long parentId) throws NotFoundException {
        try {
            webConfigurationTest.getMockMvc().perform(put(URI + "/" + id + "/parent/" + parentId)).andReturn();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
