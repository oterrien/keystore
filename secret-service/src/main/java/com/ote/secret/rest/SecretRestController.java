package com.ote.secret.rest;


import com.ote.domain.secret.api.ISecretService;
import com.ote.secret.peristence.SecretEntity;
import com.ote.secret.peristence.SecretJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/secrets")
@Slf4j
public class SecretRestController {

    @Autowired
    private SecretJpaRepository secretRepository;

    @Autowired
    private SecretMapperService secretMapperService;

    @Autowired
    private ISecretService secretServiceAdapter;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SecretPayload create(@RequestBody SecretPayload payload, @RequestParam(value = "withDetails", defaultValue = "${application.with-details.default}") boolean withDetails) {
        SecretEntity entity = secretMapperService.convert(payload);
        entity = secretRepository.save(entity);

        return secretMapperService.convert(entity, withDetails);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SecretPayload read(@PathVariable("id") long id, @RequestParam(value = "withDetails", defaultValue = "${application.with-details.default}") boolean withDetails) {
        SecretEntity entity = secretRepository.findOne(id);
        return secretMapperService.convert(entity, withDetails);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<SecretPayload> readAll(@RequestParam(value = "withDetails", defaultValue = "${application.with-details.default}") boolean withDetails) {
        List<SecretEntity> entities = secretRepository.findAll();
        return secretMapperService.convert(entities, withDetails);
    }

}
