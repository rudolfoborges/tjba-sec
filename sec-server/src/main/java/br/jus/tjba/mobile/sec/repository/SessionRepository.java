package br.jus.tjba.mobile.sec.repository;

import org.springframework.data.repository.CrudRepository;

import br.jus.tjba.mobile.sec.model.Session;

/**
 * Created by rudolfoborges on 19/04/17.
 */
public interface SessionRepository extends CrudRepository<Session, String> {
}
