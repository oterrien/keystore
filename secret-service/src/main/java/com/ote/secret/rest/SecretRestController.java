package com.ote.secret.rest;


import com.ote.domain.secret.business.NotFoundException;
import com.ote.domain.secret.spi.ISecret;
import com.ote.secret.peristence.SecretEntity;
import com.ote.secret.rest.payload.SecretPayload;
import com.ote.secret.service.SecretMapperService;
import com.ote.secret.service.SecretServiceAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/secrets")
@Slf4j
public class SecretRestController {

    @Autowired
    private SecretServiceAdapter secretServiceAdapter;

    @Autowired
    private SecretMapperService secretMapperService;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public long createSecret(@RequestBody SecretPayload payload) {
        SecretEntity entity = secretMapperService.convert(payload);
        return secretServiceAdapter.create(entity);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SecretPayload findSecret(@PathVariable("id") long id) throws NotFoundException {
        SecretEntity entity = secretServiceAdapter.find(id);
        return secretMapperService.convert(entity);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void deleteSecret(@PathVariable("id") long id) throws NotFoundException {
        secretServiceAdapter.remove(id);
    }

    @RequestMapping(value = "/{id}/parent/{parentId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void changeParent(@PathVariable("id") long id, @PathVariable("parentId") long parentId) throws NotFoundException {
        SecretEntity entity = secretServiceAdapter.find(id);
        SecretEntity parent = secretServiceAdapter.find(parentId);
        secretServiceAdapter.move(entity, parent);
    }
}
