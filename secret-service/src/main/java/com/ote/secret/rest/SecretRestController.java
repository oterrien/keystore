package com.ote.secret.rest;


import com.ote.domain.secret.api.ISecretService;
import com.ote.domain.secret.business.NotFoundException;
import com.ote.secret.peristence.SecretEntity;
import com.ote.secret.rest.payload.SecretPayload;
import com.ote.secret.service.SecretMapperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/secrets")
@Slf4j
public class SecretRestController {

    @Autowired
    private SecretMapperService secretMapperService;

    @Autowired
    private ISecretService secretServiceAdapter;

    private List<SecretPayload> secretList = new ArrayList<>();

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SecretPayload create(@RequestBody SecretPayload payload) {

        secretList.add(payload);
        return payload;
    }

    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void createEntity() {

        SecretEntity entity = new SecretEntity();
        entity.setName("Root");

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SecretPayload read(@PathVariable("id") long id) throws NotFoundException{

        return secretList.stream().filter(p -> p.getId()==id).findAny().orElseThrow(()-> new NotFoundException(Long.toString(id)));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<SecretPayload> readAll(@RequestParam(value = "withDetails", defaultValue = "${application.with-details.default}") boolean withDetails) {
       return secretList;
    }

}
