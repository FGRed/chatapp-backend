package net.chatapp.service;

import java.util.Collection;

public interface BasicService<S, ID> {

    public S saveNew(S entity);

    public S update(S entity);

    public S findById(ID id);

    public void deleteById(ID id);

    public void delete(S entity);

    public void deleteAll(Collection<S> entities);

    public Collection<S> saveAll(Collection<S> entities);

}
