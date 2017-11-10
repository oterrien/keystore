package com.ote.domain.secret.business;

import com.ote.domain.secret.api.ISecretService;
import com.ote.domain.secret.business.model.Group;
import com.ote.domain.secret.business.model.Value;
import com.ote.domain.secret.spi.ISecret;
import com.ote.domain.secret.spi.ISecretRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class SecretService implements ISecretService {

    private final ISecretRepository secretRepository;

    @Override
    public long createValue(String name, String secretValue, Group parent) {
        if (log.isTraceEnabled()) {
            log.trace("Create the value '" + name + "' under group '" + parent.getName() + "'");
        }
        Value value = new Value();
        value.setName(name);
        value.setValue(secretValue);
        value.setParent(parent);
        parent.addChildren(value);
        return secretRepository.create(value);
    }

    @Override
    public long createGroup(String name, Group parent, ISecret... children) {
        if (log.isTraceEnabled()) {
            log.trace("Create the group '" + name + "' under group '" + parent.getName() + "'");
        }
        Group group = new Group();
        group.setName(name);
        group.setParent(parent);
        if (children.length > 0) {
            group.addChildren(children);
        }
        parent.addChildren(group);
        return secretRepository.create(group);
    }

    @Override
    public void addChildren(Group group, ISecret... children) {
        if (children.length > 0) {
            if (log.isTraceEnabled()) {
                log.trace("add " + children.length + " children to group '" + group.getName() + "'");
            }
            // TODO : what happens when a group is moved from root for example to become children. It should disappear from root.children
            group.addChildren(children);
            secretRepository.update(group.getId(), group);

        }
    }
}
