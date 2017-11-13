package com.ote.secret.rest;


import com.ote.domain.secret.api.ISecretService;
import com.ote.domain.secret.business.NotFoundException;
import com.ote.secret.peristence.SecretJpaRepository;
import com.ote.secret.rest.payload.SecretPayload;
import com.ote.secret.service.SecretMapperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api/v1/secrets")
@Slf4j
public class SecretRestController {

    @Autowired
    private SecretMapperService secretMapperService;

    @Autowired
    private ISecretService secretServiceAdapter;

    @Autowired
    private SecretJpaRepository secretJpaRepository;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Long create(@RequestBody SecretPayload payload) {
        return null;
    }

    @RequestMapping(value = "/{id}/children", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Long[] getChildren(@PathVariable long id) {
        return null;
    }

    @RequestMapping(value = "/{id}/children", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SecretPayload setChildren(@PathVariable("id") long id,
                                     @RequestBody Long[] children) {
        Arrays.stream(children, 0, children.length).forEach(p -> System.out.println(p));
        return null;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SecretPayload read(@PathVariable("id") long id,
                              @RequestParam(value = "parentDepth", defaultValue = "1") int parentDepth,
                              @RequestParam(value = "childrenDepth", defaultValue = "1") int childrenDepth) throws NotFoundException {
        return null;
    }
}
